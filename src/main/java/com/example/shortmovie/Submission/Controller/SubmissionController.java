//package com.example.shortmovie.Submission.Controller;
//
//import com.example.shortmovie.Movie.Domain.Video;
//import com.example.shortmovie.Submission.Domain.Submission;
//import com.example.shortmovie.Submission.Service.SubmissionService;
//import jakarta.servlet.http.HttpServletResponse;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.util.List;
//
//@CrossOrigin(origins = "http://52.78.50.190")
//@RestController
//@RequestMapping("/api/submissions")
//public class SubmissionController {
//
//    @Autowired
//    private SubmissionService submissionService;
//
//    // 제출 목록 전체 조회
//    @GetMapping
//    public ResponseEntity<List<Submission>> getAllSubmissions() {
//        List<Submission> submissions = submissionService.getAllSubmissionsWithVideos();
//        return new ResponseEntity<>(submissions, HttpStatus.OK);
//    }
//
//    // 검색 기능
//    @GetMapping("/search")
//    public ResponseEntity<List<Submission>> searchSubmissions(
//            @RequestParam String keyword, @RequestParam String category) {
//        List<Submission> submissions = submissionService.searchSubmissions(keyword, category);
//        return new ResponseEntity<>(submissions, HttpStatus.OK);
//    }
//
//    // 제출 정보 생성
//    @PostMapping
//    public ResponseEntity<Submission> createSubmission(@RequestBody Submission submission) {
//        Submission createdSubmission = submissionService.createSubmission(submission);
//        return new ResponseEntity<>(createdSubmission, HttpStatus.CREATED);
//    }
//
//    // 제출 정보 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteSubmission(@PathVariable Long id) {
//        submissionService.deleteSubmission(id);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
//
//    // 엑셀 파일 다운로드 API
//    @GetMapping("/download")
//    public void downloadExcel(HttpServletResponse response) throws IOException {
//        List<Submission> submissions = submissionService.getAllSubmissions();
//
//        // Workbook 생성
//        try (Workbook workbook = new XSSFWorkbook()) {
//            Sheet sheet = workbook.createSheet("Submissions");
//
//            // Header Row
//            Row header = sheet.createRow(0);
//            header.createCell(0).setCellValue("ID");
//            header.createCell(1).setCellValue("Name");
//            header.createCell(2).setCellValue("Member ID");
//            header.createCell(3).setCellValue("Contact");
//            header.createCell(4).setCellValue("Submission Time");
//            header.createCell(5).setCellValue("Agreement");
//            header.createCell(6).setCellValue("Video URLs");
//
//            // Data Rows
//            int rowIndex = 1;
//            for (Submission submission : submissions) {
//                Row row = sheet.createRow(rowIndex++);
//                row.createCell(0).setCellValue(submission.getId());
//                row.createCell(1).setCellValue(submission.getMember().getName());  // Member에서 name 가져오기
//                row.createCell(2).setCellValue(submission.getMember().getId()); // Member ID
//                row.createCell(3).setCellValue(submission.getMember().getContact()); // Member의 연락처
//                row.createCell(4).setCellValue(submission.getSubmissionTime());
//                row.createCell(5).setCellValue(submission.getAgreement() != null && submission.getAgreement() ? "동의함" : "동의 안 함");
//
//                // 비디오 URL들을 문자열로 결합하여 출력
//                StringBuilder videoUrls = new StringBuilder();
//                if (submission.getVideos() != null) {
//                    for (Video video : submission.getVideos()) {
//                        videoUrls.append(video.getVideoUrl()).append(", ");
//                    }
//                }
//                row.createCell(6).setCellValue(videoUrls.toString().replaceAll(", $", ""));
//            }
//
//            // 파일 설정
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setHeader("Content-Disposition", "attachment; filename=submissions.xlsx");
//
//            // 파일을 클라이언트에 보냄
//            workbook.write(response.getOutputStream());
//        } catch (IOException e) {
//            e.printStackTrace();
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error while generating Excel file.");
//        }
//    }
//
//    @GetMapping("/download/search")
//    public ResponseEntity<byte[]> downloadSearchResults(@RequestParam String keyword, @RequestParam String category) throws IOException {
//        List<Submission> submissions = submissionService.searchSubmissions(keyword, category);
//
//        // 엑셀 파일 생성 로직
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("제출 리스트");
//
//        // 헤더 생성
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"ID", "이름", "회원 ID", "연락처", "제출 시간", "개인정보 동의", "작품 URL"};
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//        }
//
//        // 데이터 행 추가
//        int rowNum = 1;
//        for (Submission submission : submissions) {
//            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(submission.getId());
//            row.createCell(1).setCellValue(submission.getMember().getName()); // Member에서 name 가져오기
//            row.createCell(2).setCellValue(submission.getMember().getId()); // Member ID
//            row.createCell(3).setCellValue(submission.getMember().getContact()); // Member의 연락처
//            row.createCell(4).setCellValue(submission.getSubmissionTime());
//            row.createCell(5).setCellValue(submission.getAgreement() ? "동의함" : "동의하지 않음");
//
//            // 비디오 URL 추가
//            StringBuilder videoUrls = new StringBuilder();
//            if (submission.getVideos() != null) {
//                for (Video video : submission.getVideos()) {
//                    videoUrls.append(video.getVideoUrl()).append(", ");
//                }
//            }
//            row.createCell(6).setCellValue(videoUrls.toString());
//        }
//
//        // 엑셀 파일 바이트 배열로 변환
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        workbook.write(outputStream);
//        workbook.close();
//        byte[] bytes = outputStream.toByteArray();
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Content-Disposition", "attachment; filename=submission_results.xlsx");
//        return ResponseEntity.ok()
//                .headers(httpHeaders)
//                .body(bytes);
//    }
//}
