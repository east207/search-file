package com.gzh.pojo.dto;


import lombok.Data;

@Data
public class FileSearchRequest {

    // 索引库名称
    private String indexName;

    private String fileName;

    private String searchValue;


}
