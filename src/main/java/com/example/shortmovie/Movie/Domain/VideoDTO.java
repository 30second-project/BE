package com.example.shortmovie.Movie.Domain;

import com.example.shortmovie.Member.Domain.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VideoDTO {

    private Long id; // 비디오의 고유 ID

    private String videoUrl; // 비디오 파일의 URL

    private String title; // 비디오의 제목

    private String description; // 비디오에 대한 설명


    private String director; // 비디오의 감독 이름

    private String actors; // 비디오에 출연하는 배우들

    private String additionalInfo; // 비디오의 제작진 정보 (예: 제작자, 프로듀서 등)

    private String thumbnailUrl; // 비디오의 썸네일 이미지 URL

    private LocalDateTime submissionTime; // 비디오 제출 시간

    private Boolean agreement; // 개인정보 동의 여부 (true: 동의, false: 비동의)


    private String userName;// 비디오를 업로드한 회원의 이름
    private String memberId; // Member의 ID
    private String contact;  // 연락처


    public Boolean getAgreement() {
        return agreement != null ? agreement : false; // null일 경우 false 반환
    }

    public LocalDateTime getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(LocalDateTime submissionTime) {
        this.submissionTime = submissionTime;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
