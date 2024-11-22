package com.gzh.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.gzh.pojo.dto.FileSearchRequest;
import com.gzh.pojo.vo.FileUpLoadResponse;
import com.gzh.pojo.vo.ResponseVO;
import com.gzh.service.SearchFileService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;


@Service
@Slf4j
public class SearchFileServiceImpl implements SearchFileService {

    @Resource
    private ElasticsearchClient esClient;



    @Override
    public ResponseVO<FileUpLoadResponse> searchFile(FileSearchRequest fileSearchRequest) {


        SearchRequest searchRequest = new SearchRequest.Builder()
                .index(fileSearchRequest.getIndexName())
                .query(
                        q -> q.match(m -> m
                                .field(fileSearchRequest.getFileName())
                                .query(fileSearchRequest.getSearchValue())
                        )
                ).build();

        try {
            SearchResponse<Object> searchResponse = esClient.search(searchRequest, Object.class);

            for (Hit<Object> hit : searchResponse.hits().hits()) {
                Object source = hit.source();
                if (source instanceof Map) {
                    Map<String, Object> map = (Map<String, Object>) source;
//                    System.out.println(map.get());
                }
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
        }

        return null;
    }
}
