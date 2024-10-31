package com.example.shortmovie.Member.Controller;

import com.example.shortmovie.Member.Domain.MemberDTO;
import com.example.shortmovie.Member.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://52.78.50.190")
public class UserIntegrationController {

    @Autowired
    private MemberService memberService;

    // 회원 정보를 연동하는 API 엔드포인트
    @Autowired
    private MemberService userIntegrationService;

    // 회원 정보를 연동하는 API 엔드포인트
    @GetMapping("/integrateUser")
    public String integrateUser(
            @RequestParam("otpToken") String otpToken,
            @RequestParam("userId") String userId,
            @RequestParam("userName") String userName) {

        // 서비스 메서드 호출
        return userIntegrationService.integrateUser(otpToken, userId, userName);
    }

    @PostMapping("/saveMemberInfo")
    public ResponseEntity<MemberDTO> saveMemberInfo(@RequestBody MemberDTO memberDTO) {
        try {
            MemberDTO savedMember = memberService.saveMemberInfo(memberDTO);
            return ResponseEntity.ok(savedMember);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(null);
        }
    }
}


