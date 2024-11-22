package com.gzh.pojo.vo;


import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

@Data
public class FileUpLoadResponse {

    private String fileName;
    private String fileUrl;
    private String fileType;
    private String fileSize;

}
