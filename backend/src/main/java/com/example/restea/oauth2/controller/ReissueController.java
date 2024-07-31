package com.example.restea.oauth2.controller;

import static com.example.restea.oauth2.enums.TokenType.ACCESS;

import com.example.restea.oauth2.dto.CustomOAuth2User;
import com.example.restea.oauth2.service.ReissueService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ReissueController {

    private final ReissueService reissueService;

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@CookieValue(value = "refresh", required = false) String refreshToken
            , @AuthenticationPrincipal CustomOAuth2User customOAuth2User, HttpServletResponse response) {
        reissueService.reissueToken(refreshToken);

        // DB에 RefreshToken이 존재하는지 확인 -> LoginFilter에서 이미 저장을 했을 것이기 때문이다.
        reissueService.checkRefresh(customOAuth2User.getUserId());

        // RefreshToken이 DB에 존재하므로 최종적으로 AccesToken 발급 절차 진행
        String newAccessToken = reissueService.createNewAccessToken(refreshToken);

        response.setHeader(ACCESS.getType(), newAccessToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}