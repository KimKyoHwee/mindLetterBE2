package com.likelion.letterBox.dto;

import com.likelion.letterBox.domain.Letter;
import com.likelion.letterBox.domain.PostBox;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostBoxReturnDto {
    private String nickName;
    private String shape;
    private String color;
    private String ornaments;
    private int count;
    private String uuid;
    //private List<Letter> letterList;

    public static PostBoxReturnDto from(PostBox postBox){
        return PostBoxReturnDto.builder()
                .nickName(postBox.getNickName())
                .shape(postBox.getShape())
                .color(postBox.getColor())
                .ornaments(postBox.getOrnaments())
                .count(postBox.getCount())
                .uuid(postBox.getUuid())
                //.letterList(postBox.getLetterList())
                .build();
    }
}
