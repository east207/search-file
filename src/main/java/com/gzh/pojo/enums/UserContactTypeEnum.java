package com.gzh.pojo.enums;


public enum UserContactTypeEnum {


    USER(0, "U", "好友"), GROUP(1, "G", "群聊");



    private Integer type;
    private String prefix;
    private String desc;


    UserContactTypeEnum(Integer type, String prefix, String desc) {
        this.type = type;
        this.prefix = prefix;
        this.desc = desc;
    }
}
