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


        // 用户名和密码，这是标准写法
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(properties.getUsername(), properties.getPassword()));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(
                        properties.getHost(),
                        properties.getPort(),
                        properties.getProtocol()
                )
        ).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        });

        RestClient client = builder.build();

        RestClientTransport transport = new RestClientTransport(client, jsonpMapper);

        return new ElasticsearchClient(transport);
    }


}
