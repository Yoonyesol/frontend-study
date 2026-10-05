package com.springfw03_jpa_talktalk.member;

import com.springfw03_jpa_talktalk.member.MemberDto.* ;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // GET /api/members?keyword=홍
    @GetMapping
    public List<Response> list(@RequestParam(defaultValue = "") String keyword) {
        return memberService.list(keyword);
    }

    // GET /api/members/hong
    @GetMapping("/{memId}")
    public Response get(@PathVariable String memId) {
        return memberService.get(memId);
    }

    // POST /api/members
    @PostMapping
    public ResponseEntity<Map<String, String>> join(@RequestBody JoinRequest req) {
        String memId = memberService.join(req);
        return ResponseEntity.created(URI.create("/api/members/" + memId))
                .body(Map.of("memId", memId));
    }

    // PUT /api/members/hong
    @PutMapping("/{memId}")
    public ResponseEntity<Void> update(@PathVariable String memId, @RequestBody  UpdateRequest req) {
        memberService.update(memId, req);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/members/hong/password
    @PatchMapping("/{memId}/password")
    public ResponseEntity<Void> changePassword(@PathVariable String memId, @RequestBody   PasswordRequest req) {
        memberService.changePassword(memId, req);
        return ResponseEntity.noContent().build();
    }

    // PATCH /api/members/hong/cd
    @PatchMapping("/{memId}/cd")
    public ResponseEntity<Void> changeCd(@PathVariable String memId, @RequestBody  CdRequest req) {
        memberService.changeCd(memId, req);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/members/hong
    @DeleteMapping("/{memId}")
    public ResponseEntity<Void> delete(@PathVariable String memId) {
        memberService.delete(memId);
        return ResponseEntity.noContent().build();
    }

    // ----- 예외 처리 -----
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> notFound(NoSuchElementException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> conflict(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }
}
