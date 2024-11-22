package com.gzh.pojo.vo;

import lombok.Data;

import static com.gzh.pojo.enums.ResponseCodeEnum.CODE_200;

@Data
public class ResponseVO<T> {
    private String status;
    private Integer code;
    private String info;
    private T data;

    public ResponseVO() {

    }

    public ResponseVO(String status, Integer code) {
        this.status = status;
        this.code = code;
    }


}
