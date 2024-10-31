//package com.example.shortmovie.Submission.Domain;
//
//import com.example.shortmovie.Member.Domain.Member;
//import com.example.shortmovie.Movie.Domain.Video;
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Entity
//@Data
//public class Submission {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "member_id")
//    private Member member; // Member 엔티티와의 관계
//
//    private LocalDateTime submissionTime;
//
//    private Boolean agreement;
//
//    @OneToMany(mappedBy = "submission", cascade = CascadeType.ALL)
//    private List<Video> videos; // Submission에 속하는 비디오 리스트
//
//    public String getName() {
//        return member.getName();  // member에서 name을 가져오는 메서드
//    }
//}
