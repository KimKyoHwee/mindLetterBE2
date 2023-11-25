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
public class PostBoxResponseMemoList {
    private String writer;
    private Long letterId;

    public static PostBoxResponseMemoList from(Letter letter){
        return PostBoxResponseMemoList.builder()
                .writer(letter.getWriter())
                .letterId(letter.getId())
                .build();
    }
}
