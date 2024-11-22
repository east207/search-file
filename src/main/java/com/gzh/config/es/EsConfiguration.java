package com.gzh.config.es;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.annotation.Resource;

@Configuration
public class EsConfiguration {


    @Resource
    private ElasticsearchProperties properties;

    @Bean
    public ElasticsearchClient esClient(@Autowired(required = false) JsonpMapper jsonpMapper) {

        if (jsonpMapper == null) {
            jsonpMapper = new JacksonJsonpMapper();
        }
        RestClient client = RestClient.builder(new HttpHost(
                "192.168.1.12",
                9200,
                "http"
        )).build();

        RestClientTransport transport = new RestClientTransport(
                client, jsonpMapper
        );

        return new ElasticsearchClient(transport);
    }


}
