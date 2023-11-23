package com.likelion.letterBox.service;

import com.likelion.letterBox.DataNotFoundException;
import com.likelion.letterBox.domain.User;
import com.likelion.letterBox.dto.UserJoinDto;
import com.likelion.letterBox.repository.UserRepository;
import com.likelion.letterBox.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    @Value("${jwt.secret}")
    private String secretKey;
    private final UserRepository userRepository;

    public User returnUser(String email){
        Optional<User> optUser=userRepository.findByEmail(email);
        if(optUser.isEmpty()) throw new DataNotFoundException("유저를 찾을 수 없습니다.");
        else{
            User user=optUser.get();
            return user;
        }
    }

    private Long expiredMs=1000*60*60l;  //1시간
    public void join(UserJoinDto userJoinDto) {
        String userEmail = userJoinDto.getEmail();
        Optional<User> optUser = userRepository.findByEmail(userEmail);
        if (optUser.isPresent()) throw new DataNotFoundException("아이디가 겹칩니다.");
        else {
            User user = User.from(userJoinDto);
            userRepository.save(user);
        }
    }

    public String login(String email, String password){
        Optional<User> optUser=userRepository.findByEmailAndPassword(email, password);
        if(optUser.isPresent()) {
            return JwtUtil.createJwt(email, secretKey, expiredMs);
        }
        else throw new DataNotFoundException("일치하는 회원 정보가 없습니다.");
    }
}
