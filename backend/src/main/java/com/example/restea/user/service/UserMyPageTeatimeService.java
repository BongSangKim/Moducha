package com.example.restea.user.service;

import com.example.restea.common.dto.PaginationDTO;
import com.example.restea.common.dto.ResponseDTO;
import com.example.restea.teatime.dto.TeatimeListResponse;
import com.example.restea.teatime.entity.TeatimeBoard;
import com.example.restea.teatime.repository.TeatimeBoardRepository;
import com.example.restea.teatime.repository.TeatimeParticipantRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserMyPageTeatimeService {

    private final TeatimeBoardRepository teatimeBoardRepository;
    private final TeatimeParticipantRepository teatimeParticipantRepository;

    /**
     * 내가 작성한 TeatimeBoardList를 최신순으로 불러오는 메소드
     *
     * @param userId  유저Id
     * @param page    요청받은 페이지 Number
     * @param perPage 페이지 컨텐츠 개수
     * @return TeatimeListResponse의 List를 Data로 가지는 ResponseDTO
     */
    @Transactional
    public ResponseDTO<List<TeatimeListResponse>> getTeatimeBoardList(Integer userId, Integer page, Integer perPage) {

        Page<TeatimeBoard> teatimeBoards = fetchActiveTeatimeBoards(userId, page, perPage); // 아직 기간이 지나지 않고 활성화된 게시글
        List<TeatimeListResponse> data = createResponseFormTeatimeBoards(teatimeBoards.getContent());
        Long count = calculateCount();

        PaginationDTO pagination = PaginationDTO.of(count.intValue(), page, perPage);

        return ResponseDTO.of(data, pagination);
    }

    /**
     * 최신순 내림차순으로 나눔 글 조회
     *
     * @param page    페이지
     * @param perPage 페이지 당 컨텐츠 개수
     * @return 컨텐츠 Page 객체
     */
    private @NotNull Page<TeatimeBoard> fetchActiveTeatimeBoards(Integer userId, Integer page, Integer perPage) {

        PageRequest pageRequest = PageRequest.of(page - 1, perPage, Sort.by("createdDate").descending());
        Page<TeatimeBoard> teatimeBoards = teatimeBoardRepository.findAllByActivatedAndUserId(true, userId,
                pageRequest);

        if (teatimeBoards.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return teatimeBoards;
    }

    /**
     * 내가 쓴 티타임 글 Page 객체 생성
     *
     * @param teatimeBoards 나눔 글
     * @return 티타임 글 Response List
     */
    private List<TeatimeListResponse> createResponseFormTeatimeBoards(List<TeatimeBoard> teatimeBoards) {
        List<TeatimeListResponse> data = new ArrayList<>();
        teatimeBoards.forEach(teatimeBoard -> {
            Integer participant = teatimeParticipantRepository.countByTeatimeBoard(teatimeBoard).intValue();
            data.add(TeatimeListResponse.of(teatimeBoard, participant));
        });
        return data;
    }

    /**
     * 총 개수 세기
     *
     * @return 총 개수
     */
    private Long calculateCount() {
        return teatimeBoardRepository.countByActivated(true);
    }
}