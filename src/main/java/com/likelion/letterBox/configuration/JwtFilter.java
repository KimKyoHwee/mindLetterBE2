package com.likelion.letterBox.configuration;

import com.likelion.letterBox.service.UserService;
import com.likelion.letterBox.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final String secretKey;

    @Override  //권한부여ghr
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        // Swagger 관련 경로에 대한 요청 처리 스킵
        if (path.contains("/swagger-ui/**") || path.contains("/v3/api-docs/**") || path.contains("/swagger-resources/**")
        ||path.contains("/api/v1/user/**")||path.contains("/api/v1/letter/**") || path.contains("/api/v1/postbox/open/**")
        ||path.contains("/api/v1/postbox/letters/**")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authorization=request.getHeader(HttpHeaders.AUTHORIZATION); //헤더에서 꺼내기
        log.info("authorization : {}", authorization);//토큰에서 userName꺼내기

        //토큰 안보내면 블락걸기(토큰이 입력되었는지)
        if(authorization==null || !authorization.startsWith("Bearer ")){
            log.error("authorization을 잘못보냈습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        //토큰꺼내기
        String token=authorization.split(" ")[1];  //authorization에서 첫공백 이전까지가 토큰

        //토큰 Expired 여부
        if(JwtUtil.isExpired(token, secretKey)){
            log.error("토큰이 만료되었습니다.");
            filterChain.doFilter(request, response);
            return;
        }
        String email=JwtUtil.getEmail(token, secretKey);
        log.info("email: {}", email);

        UsernamePasswordAuthenticationToken authenticationToken=
                new UsernamePasswordAuthenticationToken(email, null, List.of(new
                        SimpleGrantedAuthority("USER")));
        //디테일넣기
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
