package com.likelion.letterBox.service;

import com.likelion.letterBox.dto.KarloResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class KarloService {

    private final RestTemplate restTemplate;

    @Value("${kakao.secret}")
    private String apiKey; // 카카오 API 키

    /**
     * 단일 제시어를 사용하여 이미지를 생성합니다.
     */
    public KarloResponseDto createImage(String prompt) {
        String url = "https://api.kakaobrain.com/v2/inference/karlo/t2i";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("prompt", prompt);
        requestBody.put("return_type", "base64_string");
        requestBody.put("image_format", "jpeg");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<KarloResponseDto> response = restTemplate.postForEntity(url, entity, KarloResponseDto.class);

        return response.getBody();
    }
}