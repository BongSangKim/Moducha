package com.example.restea.share.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShareUpdateResponse {

    private final Integer boardId;
    private final String title;
    private final String content;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastUpdated;
    private final LocalDateTime endDate;
    private final Integer maxParticipants;
    private final Integer participants;
    private final Integer viewCount;

    @Builder
    public ShareUpdateResponse(Integer boardId, String title, String content,
                               LocalDateTime createdDate, LocalDateTime lastUpdated, LocalDateTime endDate,
                               Integer maxParticipants, Integer participants, Integer viewCount) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.lastUpdated = lastUpdated;
        this.endDate = endDate;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
        this.viewCount = viewCount;
    }
}
