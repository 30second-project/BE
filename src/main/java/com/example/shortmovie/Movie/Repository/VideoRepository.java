package com.example.shortmovie.Movie.Repository;

import com.example.shortmovie.Movie.Domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    // Member 이름으로 비디오 검색
    @Query("SELECT v FROM Video v WHERE v.member.userName LIKE %?1%")
    List<Video> findByMemberNameContaining(String name);

    // Member ID로 비디오 검색
    @Query("SELECT v FROM Video v WHERE v.member.memberId LIKE %?1%")
    List<Video> findByMemberIdContaining(String memberId);

    // 모든 비디오 정보 포함 쿼리
    @Query("SELECT v FROM Video v LEFT JOIN FETCH v.member")
    List<Video> findAllWithMembers();
    @Query("SELECT v FROM Video v WHERE v.member.memberId = ?1")
    List<Video> findByMemberId(String memberId); // memberId로 영상을 조회하는 메서드

    List<Video> findByTitleContaining(String title); // Search by title
    @Query("SELECT v FROM Video v WHERE v.member.userName LIKE %?1%")
    List<Video> findByUserNameContaining(String userName);
}
