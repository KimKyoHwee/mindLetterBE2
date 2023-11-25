package com.likelion.letterBox.dto;

import com.likelion.letterBox.domain.Letter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LetterResponseDto {
    String content;
    String image;

    public static LetterResponseDto from(Letter letter){
        return LetterResponseDto.builder()
                .content(letter.getContent())
                .image(letter.getImage())
                .build();
    }
}
