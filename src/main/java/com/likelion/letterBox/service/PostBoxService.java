package com.likelion.letterBox.service;

import com.likelion.letterBox.DataNotFoundException;
import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.PostBoxRequestDto;
import com.likelion.letterBox.dto.PostBoxReturnDto;
import com.likelion.letterBox.repository.PostBoxRepository;
import com.likelion.letterBox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PostBoxService {
    private final PostBoxRepository postBoxRepository;
    private final UserRepository userRepository;

    public void createPostBox(User user, PostBoxRequestDto postBoxRequestDto){ //우체통 만들기
        PostBox postBox=PostBox.from(postBoxRequestDto, user);
        postBox.setUser(user);
        String uuid = UUID.randomUUID().toString();
        postBox.setUuid(uuid);
        postBox.setColor(0);
        postBoxRepository.save(postBox);
    }

    public PostBoxReturnDto returnPostBoxDto(User user){
        Optional<PostBox> optPostBox=postBoxRepository.findByUser(user);
        if(optPostBox.isEmpty()) throw new DataNotFoundException("우체통이 존재하지 않습니다.");
        else{
            PostBox postBox=optPostBox.get();
            return PostBoxReturnDto.from(postBox);
        }
    }

    public PostBox returnPostBox(String uuid){
        Optional<PostBox> optPostBox=postBoxRepository.findByUuid(uuid);
        if(optPostBox.isEmpty()) throw new DataNotFoundException("우체통이 존재하지 않습니다.");
        else return optPostBox.get();
    }

}
