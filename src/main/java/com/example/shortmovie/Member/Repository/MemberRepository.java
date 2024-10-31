package com.example.shortmovie.Member.Repository;

import com.example.shortmovie.Member.Domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 회원 ID를 기반으로 회원을 찾는 메서드
    Optional<Member> findByMemberId(String memberId);
}
