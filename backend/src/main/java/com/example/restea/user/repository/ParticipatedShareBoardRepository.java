package com.example.restea.user.repository;

import com.example.restea.share.entity.QShareBoard;
import com.example.restea.share.entity.QShareParticipant;
import com.example.restea.share.entity.ShareBoard;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipatedShareBoardRepository {

    private final JPAQueryFactory queryFactory;

    QShareBoard shareBoard = QShareBoard.shareBoard;
    QShareParticipant shareParticipant = QShareParticipant.shareParticipant;

    /**
     * 신청한 나눔 게시판 글을 가져오는 메소드 - 지금으로부터 가까운 마감일 순 정렬
     *
     * @param userId    userId
     * @param page      몇번째 페이지인지?
     * @param perPage   넘겨줄 데이터 개수
     * @param activated 활성화 되어있는 유저인지?
     * @return ShareBoard의 List
     */
    public List<ShareBoard> findParticipatedShareBoardsBySort(Integer userId, String sort, Integer page,
                                                              Integer perPage,
                                                              boolean activated) {
        return queryFactory
                .selectFrom(shareBoard) // shareBoard
                .join(shareParticipant)
                .on(shareBoard.id.eq(shareParticipant.shareBoard.id)) // shareParticipant의 shareBoard.id와 같은 것
                .fetchJoin() // fetchJoin 적용
                .where(createWhereCondition(userId, sort, activated))
                .orderBy(getOrderSpecifier(sort)) // sort가 ongoing일 시 오름차순, before일 시 내림차순
                .offset((long) (page - 1) * perPage)
                .limit(perPage)
                .fetch();
    }

    /**
     * 신청한 나눔 게시판 글의 전체 개수를 세는 메소드
     *
     * @param activated 활성화 되어있는 유저인지?
     * @param userId    userId
     * @return shareBoard의 count
     */
    public long countParticipatedShareBoardsBySort(Integer userId, String sort, boolean activated) {

        Long totalCount = queryFactory
                .select(shareBoard.count())
                .from(shareBoard) // shareBoard
                .join(shareParticipant)
                .on(shareBoard.id.eq(shareParticipant.shareBoard.id)) // shareParticipant의 shareBoard.id와 같은 것
                .where(createWhereCondition(userId, sort, activated))
                .fetchOne();

        if (totalCount == null) {
            return 0;
        }
        return totalCount;
    }

    /**
     * User의 정보를 확인하는 where절 메소드
     *
     * @param userId    userId
     * @param sort      정렬기준
     * @param activated 활성화 되어있는 유저인지?
     * @return boolean
     */
    private BooleanExpression createWhereCondition(Integer userId, String sort, boolean activated) {
        // shareParticipant의 user.id가 customOAuth2Uwer의 id와 같은 것 + 활성화된 shareBoard만
        BooleanExpression baseCondition = shareParticipant.user.id.eq(userId)
                .and(shareBoard.activated.eq(activated));

        if ("ongoing".equals(sort)) {
            return baseCondition.and(shareBoard.endDate.after(LocalDateTime.now()));
        }
        if ("before".equals(sort)) {
            return baseCondition.and(shareBoard.endDate.before(LocalDateTime.now()));
        }
        return baseCondition;
    }

    /**
     * ongoing일 시 오름차순, before일 시 내림차순 정렬
     *
     * @param sort 정렬 정보
     * @return OrderSpecifier(정렬 기준)
     */
    private OrderSpecifier<LocalDateTime> getOrderSpecifier(String sort) {

        if ("ongoing".equals(sort)) {
            return shareBoard.endDate.asc();
        }
        if ("before".equals(sort)) {
            return shareBoard.endDate.desc();
        }
        return shareBoard.endDate.asc();
    }

}