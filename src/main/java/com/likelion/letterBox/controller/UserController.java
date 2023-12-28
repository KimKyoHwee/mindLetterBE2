package com.likelion.letterBox.controller;

import com.likelion.letterBox.dto.UserJoinDto;
import com.likelion.letterBox.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name="user", description = "유저 관련 api")
public class UserController {

    private final UserService userService;


    @Operation(summary="회원가입 api")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody UserJoinDto userJoinDto){
        userService.join(userJoinDto);
        return ResponseEntity.ok().build();
    }


    @Operation(summary="로그인 api")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserJoinDto userDto){
        //토큰만 발급됨(로그인 성공시)
        String email= userDto.getEmail();
        String password=userDto.getPassword();
        return ResponseEntity.ok().body(userService.login(email, password));
    }


    @Operation(summary="아이디 중복확인 api")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/email")
    public ResponseEntity<String> checkId(@RequestBody String email){
        //토큰만 발급됨(로그인 성공시)
        return ResponseEntity.ok().body(userService.checkEmail(email));
    }
}
