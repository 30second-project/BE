package com.example.shortmovie.Movie.Domain;

import com.example.shortmovie.Member.Domain.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Data
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 비디오의 고유 ID

    private String videoUrl; // 비디오 파일의 URL

    private String title; // 비디오의 제목

    private String description; // 비디오에 대한 설명

    private String director; // 비디오의 감독 이름

    private String actors; // 비디오에 출연하는 배우들

    private String additionalInfo; // 비디오의 제작진 정보 (예: 제작자, 프로듀서 등)

    private String thumbnailUrl; // 비디오의 썸네일 이미지 URL

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "member_id", nullable = false) // 비디오를 업로드한 회원의 ID (null이 허용되지 않음)
    private Member member; // Member 엔티티와의 관계

    private LocalDateTime submissionTime; // 비디오 제출 시간

    private Boolean agreement; // 개인정보 동의 여부 (true: 동의, false: 비동의)

    // Member에서 name을 가져오는 메서드
    public String getName() {
        return member != null ? member.getUserName() : null; // 회원의 이름 반환
    }
}
