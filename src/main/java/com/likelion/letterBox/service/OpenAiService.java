package com.likelion.letterBox.service;

import com.likelion.letterBox.dto.OpenApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class OpenAiService {
    private final RestTemplate restTemplate;

    @Value("${openai.secret}")
    private String openAiApiKey;

    public String translateToEnglish(String text) {
        String url = "https://api.openai.com/v1/completions"; // OpenAI 번역 API 엔드포인트

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + openAiApiKey);

        // 요청 본문 구성
        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("model", "gpt-3.5-turbo-16k");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", "다음의 문자열을 영어로 번역해줘"+text));
        requestJson.put("messages", messages);

        int maxOutputTokens=1800;
        requestJson.put("max_tokens", maxOutputTokens);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<OpenApiResponseDto> response = restTemplate.postForEntity(url, entity, OpenApiResponseDto.class);

        // 응답에서 'message'의 'content' 추출
        OpenApiResponseDto openAiResponse = response.getBody();
        if (openAiResponse != null && openAiResponse.getChoices() != null && !openAiResponse.getChoices().isEmpty()) {
            OpenApiResponseDto.Message message = openAiResponse.getChoices().get(0).getMessage();
            return message != null ? message.getContent().trim() : null; // 공백 제거 및 반환
        } else
            return null;
    }
}
