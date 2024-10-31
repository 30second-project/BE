package com.example.shortmovie.Member.Service;

import com.example.shortmovie.Member.Domain.Member;
import com.example.shortmovie.Member.Domain.MemberDTO;
import com.example.shortmovie.Member.Repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);
    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;

    public MemberService(RestTemplate restTemplate, MemberRepository memberRepository) {
        this.restTemplate = restTemplate;
        this.memberRepository = memberRepository;
    }


    public ResponseEntity<String> verifyOtpToken(String otpToken) {
        String url = "https://www.pobanuri.or.kr/sso/vendorUserCertTonken?otpToken=" + otpToken; // 실제 환경 URL로 변경

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            logger.info("OTP 토큰 인증 요청: {}", url);
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(headers),
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                logger.info("OTP 인증 성공: {}", response.getBody());
            } else {
                logger.warn("OTP 인증 실패: {}", response.getBody());
            }
            return response;
        } catch (Exception e) {
            logger.error("OTP 토큰 인증 중 오류 발생", e);
            throw new RuntimeException("OTP 토큰 인증 중 오류 발생", e);
        }
    }


    // 회원 정보를 연동하는 메서드
    public String integrateUser(String otpToken, String userId, String userName) {
        // OTP_TOKEN 인증 요청
        ResponseEntity<String> otpResponse = verifyOtpToken(otpToken);

        if (otpResponse.getStatusCode() == HttpStatus.OK) {
            // 인증 성공시 회원 정보 연동
            String url = "http://dev-fo.etbs.co.kr/sso/vendorUserCertTonken?otpToken=" + otpToken;

            // 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("vendor", "VR005749"); // vendor 정보 추가
            headers.set("svid", "2848"); // svid 정보 추가

            // POST 요청에 필요한 데이터 설정 (회원 정보 연동 파라미터)
            Map<String, Object> params = new HashMap<>();
            params.put("CFM_CUS_ID", userId); // 고객 ID
            params.put("USER_NAME", userName); // 고객 이름
            params.put("OTP_TOKEN", otpToken); // OTP 토큰

            // HttpEntity로 헤더와 바디 설정
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

            // API 호출 (POST 요청)
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class
            );

            // 응답 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody(); // 성공 응답 처리
            } else {
                throw new RuntimeException("회원 연동 실패: " + response.getStatusCode());
            }
        } else {
            throw new RuntimeException("OTP_TOKEN 인증 실패: " + otpResponse.getBody());
        }
    }


    public MemberDTO saveMemberInfo(MemberDTO memberDTO) {
        Member member = new Member();
        member.setUserName(memberDTO.getUserName());
        member.setMemberId(memberDTO.getMemberId());
        member.setContact(memberDTO.getContact());

        Member savedMember = memberRepository.save(member);
        logger.info("회원 정보 저장 완료: {}", savedMember);

        return toMemberDTO(savedMember);
    }

    private MemberDTO toMemberDTO(Member member) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setUserName(member.getUserName());
        memberDTO.setMemberId(member.getMemberId());
        memberDTO.setContact(member.getContact());
        logger.info("Member 엔티티를 DTO로 변환: {}", memberDTO);
        return memberDTO;
    }
}


//
//    // OTP_TOKEN 인증 및 회원 정보를 연동하는 메서드
//    public MemberDTO getMemberInfoByOtpToken(String otpToken) {
//        ResponseEntity<String> otpResponse = verifyOtpToken(otpToken);
//
//        if (otpResponse.getStatusCode() == HttpStatus.OK) {
//            logger.info("OTP 인증 성공, 회원 정보 조회 중...");
//            Member member = memberRepository.findByMemberId(otpToken)
//                    .orElseThrow(() -> {
//                        logger.error("해당 회원이 존재하지 않습니다: OTP Token = {}", otpToken);
//                        return new RuntimeException("해당 회원이 존재하지 않습니다.");
//                    });
//
//            return toMemberDTO(member);
//        } else {
//            logger.error("OTP 인증 실패: {}", otpResponse.getBody());
//            throw new RuntimeException("OTP_TOKEN 인증 실패: " + otpResponse.getBody());
//        }
//    }