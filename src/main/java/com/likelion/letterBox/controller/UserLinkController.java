package com.likelion.letterBox.controller;

import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.service.UserLinkService;
import com.likelion.letterBox.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/link")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserLinkController {
    @Value("${jwt.secret}")
    private String secretKey;
    private final UserLinkService userLinkService;
    //UUID 생성
    @PostMapping("/create")
    public ResponseEntity<String> createUserLink(@RequestHeader("Authorization") String token){
        String email = JwtUtil.getEmail(token, secretKey);
        return ResponseEntity.ok(userLinkService.createUserLink(email));
    }
}
