package com.gzh.config.es;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.elasticsearch")
public class ElasticsearchProperties {

    private String urls;

    private String host;

    private int port;

    private String protocol;

    private String username;

    private String password;

}