package com.gzh.pojo.dto;

import lombok.Data;
import java.io.Serializable;


@Data
public class UserRegisterDto implements Serializable {


        // 这个是时间戳
        private String checkcodeKey;

        private String email;

        private String password;

        private String nickName;

        // 这个是用户输入的验证码
        private String checkCode;




}
