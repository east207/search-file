package com.gzh.controller;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;




public class ArkChatClient {
    public static void main(String[] args) {
        String url = "https://ark.cn-beijing.volces.com/api/v3/chat/completions";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("24e2dd2c-7870-4758-90fa-77b596a62dbe");

        Map<String, Object> body = new HashMap<>();
        body.put("model", "doubao-1-5-thinking-pro-250415");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "system", "content", "你是人工智能助手."));
        messages.add(Map.of("role", "user", "content", "你好"));

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("响应状态码: " + response.getStatusCode());
        System.out.println("响应体: " + response.getBody());
    }
}