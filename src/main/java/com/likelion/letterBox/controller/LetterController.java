package com.likelion.letterBox.controller;

import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.LetterRequestDto;
import com.likelion.letterBox.service.LetterService;
import com.likelion.letterBox.service.PostBoxService;
import com.likelion.letterBox.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

    private final UserService userService;
    private final LetterService letterService;
    private final PostBoxService postBoxService;

    @ApiOperation(value = "엽서 만들기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "실패")
    })
    //엽서만들기 TODO:답변 리스트 받아서 칼로API연동
    @PostMapping
    public ResponseEntity<?> createLetter(@RequestBody LetterRequestDto letterRequestDto,
                                          @PathVariable("UUID") String uuid){
        PostBox postBox=postBoxService.returnPostBox(uuid);
        letterService.createLetter(postBox, letterRequestDto);
        return ResponseEntity.ok().build();
    }

}
