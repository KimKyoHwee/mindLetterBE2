package com.likelion.letterBox.controller;

import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.KarloResponseDto;
import com.likelion.letterBox.dto.LetterRequestDto;
import com.likelion.letterBox.dto.LetterResponseDto;
import com.likelion.letterBox.service.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

    private final UserService userService;
    private final LetterService letterService;
    private final PostBoxService postBoxService;
    private final KarloService karloService;

    private final OpenAiService openAiService;

    private final PapagoService papagoService;

    private final S3Service s3Service;

    @ApiOperation(value = "엽서 만들기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "실패")
    })
    //엽서만들기 TODO:답변 리스트 받아서 칼로API연동
    @PostMapping("/{UUID}")
    public ResponseEntity<?> createLetter(@RequestBody LetterRequestDto letterRequestDto,
                                          @PathVariable("UUID") String uuid,
                                          @RequestPart MultipartFile image) throws IOException {
        PostBox postBox=postBoxService.returnPostBox(uuid);


        //이미지 S3저장
        String url=s3Service.saveFile("karlo",postBox.getUserId(),image);
        /*여기부터 이미지 링크 생성
        KarloResponseDto response = karloService.createImage(
                papagoService.translateToEnglish(letterRequestDto.getAnswer()));
        String url=response.getImages().get(0).getImage();
         */
        letterService.createLetter(postBox, letterRequestDto, url);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "칼로 api로 이미지 링크 받아오기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "실패")
    })
    //테스트용
    @PostMapping("/image")
    public ResponseEntity<String> createImage(@RequestBody String promptKOR) {
        String prompt= papagoService.translateToEnglish(promptKOR);
        KarloResponseDto response = karloService.createImage(prompt);
        String url=response.getImages().get(0).getImage();
        return ResponseEntity.ok(url);
    }

    @ApiOperation(value = "openai 번역 테스트")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "실패")
    })
    //테스트용
    @PostMapping("/openAi")
    public ResponseEntity<String> requestTranslate(@RequestBody String prompt) {
        return ResponseEntity.ok().body(openAiService.translateToEnglish(prompt));
    }


    //칼로api를 통해 10분간 유지되는 url을 영구적인 url로 수정하기
    public String downloadAndSaveImage(String imageUrl, String destinationPath) throws Exception {
        URL url = new URL(imageUrl);
        try (InputStream in = url.openStream()) {
            Path targetPath = Path.of(destinationPath);
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return targetPath.toString();
        }
    }

    @ApiOperation(value = "파파고 번역 테스트")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "실패")
    })
    //테스트용
    @PostMapping("/papgo")
    public ResponseEntity<String> requestTranslatePapago(@RequestBody String prompt) {
        return ResponseEntity.ok().body(papagoService.translateToEnglish(prompt));
    }

    @ApiOperation(value = "엽서 id로 엽서 열어보기")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "실패")
    })
    @GetMapping("/{letterId}")
    public ResponseEntity<LetterResponseDto> createLetter(@PathVariable Long letterId){
        return ResponseEntity.ok().body(letterService.getLetterDetail(letterId));
    }
}
