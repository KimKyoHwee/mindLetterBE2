package com.likelion.letterBox.domain;

import com.likelion.letterBox.dto.PostBoxRequestDto;
import io.swagger.models.auth.In;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostBox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="postBox_id")
    private Long id;

    @Column
    private String nickName;

    @Column
    private int shape;

    @Column
    private int color;

    @Column
    private int background;

    @Column
    private int count;  //초기화 세팅필요

    @Column
    private String uuid; // UUID 우체통, 회원 보류

    @OneToMany(mappedBy = "postBox")
    private final List<Letter> letterList = new ArrayList<>();

    @OneToOne(mappedBy = "postBox")
    @JoinColumn(name = "user_id")
    private User user;

    public static PostBox from(PostBoxRequestDto postBoxRequestDto){
        return PostBox.builder()
                .nickName(postBoxRequestDto.getNickName())
                .shape(postBoxRequestDto.getShape())
                .color(postBoxRequestDto.getColor())
                .background(postBoxRequestDto.getBackground())
                .build();
    }
}
