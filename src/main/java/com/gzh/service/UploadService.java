package com.gzh.service;

import com.gzh.pojo.vo.FileUpLoadResponse;
import com.gzh.pojo.vo.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploadService {

    ResponseVO<FileUpLoadResponse> upload(MultipartFile file) throws IOException;

    ResponseVO<FileUpLoadResponse> getFileByName(String fileName) throws Exception;
}
