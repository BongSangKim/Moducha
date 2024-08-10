package com.example.restea.teatime.controller;

import com.example.restea.common.dto.ResponseDTO;
import com.example.restea.oauth2.dto.CustomOAuth2User;
import com.example.restea.teatime.dto.TeatimeJoinRequest;
import com.example.restea.teatime.dto.TeatimeJoinResponse;
import com.example.restea.teatime.service.TeatimeParticipantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/teatimes/{teatimeBoardId}/participants")
public class TeatimeParticipantController {

    private final TeatimeParticipantService teatimeParticipantService;

    /**
     * 주어진 티타임 게시글에 참가 신청
     *
     * @param teatimeBoardId   티타임 게시글 ID.
     * @param request          name, phone, address.
     * @param customOAuth2User 현재 인증된 사용자.
     * @return 작성한 참가 정보를 포함하는 ResponseEntity 객체를 반환합니다. 참가 신청에 실패하면 에러 코드를 담은 ResponseEntity를 반환합니다.
     */
    @PostMapping
    public ResponseEntity<?> addParticipant(@PathVariable("teatimeBoardId") Integer teatimeBoardId,
                                            @Valid @RequestBody TeatimeJoinRequest request,
                                            @AuthenticationPrincipal CustomOAuth2User customOAuth2User) {

        TeatimeJoinResponse result = teatimeParticipantService.addParticipant(teatimeBoardId, request,
                customOAuth2User.getUserId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseDTO.from(result));
    }
}