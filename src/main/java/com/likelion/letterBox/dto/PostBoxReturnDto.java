package com.likelion.letterBox.dto;

import com.likelion.letterBox.domain.PostBox;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostBoxReturnDto {
    private Long id;
    private String nickName;
    private Integer shape;
    private Integer color;
    private Integer background;
    private Integer count;

    public PostBoxReturnDto(PostBox postBox){
        this.id = postBox.getId();
        this.background = postBox.getBackground();
        this.color = postBox.getColor();
        this.shape = postBox.getShape();
        this.count = postBox.getCount();
    }
}
