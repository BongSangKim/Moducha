package com.example.restea.oauth2.jwt;

import static com.example.restea.oauth2.enums.JWTFilterMessage.EXPIRED_ACCESS_TOKEN;
import static com.example.restea.oauth2.enums.JWTFilterMessage.NO_ACCESS_TOKEN;
import static com.example.restea.oauth2.enums.TokenType.ACCESS;

import com.example.restea.oauth2.dto.CustomOAuth2User;
import com.example.restea.oauth2.dto.OAuth2JwtMemberDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private static final Pattern LOGIN = Pattern.compile("^\\/login(?:\\/.*)?$");
    private static final Pattern OAUTH2 = Pattern.compile("^\\/oauth2(?:\\/.*)?$");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isExemptedUri(request, response, filterChain)) { // URI 확인
            return;
        }

        String accessToken = request.getHeader(ACCESS.getType());
        if (accessToken == null) { // Access Token 존재 확인
            filterChain.doFilter(request, response);
            return;
        }

        if (isTokenInvalid(accessToken, response)) { // 만료되었는지 && 올바른 토큰인지 확인
            return;
        }

        setUpAuthentication(accessToken);
        filterChain.doFilter(request, response);
    }

    private boolean isExemptedUri(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        String requestUri = request.getRequestURI();

        if (LOGIN.matcher(requestUri).matches() || OAUTH2.matcher(requestUri).matches()) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private boolean isTokenInvalid(String token, HttpServletResponse response) throws IOException {
        return isTokenExpired(token, response) || isNotAccessToken(token, response);
    }

    private boolean isTokenExpired(String token, HttpServletResponse response) throws IOException {
        try {
            jwtUtil.isExpired(token);
            // 정상적이라면 Exception이 발생하지 않음
            return false;
        } catch (ExpiredJwtException e) {
            // 만료된 Jwt 토큰이라면
            setResponse(response, EXPIRED_ACCESS_TOKEN.toJson());
            return true;
        }
    }

    private boolean isNotAccessToken(String token, HttpServletResponse response) throws IOException {
        if (jwtUtil.getCategory(token).equals(ACCESS.getType())) {
            return false;
        }
        // AccessToken이 아닐 경우
        setResponse(response, NO_ACCESS_TOKEN.toJson());
        return true;
    }

    private void setResponse(HttpServletResponse response, String message) throws IOException {
        // HttpStatus 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 헤더 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // ResponseBody 설정
        PrintWriter writer = response.getWriter();
        writer.print(message);
        writer.flush();
    }

    private void setUpAuthentication(String token) {
        String nickname = jwtUtil.getNickname(token);
        Integer userId = jwtUtil.getUserId(token);
        String role = jwtUtil.getRole(token);

        OAuth2JwtMemberDTO oAuth2JwtMemberDTO = OAuth2JwtMemberDTO.builder()
                .nickname(nickname)
                .userId(userId)
                .role(role)
                .build();

        //CustomOAuth2User에 유저 정보 담기
        CustomOAuth2User customOAuth2User = new CustomOAuth2User(oAuth2JwtMemberDTO);
        // 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null,
                customOAuth2User.getAuthorities());
        // SecurityContextHolder에 일시적인 세션 생성
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}