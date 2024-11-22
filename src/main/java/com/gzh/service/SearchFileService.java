package com.gzh.service;

import com.gzh.pojo.dto.FileSearchRequest;
import com.gzh.pojo.vo.FileUpLoadResponse;
import com.gzh.pojo.vo.ResponseVO;

public interface SearchFileService {


    ResponseVO<FileUpLoadResponse> searchFile(FileSearchRequest fileSearchRequest);
}
