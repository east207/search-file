package com.gzh.controller;


import com.gzh.pojo.dto.FileSearchRequest;
import com.gzh.pojo.vo.FileUpLoadResponse;
import com.gzh.pojo.vo.ResponseVO;
import com.gzh.service.SearchFileService;
import com.gzh.service.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.IOException;


@Slf4j
@RestController
public class FileUpLoadController {

    @Resource
    private UploadService uploadService;

    @Resource
    private SearchFileService searchFileService;


    @PostMapping("/upload")
    public ResponseVO<FileUpLoadResponse> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return uploadService.upload(file);
    }


    @GetMapping("/getFileByName")
    public ResponseVO<FileUpLoadResponse> getFileByName(@RequestParam("fileName") String fileName) throws Exception {
        return uploadService.getFileByName(fileName);
    }

    // 根据关键字查询es，返回文章url，文章原文，文章名称
    @GetMapping("/searchContent")
    public ResponseVO<FileUpLoadResponse> searchContent(@RequestParam FileSearchRequest fileSearchRequest) throws Exception {
        return searchFileService.searchFile(fileSearchRequest);
    }

}




