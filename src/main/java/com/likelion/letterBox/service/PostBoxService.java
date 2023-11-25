package com.likelion.letterBox.service;

import com.likelion.letterBox.DataNotFoundException;
import com.likelion.letterBox.domain.Letter;
import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.PostBoxRequestDto;
import com.likelion.letterBox.dto.PostBoxResponseMemoList;
import com.likelion.letterBox.dto.PostBoxReturnDto;
import com.likelion.letterBox.repository.PostBoxRepository;
import com.likelion.letterBox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
        user.setPostBox(postBox);
        String uuid = UUID.randomUUID().toString();
        postBox.setUuid(uuid);
        postBox.setColor(0);
        postBox.setUserId(user.getId());
        postBoxRepository.save(postBox);
    }

    public PostBoxReturnDto returnPostBoxDto(User user){
        Optional<PostBox> optPostBox=postBoxRepository.findByUserId(user.getId());//TODO:유저로 찾기(user);
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

    public List<PostBoxResponseMemoList> getMemoList(String uuid){
        Optional<PostBox> optPostBox=postBoxRepository.findByUuid(uuid);
        PostBox postBox=optPostBox.get();
        List<Letter> letterList=postBox.getLetterList();

        List<PostBoxResponseMemoList> returnList=new ArrayList<>();
        for(int i=0;i< letterList.size();i++){
            returnList.add(PostBoxResponseMemoList.from(letterList.get(i)));
        }
        return returnList;
    }

}
