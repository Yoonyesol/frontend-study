package com.springfw03_jpa_talktalk.todo;

import com.springfw03_jpa_talktalk.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // 쿼리 메서드: WHERE Title LIKE '%?%' order by id
    //List<Todo> findByTitleContainingOrderById(String keyword);

}
