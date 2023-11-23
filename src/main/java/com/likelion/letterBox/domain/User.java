package com.likelion.letterBox.domain;

import com.likelion.letterBox.dto.UserJoinDto;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private UUID ownerId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="letter_id")
    private Letter letter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="postBox_id")
    private PostBox postBox;

    public static User from(UserJoinDto userJoinDto){
        return User.builder()
                .email(userJoinDto.getEmail())
                .password(userJoinDto.getPassword())
                .build();
    }
}
