package com.example.restea.share.entity;

import com.example.restea.common.entity.BaseTimeEntity;
import com.example.restea.user.entity.Users;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@Table(name = "share_board")
public class ShareBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "share_board_id")
    private Integer id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false)
    private Integer maxParticipants;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer viewCount;

    @Column(nullable = false)
    @ColumnDefault("true")
    private Boolean activated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", insertable = false, updatable = false, nullable = false)
    private Users user;

    @OneToMany(mappedBy = "shareBoard") // 글 비활성화 시  댓글 비활성화
    private List<ShareComment> shareComments = new ArrayList<>();

    @OneToMany(mappedBy = "shareBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShareParticipant> shareParticipants = new ArrayList<>();
}