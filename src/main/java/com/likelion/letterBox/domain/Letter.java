package com.likelion.letterBox.domain;

import com.likelion.letterBox.dto.LetterRequestDto;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Letter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="letter_id")
    private Long id;

    @ManyToOne(targetEntity = PostBox.class, fetch = FetchType.LAZY)
    @JoinColumn(name="postBox_id")
    private PostBox postBox;

    @Column
    private String image;

    @Column
    private String content;

    @Column
    private String writer;

    /*
    @ElementCollection  //컬렉션을 칼럼으로 매핑해주는 어노테이션 (별도의 테이블 생성)
    @CollectionTable(name = "questions", joinColumns = @JoinColumn(name = "photoBox_id")) //임시로 생성할 테이블 설정
    @Column(name = "questionList")
    private List<String> questionList;

     */

    /*
    @ElementCollection  //컬렉션을 칼럼으로 매핑해주는 어노테이션 (별도의 테이블 생성)
    @CollectionTable(name = "answers", joinColumns = @JoinColumn(name = "letter_id")) //임시로 생성할 테이블 설정
    @Column(name = "answerList")
    private List<String> answerList;
    */
    //TODO: 1on1으로 USER랑 엮어야되는데, UUID로 찾아서 엮기, 이건 편지 받는 대상 + 대상에 count한개 늘려주기
    public static Letter from(LetterRequestDto letterRequestDto, PostBox requestPostBox,
                              String imageUrl){
        return Letter.builder()
                .content(letterRequestDto.getContent())
                .writer(letterRequestDto.getWriter())
                .postBox(requestPostBox)
                //.answerList(letterRequestDto.getAnswerList())
                .image(imageUrl)
                .build();
    }
}
