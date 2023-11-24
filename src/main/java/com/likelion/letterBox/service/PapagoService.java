package com.likelion.letterBox.service;

import com.likelion.letterBox.dto.OpenApiResponseDto;
import com.likelion.letterBox.dto.PapagoResponseDto;
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
public class PapagoService {

    private final RestTemplate restTemplate;
    @Value("${papago.client.id}")
    private String clientId;

    @Value("${papago.client.secret}")
    private String clientSecret;

    public String translateToEnglish(String text) {
        String url = "https://openapi.naver.com/v1/papago/n2mt"; // OpenAI 번역 API 엔드포인트

        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Naver-Client-Id", clientId);
        headers.set("X-Naver-Client-Secret", clientSecret);

        // 요청 본문 구성
        Map<String, Object> requestJson = new HashMap<>();
        requestJson.put("source", "ko");
        requestJson.put("target", "en");
        requestJson.put("text", text);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<PapagoResponseDto> response = restTemplate.postForEntity(url, entity, PapagoResponseDto.class);

        // 응답에서 'message'의 'content' 추출
        return response.getBody().getMessage().getResult().getTranslatedText();
    }
}
