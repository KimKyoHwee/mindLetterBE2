package com.likelion.letterBox.controller;

import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.PostBoxRequestDto;
import com.likelion.letterBox.dto.PostBoxResponseMemoList;
import com.likelion.letterBox.dto.PostBoxReturnDto;
import com.likelion.letterBox.service.PostBoxService;
import com.likelion.letterBox.service.UserService;
import com.likelion.letterBox.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/postbox")
@RequiredArgsConstructor
@Tag(name="postbox", description = "우체통 관련 api")
public class PostBoxController {

    private final PostBoxService postBoxService;
    private final UserService userService;

    //닉네임 , 기본 디자인으로 우체통 생성

    @Operation(summary="우체통 만들기")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping
    public ResponseEntity<?> createPostBox(Authentication authentication,
                                           @RequestBody PostBoxRequestDto postBoxRequestDto){
        String email= authentication.getName();
        User user=userService.returnUser(email);
        postBoxService.createPostBox(user, postBoxRequestDto);
        return ResponseEntity.ok().build();
    }


    @Operation(summary="내 우체통 정보 보기")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping
    public ResponseEntity<PostBoxReturnDto> getPostBox(Authentication authentication){
        String email= authentication.getName();
        User user=userService.returnUser(email);
        return ResponseEntity.ok().body(postBoxService.returnPostBoxDto(user));
    }


    @Operation(summary="UUID활용한 공개 우체통 공유")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/open/{UUID}")
    public ResponseEntity<PostBoxReturnDto> getOpenPostBox(@PathVariable("UUID") String uuid){
        return ResponseEntity.ok().body(PostBoxReturnDto.from(postBoxService.returnPostBox(uuid)));
    }


    @Operation(summary="내 우체통에 담긴 편지 리스트 보기")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/letters/{UUID}")
    public ResponseEntity<List<PostBoxResponseMemoList>> getMemoList(Authentication authentication,
                                                                     @PathVariable("UUID") String uuid){
        return ResponseEntity.ok().body(postBoxService.getMemoList(uuid));
    }
}
