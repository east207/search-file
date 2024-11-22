package com.gzh.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import com.gzh.config.minio.MinioProperties;
import com.gzh.pojo.Tika.TikaParaseObject;
import com.gzh.pojo.vo.FileUpLoadResponse;
import com.gzh.pojo.vo.ResponseVO;
import com.gzh.service.UploadService;
import com.gzh.utils.StringTools;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.*;

import static com.gzh.pojo.enums.ResponseCodeEnum.*;


@Slf4j
@Service
public class UploadSerivicelmpl implements UploadService {



    @Resource
    private Tika tika;

    @Resource
    private ElasticsearchClient esClient;

    @Resource
    private MinioClient minioClient;

    @Autowired
    private MinioProperties minioProperties;

    @Autowired
    private ThreadPoolTaskExecutor defaultExecutor;

    // 索引库的名称
    private static final String indexName = "file_content_search";

    @Override
    public ResponseVO<FileUpLoadResponse> upload(MultipartFile file) throws IOException {


        // 文件上传成功后返回的文件信息
        FileUpLoadResponse res = new FileUpLoadResponse();
        ResponseVO<FileUpLoadResponse> response = new ResponseVO<>(CODE_200.getMsg(), CODE_200.getCode());
        String fileSize = StringTools.generateByte(file.getSize());

        CountDownLatch latch = new CountDownLatch(2);

        // 将MultipartFile对象转换为File对象，如果文件为空
        if (file.isEmpty()) {
            response.setCode(CODE_602.getCode());
            response.setInfo(CODE_602.getMsg());
            return response;
        }

        String filename = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        File tempFile = File.createTempFile(filename, ".tmp");
        file.transferTo(tempFile);
        String fileContentType = file.getContentType(); // 文件类型

        // 两个任务异步执行
        defaultExecutor.execute(() -> {
            try {
                paraseAndWriteEs(tempFile, filename);
                res.setFileName(filename);
                res.setFileType(fileContentType);
            } catch (Exception e) {
                response.setCode(CODE_603.getCode());
                response.setInfo(CODE_603.getMsg());
                log.error(e.getMessage(), e);
            }
            finally {
                latch.countDown();
            }
        });

        defaultExecutor.execute(() -> {
            try {
                String url = writeMinio(tempFile, filename, fileContentType);
                res.setFileUrl(url);
                res.setFileSize(fileSize);
            } catch (Exception e) {
                response.setCode(CODE_603.getCode());
                response.setInfo(CODE_603.getMsg());
                log.error(e.getMessage(), e);
            } finally {
                latch.countDown();
            }
        });


        // 关闭服务
        try {
            response.setData(res);
            latch.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } finally {
            defaultExecutor.shutdown();
        }

        // 返回结果
        return response;

    }

    @Override
    public ResponseVO<FileUpLoadResponse> getFileByName(String fileName) throws Exception {
        String url = getFilePreSignedUrl(fileName);
        FileUpLoadResponse res = new FileUpLoadResponse();
        res.setFileUrl(url);
        ResponseVO<FileUpLoadResponse> response = new ResponseVO<>(CODE_200.getMsg(), CODE_200.getCode());
        response.setData(res);
        return response;
    }


    /**
     * 解析字段并且写入es
     * @param file
     * @param filename
     * @throws Exception
     */
    private void paraseAndWriteEs(File file, String filename) throws Exception {
        TikaParaseObject paraseObject = parseByTika(file, filename);
        IndexRequest<Object> request = IndexRequest.of(item -> item
                .index(indexName)
                .document(paraseObject)
        );
        esClient.index(request);
    }

    /**
     * 将文件写入minio，并且返回文件临时访问路径
     * @param file
     * @param filename
     * @param fileType
     * @return
     * @throws Exception
     */
    protected String writeMinio(File file, String filename, String fileType) throws Exception {

        // 上传文件
        FileInputStream fileInputStream = new FileInputStream(file);
            PutObjectArgs objectArgs = PutObjectArgs.builder()
                    .object(filename)
                    .contentType(fileType)
                    .bucket(minioProperties.getBucketName())
                    .stream(fileInputStream, file.length(), -1)
                    .build();
        minioClient.putObject(objectArgs);

        return getFilePreSignedUrl(filename);

    }

    /**
     * 通过Tika解析文件
     * @return 返回Tika解析后的对象
     */
    private TikaParaseObject parseByTika(File tempFile, String name) throws TikaException, IOException {
        TikaParaseObject paraseObject = new TikaParaseObject();
        paraseObject.setFileName(name);
        paraseObject.setFileContent(tika.parseToString(tempFile));
        return paraseObject;
    }

    /**
     * 获取对象预签名URL，这个url短期有效
     * @return
     */
    private String getFilePreSignedUrl(String fileName) throws Exception {
        // 生成临时资源路径
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minioProperties.getBucketName())
                        .object(fileName)
                        .expiry(2 * 60)
                        .build()
        );
    }

}
