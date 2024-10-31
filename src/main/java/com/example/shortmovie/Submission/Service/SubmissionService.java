//package com.example.shortmovie.Submission.Service;
//
//import com.example.shortmovie.Member.Domain.Member;
//import com.example.shortmovie.Member.Repository.MemberRepository;
//import com.example.shortmovie.Movie.Domain.Video;
//import com.example.shortmovie.Submission.Domain.Submission;
//import com.example.shortmovie.Submission.Repository.SubmissionRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class SubmissionService {
//
//    @Autowired
//    private SubmissionRepository submissionRepository;
//
//    @Autowired
//    private MemberRepository memberRepository; // UserRepository를 MemberRepository로 수정
//
//    public List<Submission> getAllSubmissions() {
//        return submissionRepository.findAll();
//    }
//
//    public List<Submission> getAllSubmissionsWithVideos() {
//        List<Submission> submissions = submissionRepository.findAllWithVideos(); // 비디오 정보 포함 쿼리
//        for (Submission submission : submissions) {
//            Member member = memberRepository.findById(submission.getMember().getId()).orElse(null);
//            submission.setMember(member); // Member 정보를 설정
//        }
//        return submissions;
//    }
//
//    public List<Submission> getAllSubmissionsWithMembers() {
//        List<Submission> submissions = submissionRepository.findAll();
//        for (Submission submission : submissions) {
//            Member member = memberRepository.findById(submission.getMember().getId()).orElse(null);
//            submission.setMember(member); // Submission 엔티티에 Member 추가
//        }
//        return submissions;
//    }
//
//    public List<Submission> searchSubmissions(String keyword, String category) {
//        switch (category) {
//            case "name":
//                return submissionRepository.findByMemberNameContaining(keyword); // Member의 이름을 기준으로 검색
//            case "memberId":
//                return submissionRepository.findByMemberIdContaining(keyword); // Member ID를 기준으로 검색
//            default:
//                return submissionRepository.findAll();
//        }
//    }
//
//    public Submission createSubmission(Submission submission) {
//        // 비디오와 제출을 연결
//        for (Video video : submission.getVideos()) {
//            video.setSubmission(submission);
//        }
//        return submissionRepository.save(submission);
//    }
//
//    public void deleteSubmission(Long id) {
//        submissionRepository.deleteById(id);
//    }
//}
