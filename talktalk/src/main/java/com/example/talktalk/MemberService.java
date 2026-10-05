package com.example.talktalk;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 목록 (이름 검색)
    public List<Response> list(String keyword) {
        return memberRepository.findByMemNmContainingOrderByMemId(keyword)
                .stream().map(Response::from).toList();
    }

    // 단건 조회
    public Response get(String memId) {
        return Response.from(findMember(memId));
    }

    // 가입
    @Transactional
    public String join(JoinRequest req) {
        if (memberRepository.existsById(req.memId())) {
            throw new IllegalStateException("이미 사용 중인 아이디입니다.");
        }
        Member member = new Member(req.memId(), req.memNm(), req.pwd(), req.profileImg());
        memberRepository.save(member);
        return member.getMemId();
    }

    // 정보 수정 — save() 없이 변경 감지로 UPDATE
    @Transactional
    public void update(String memId, UpdateRequest req) {
        findMember(memId).changeInfo(req.memNm(), req.profileImg());
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(String memId, PasswordRequest req) {
        findMember(memId).changePassword(req.currentPwd(), req.newPwd());
    }

    // 회원 구분 변경
    @Transactional
    public void changeCd(String memId, CdRequest req) {
        findMember(memId).changeCd(req.memCd());
    }

    // 탈퇴 (물리 삭제)
    @Transactional
    public void delete(String memId) {
        memberRepository.delete(findMember(memId));
    }

    private Member findMember(String memId) {
        return memberRepository.findById(memId)
                .orElseThrow(() -> new NoSuchElementException("회원이 없습니다. memId=" + memId));
    }
}
