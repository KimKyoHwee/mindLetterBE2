package com.likelion.letterBox.service;

import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLinkService {
    private final UserRepository userRepository;

    public String createUserLink(String email){
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> {
                throw new IllegalArgumentException("유저 정보를 불러올 수 없습니다");
            });
            user.setOwnerId(UUID.randomUUID());
            userRepository.save(user);

            // url 임시 설정 -> 링크를 string형식으로 반환하는게 맞는지 모르겠습니닷 ㅜ
            // 아니면 uuid 자체만 반환하는게 맞을까요?
            return "/api/v1/postbox/post?uuid=" + user.getOwnerId();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
