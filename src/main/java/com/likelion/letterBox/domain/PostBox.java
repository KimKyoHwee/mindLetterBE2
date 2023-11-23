package com.likelion.letterBox.domain;

import io.swagger.models.auth.In;
import lombok.*;

import javax.persistence.*;
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
    @Column(name="post_id")
    private Long id;
    private String nickName;
    private Integer shape;
    private Integer color;
    private Integer background;
    private Integer count;
//    private UUID uuid; // UUID 우체통, 회원 보류
    @OneToOne(mappedBy = "postBox")
    @JoinColumn(name = "user_id")
    private User user;


}
