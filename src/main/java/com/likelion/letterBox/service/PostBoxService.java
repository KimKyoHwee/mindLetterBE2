package com.likelion.letterBox.service;

import com.likelion.letterBox.domain.PostBox;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.PostBoxRequestDto;
import com.likelion.letterBox.dto.PostBoxReturnDto;
import com.likelion.letterBox.repository.PostBoxRepository;
import com.likelion.letterBox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostBoxService {
    private final PostBoxRepository postBoxRepository;
    private final UserRepository userRepository;

    public PostBoxReturnDto saveDefault(String email, String nickname){
        try{
            User user = userRepository.findByEmail(email).orElseThrow(()-> {
                throw new IllegalArgumentException("유저 정보를 불러올 수 없습니다");
            });
            PostBoxRequestDto requestDto = new PostBoxRequestDto(nickname);
            PostBox postbox = postBoxRepository.save(requestDto.toEntity(user));
            postBoxRepository.save(postbox);
            return new PostBoxReturnDto(postbox);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public PostBoxReturnDto updateDesign(String email, PostBoxRequestDto postBoxRequestDto){
        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> {
                throw new IllegalArgumentException("유저 정보를 불러올 수 없습니다");
            });
            PostBox postBox = user.getPostBox();
            postBox.setBackground(postBoxRequestDto.getBackground());
            postBox.setColor(postBoxRequestDto.getColor());
            postBox.setShape(postBoxRequestDto.getShape());
            postBoxRepository.save(postBox);
            return new PostBoxReturnDto(postBox);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public PostBoxReturnDto findByUserEmail(String email){
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> {
                throw new IllegalArgumentException("유저 정보를 불러올 수 없습니다");
            });
            PostBox postBox = user.getPostBox();
            return new PostBoxReturnDto(postBox);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
