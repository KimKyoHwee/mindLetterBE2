package com.likelion.letterBox.dto;

import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import lombok.AllArgsConstructor;
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

    public PostBoxRequestDto(String nickName) {
        this.nickName = nickName;
        this.shape = 0;
        this.background = 0;
        this.color = 0;
    }

    public PostBox toEntity(User user){
        PostBox postBox = PostBox.builder()
                .nickName(nickName)
                .shape(shape)
                .color(color)
                .background(background)
                .count(0)
                .user(user)
                .build();

        return postBox;
    }
}
