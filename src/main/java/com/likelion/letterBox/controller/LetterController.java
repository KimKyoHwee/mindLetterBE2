package com.likelion.letterBox.controller;

import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.KarloResponseDto;
import com.likelion.letterBox.dto.LetterRequestDto;
import com.likelion.letterBox.dto.LetterResponseDto;
import com.likelion.letterBox.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/letter")
@RequiredArgsConstructor
@Tag(name="LETTER", description = "편지 관련 api")
public class LetterController {

    private final UserService userService;
    private final LetterService letterService;
    private final PostBoxService postBoxService;
    private final KarloService karloService;

    private final OpenAiService openAiService;

    private final PapagoService papagoService;

    private final S3Service s3Service;


    @Operation(summary="엽서 만들기")
    @ApiResponse(responseCode = "200", description = "성공")
    //엽서만들기 TODO:답변 리스트 받아서 칼로API연동
    @PostMapping("/open/{UUID}")
    public ResponseEntity<?> createLetter(@RequestBody LetterRequestDto letterRequestDto,
                                          @PathVariable("UUID") String uuid) throws IOException {
        PostBox postBox=postBoxService.returnPostBox(uuid);
        letterService.createLetter(postBox, letterRequestDto);
        return ResponseEntity.ok().build();
    }


    @Operation(summary="칼로 api로 이미지 링크 받아오기")
    @ApiResponse(responseCode = "200", description = "성공")
    //테스트용
    @PostMapping("/image/{UUID}")
    public ResponseEntity<String> createImage(@RequestBody String promptKOR,
                                              @PathVariable("UUID") String uuid) {
        PostBox postBox=postBoxService.returnPostBox(uuid);
        String prompt= papagoService.translateToEnglish(promptKOR);
        KarloResponseDto response = karloService.createImage(prompt);
        String base64Image=response.getImages().get(0).getImage();  //base64 문자열
        String url=s3Service.saveBase64Image("karlo",postBox.getUserId(),base64Image);
        return ResponseEntity.ok(url);
    }


    @Operation(summary="openai 번역 테스트")
    @ApiResponse(responseCode = "200", description = "성공")
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


    @Operation(summary="파파고 번역 테스트")
    @ApiResponse(responseCode = "200", description = "성공")
    //테스트용
    @PostMapping("/papgo")
    public ResponseEntity<String> requestTranslatePapago(@RequestBody String prompt) {
        return ResponseEntity.ok().body(papagoService.translateToEnglish(prompt));
    }


    @Operation(summary="엽서 id로 엽서 열어보기")
    @ApiResponse(responseCode = "200", description = "성공")
    @GetMapping("/{letterId}")
    public ResponseEntity<LetterResponseDto> createLetter(@PathVariable Long letterId){
        return ResponseEntity.ok().body(letterService.getLetterDetail(letterId));
    }
}
