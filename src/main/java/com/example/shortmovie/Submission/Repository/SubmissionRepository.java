//package com.example.shortmovie.Submission.Repository;
//
//import com.example.shortmovie.Submission.Domain.Submission;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface SubmissionRepository extends JpaRepository<Submission, Long> {
//
//    @Query("SELECT s FROM Submission s WHERE s.member.name LIKE %?1%")
//    List<Submission> findByMemberNameContaining(String name);
//
//    @Query("SELECT s FROM Submission s WHERE s.member.memberId LIKE %?1%")
//    List<Submission> findByMemberIdContaining(String memberId);
//
//    // 비디오 정보 포함 쿼리
//    @Query("SELECT s FROM Submission s LEFT JOIN FETCH s.videos")
//    List<Submission> findAllWithVideos();
//}
