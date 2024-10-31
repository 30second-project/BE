package com.example.shortmovie.Member.Domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberDTO {
    private Long id;
    private String userName;
    private String memberId;
    private String contact;
    // 필요한 필드만 포함
}
