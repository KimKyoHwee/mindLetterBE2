package com.likelion.letterBox.dto;

import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostBoxRequestDto {
    private String nickName;
    private Integer shape;
    private Integer color;
    private Integer background;
}
