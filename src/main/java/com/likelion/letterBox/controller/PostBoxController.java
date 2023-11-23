package com.likelion.letterBox.controller;

import com.likelion.letterBox.dto.PostBoxRequestDto;
import com.likelion.letterBox.dto.PostBoxReturnDto;
import com.likelion.letterBox.service.PostBoxService;
import com.likelion.letterBox.utils.JwtUtil;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/postbox")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PostBoxController {

    @Value("${jwt.secret}")
    private String secretKey;
    private final PostBoxService postBoxService;

    //닉네임 , 기본 디자인으로 우체통 생성
    @PostMapping(value = "/basic")
    public ResponseEntity<PostBoxReturnDto> createPostBox(@RequestBody String nickname,
                                           @RequestHeader("Authorization") String token){
        String email = JwtUtil.getEmail(token, secretKey);
        return ResponseEntity.ok(postBoxService.saveDefault(email,nickname));
    }
    // 디자인 변경
    @PatchMapping(value="/update")
    public ResponseEntity<PostBoxReturnDto> updatePostBox(@RequestBody PostBoxRequestDto postBoxRequestDto,
                                                          @RequestHeader("Authorization")String token){
        String email = JwtUtil.getEmail(token, secretKey);
        return ResponseEntity.ok(postBoxService.updateDesign(email,postBoxRequestDto));
    }
    @GetMapping(value="/")
    public ResponseEntity<PostBoxReturnDto> getPostBox(@RequestHeader("Authorization")String token){
        String email = JwtUtil.getEmail(token, secretKey);
        return ResponseEntity.ok(postBoxService.findByUserEmail(email));
    }

}
