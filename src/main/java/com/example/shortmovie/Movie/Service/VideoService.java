package com.example.shortmovie.Movie.Service;

import com.example.shortmovie.Member.Domain.Member;
import com.example.shortmovie.Member.Repository.MemberRepository;
import com.example.shortmovie.Movie.Domain.Video;
import com.example.shortmovie.Movie.Domain.VideoDTO;
import com.example.shortmovie.Movie.Repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public VideoService(VideoRepository videoRepository, MemberRepository memberRepository) {
        this.videoRepository = videoRepository;
        this.memberRepository = memberRepository;
    }

    // VideoService 클래스에서 모든 비디오 제출을 조회할 때
    public List<VideoDTO> getAllSubmissions() {
        List<Video> videos = videoRepository.findAll();
        videos.forEach(video -> {
            System.out.println("Video ID: " + video.getId() + ", Title: " + video.getTitle());
        });
        return videos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<VideoDTO> getFilteredSubmissions(String keyword, String category) {
        List<Video> videos;

        switch (category) {
            case "title":
                videos = videoRepository.findByTitleContaining(keyword);
                break;
            case "memberId":
                videos = videoRepository.findByMemberIdContaining(keyword);
                break;
            case "userName": // 회원 이름에 대한 검색 로직 추가
                videos = videoRepository.findByMemberNameContaining(keyword);
                break;
            default:
                videos = videoRepository.findAll();
                break;
        }

        return videos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // 비디오를 DTO로 변환하는 메서드
    private VideoDTO convertToDTO(Video video) {
        VideoDTO videoDTO = new VideoDTO();
        videoDTO.setId(video.getId());
        videoDTO.setVideoUrl(video.getVideoUrl());
        videoDTO.setTitle(video.getTitle());
        videoDTO.setDescription(video.getDescription());
        videoDTO.setSubmissionTime(video.getSubmissionTime());
        videoDTO.setAgreement(video.getAgreement());
        videoDTO.setAdditionalInfo(video.getAdditionalInfo());
        videoDTO.setDirector(video.getDirector());
        videoDTO.setActors(video.getActors());
        videoDTO.setThumbnailUrl(video.getThumbnailUrl());

        // 여기서 member의 이름과 연락처도 추가로 설정
        Member member = video.getMember();
        if (member != null) {
            videoDTO.setMemberId(member.getMemberId());  // 회원 이름 설정
            videoDTO.setUserName(member.getUserName());  // 회원 이름 설정
            videoDTO.setContact(member.getContact());  // 연락처 설정
        }
        return videoDTO;
    }

//    public List<Video> getVideosByMemberId(String memberId) {
//        return videoRepository.findByMemberId(memberId);
//    }
        public List<Video> getVideosByMemberId(String memberId) {
            return videoRepository.findByMemberId(memberId); // 회원 ID로 영상 목록 반환
        }
    // 제출 검색
    public List<VideoDTO> searchSubmissions(String keyword, String category) {
        switch (category) {
            case "name":
                return videoRepository.findByMemberNameContaining(keyword).stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            case "memberId":
                return videoRepository.findByMemberIdContaining(keyword).stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            default:
                return getAllSubmissions(); // 모든 비디오 제출 반환
        }
    }



    public VideoDTO uploadVideo(String memberId, String userName,String contact, VideoDTO videoDTO) {
        Video video = new Video();
        video.setVideoUrl(videoDTO.getVideoUrl());
        video.setTitle(videoDTO.getTitle());
        video.setDescription(videoDTO.getDescription());
        video.setDirector(videoDTO.getDirector());
        video.setActors(videoDTO.getActors());
        video.setThumbnailUrl(videoDTO.getThumbnailUrl());
        video.setAdditionalInfo(videoDTO.getAdditionalInfo());
        video.setSubmissionTime(LocalDateTime.now()); // 제출 시간 설정
        video.setAgreement(videoDTO.getAgreement());

        Member member = memberRepository.findByMemberId(memberId).orElse(null);
        if (member != null) {
            video.setMember(member); // 비디오에 회원 정보 설정
            video = videoRepository.save(video); // 비디오 저장
            return convertToDTO(video); // DTO로 변환하여 반환
        } else {
            throw new IllegalArgumentException("회원이 존재하지 않습니다.");
        }
    }
// 비디오 업로드
//    public VideoDTO uploadVideo(String memberId, String userName, VideoDTO videoDTO) {
//        Video video = new Video();
//        video.setVideoUrl(videoDTO.getVideoUrl());
//        video.setTitle(videoDTO.getTitle());
//        video.setDescription(videoDTO.getDescription());
//        video.setStaff(videoDTO.getStaff());
//        video.setDirector(videoDTO.getDirector());
//        video.setActors(videoDTO.getActors());
//        video.setThumbnailUrl(videoDTO.getThumbnailUrl());
//        video.setSubmissionTime(LocalDateTime.now()); // 제출 시간 설정
//        video.setAgreement(videoDTO.getAgreement());
//
//        // 멤버 ID가 있는 경우만 멤버 정보를 설정
//        if (videoDTO.getMemberId() != null) {
//// memberId와 userName을 이용하여 멤버 정보를 설정하는 방식
//            Member member = memberRepository.findByMemberId(memberId).orElse(null);
//            if (member != null) {
//                video.setMember(member); // 비디오에 회원 정보 설정
//            } else {
//                throw new IllegalArgumentException("회원이 존재하지 않습니다.");
//            }
//        }
//
//        video = videoRepository.save(video); // 비디오 저장
//
//        // DTO로 변환하여 반환
//        VideoDTO resultDTO = convertToDTO(video);
//        if (videoDTO.getMemberId() != null) {
//            resultDTO.getName(); // 회원 이름 설정
//        }
//        return resultDTO;
//    }


}
