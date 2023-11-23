package com.likelion.letterBox.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LetterRequestDto {
    private String content;
    private String writer;
    List<String> AnswerList;
}
