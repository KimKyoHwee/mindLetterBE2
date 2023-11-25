package com.likelion.letterBox.service;


import com.likelion.letterBox.domain.Letter;
import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.LetterRequestDto;
import com.likelion.letterBox.dto.LetterResponseDto;
import com.likelion.letterBox.dto.PostBoxRequestDto;
import com.likelion.letterBox.repository.LetterRepository;
import com.likelion.letterBox.repository.PostBoxRepository;
import com.likelion.letterBox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LetterService {

    private final LetterRepository letterRepository;
    private final PostBoxRepository postBoxRepository;

    //TODO: 엽서 만들때 이미지는 칼로API써야됨됨
   public void createLetter(PostBox postBox, LetterRequestDto letterRequestDto, String ImageUrl){
       //엽서 만들면서 대상count++
        Letter letter=Letter.from(letterRequestDto, postBox, ImageUrl);
        letterRepository.save(letter);
        //엽서 받는사람 카운트 1추가
       postBox.setCount(postBox.getCount()+1);
    }

    public LetterResponseDto getLetterDetail(Long letterId){
       Optional<Letter> optLetter=letterRepository.findById(letterId);
       Letter letter=optLetter.get();
       return LetterResponseDto.from(letter);
    }
}
