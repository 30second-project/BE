package com.example.shortmovie.Movie.Contorller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.example.shortmovie.Movie.Domain.Video;
import com.example.shortmovie.Movie.Domain.VideoDTO;
import com.example.shortmovie.Movie.Service.VideoService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://52.78.50.190")
public class S3Controller {

    private final AmazonS3 amazonS3Client;
    private final VideoService videoService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @GetMapping("/submission/videos/{memberId}")
    public ResponseEntity<List<Video>> getVideosByMemberId(@PathVariable String memberId) {
        List<Video> videos = videoService.getVideosByMemberId(memberId);
        return ResponseEntity.ok(videos);
    }

    // 비디오 업로드 및 제출 생성
    @PostMapping("/upload")
    public ResponseEntity<List<VideoDTO>> uploadMultipleVideosAndCreateSubmissions(
            @RequestParam("memberId") String memberId,
            @RequestParam("userName") String userName,
            @RequestParam("contact") String contact, // 연락처 추가
            @RequestPart("files") List<MultipartFile> files,
            @RequestParam("videoDTOList") String videoDTOListJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<VideoDTO> videoDTOList = objectMapper.readValue(videoDTOListJson, new TypeReference<List<VideoDTO>>() {});

            List<VideoDTO> uploadedVideos = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                VideoDTO videoDTO = videoDTOList.get(i);

                String fileName = file.getOriginalFilename();
                String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);

                // 현재 시간을 한국 시간대(KST)로 저장
                ZonedDateTime submissionTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
                System.out.println("제출 시간: " + submissionTime);

                // 비디오 업로드 및 정보 저장
                videoDTO.setVideoUrl(fileUrl);
                videoDTO.setMemberId(memberId); // 연락처 설정
                videoDTO.setUserName(userName); // 연락처 설정
                videoDTO.setContact(contact); // 연락처 설정
                VideoDTO uploadedVideo = videoService.uploadVideo(memberId, userName, contact,videoDTO);
                uploadedVideos.add(uploadedVideo);
            }

            return ResponseEntity.ok(uploadedVideos);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



//    @PostMapping
//    public ResponseEntity<List<VideoDTO>> uploadMultipleVideosAndCreateSubmissions(
//            @RequestPart(value = "memberId", required = false) String memberId,
//            @RequestPart(value = "userName", required = false) String userName,
//            @RequestPart("files") List<MultipartFile> files,
//            @RequestPart("videoDTOList") List<VideoDTO> videoDTOList) {
//        try {
//            List<VideoDTO> uploadedVideos = new ArrayList<>();
//
//            for (int i = 0; i < files.size(); i++) {
//                MultipartFile file = files.get(i);
//                VideoDTO videoDTO = videoDTOList.get(i);
//
//                // S3에 파일 업로드
//                String fileName = file.getOriginalFilename();
//                String fileUrl = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + fileName;
//                ObjectMetadata metadata = new ObjectMetadata();
//                metadata.setContentType(file.getContentType());
//                metadata.setContentLength(file.getSize());
//                amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
//
//
//                // 비디오 업로드 및 정보 저장
//                videoDTO.setVideoUrl(fileUrl); // URL 설정
//                videoDTO.setMember(memberId); // 멤버 ID 설정 (있을 경우)
//                videoDTO.setMemberName(userName); // 사용자 이름 설정 (있을 경우)
//
//                VideoDTO uploadedVideo = videoService.uploadVideo(videoDTO);
//                uploadedVideos.add(uploadedVideo); // 리스트에 추가
//            }
//
//            return ResponseEntity.ok(uploadedVideos);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    // 제출 목록 전체 조회
    @GetMapping("/submissions")
    public ResponseEntity<List<VideoDTO>> getAllSubmissions() {
        List<VideoDTO> submissions = videoService.getAllSubmissions();
        return ResponseEntity.ok(submissions);
    }

//    @GetMapping("/submission/videos/{memberId}")
//    public ResponseEntity<List<Video>> getVideosByMemberId(@PathVariable String memberId) {
//        List<Video> videos = videoService.getVideosByMemberId(memberId);
//        return ResponseEntity.ok(videos);
//    }


    // 검색 기능
    @GetMapping("/submissions/search")
    public ResponseEntity<List<VideoDTO>> searchSubmissions(
            @RequestParam String keyword, @RequestParam String category) {
        List<VideoDTO> submissions = videoService.searchSubmissions(keyword, category);
        return ResponseEntity.ok(submissions);
    }

    @GetMapping("/submissions/download")
    public void downloadExcel(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String category,
                              HttpServletResponse response) throws IOException {
        List<VideoDTO> submissions;

        // 검색 키워드와 카테고리에 따라 데이터 필터링
        if (keyword != null && !keyword.isEmpty()) {
            submissions = videoService.getFilteredSubmissions(keyword, category);
        } else {
            // 전체 데이터 가져오기
            submissions = videoService.getAllSubmissions();
        }

        // Excel 파일 생성 및 반환
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Submissions");

            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("No.");
            header.createCell(1).setCellValue("영상 제목");
            header.createCell(2).setCellValue("회원 ID");
            header.createCell(3).setCellValue("성함");
            header.createCell(4).setCellValue("연락처");
            header.createCell(5).setCellValue("제출 시간");
            header.createCell(6).setCellValue("동의 여부");
            header.createCell(7).setCellValue("비디오 URL");

            int rowIndex = 1;
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));

            for (VideoDTO submission : submissions) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(submission.getId());
                row.createCell(1).setCellValue(submission.getTitle());
                row.createCell(2).setCellValue(submission.getMemberId());
                row.createCell(3).setCellValue(submission.getUserName());
                row.createCell(4).setCellValue(submission.getContact());

                // 제출 시간을 서울 시간으로 변환
                ZonedDateTime submissionTime = submission.getSubmissionTime().atZone(ZoneId.of("Asia/Seoul"));
                row.createCell(5).setCellValue(submissionTime.toLocalDateTime());
                row.getCell(5).setCellStyle(dateCellStyle);

                // 동의 여부 처리
                row.createCell(6).setCellValue(submission.getAgreement() != null && submission.getAgreement() ? "동의함" : "동의함");

                row.createCell(7).setCellValue(submission.getVideoUrl());
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=submissions.xlsx");
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "엑셀 파일 생성 중 오류가 발생했습니다.");
        }
    }



}

