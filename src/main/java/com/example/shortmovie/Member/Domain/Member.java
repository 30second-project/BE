package com.example.shortmovie.Member.Domain;

import com.example.shortmovie.Movie.Domain.Video;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String memberId; // Member의 ID
    private String contact;  // 연락처

    @JsonManagedReference
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Video> videos; // 업로드한 비디오 목록
}
