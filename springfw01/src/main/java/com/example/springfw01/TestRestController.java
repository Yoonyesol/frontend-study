package com.example.springfw01;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Lazy
@RestController
public class TestRestController {
    public TestRestController() {
        System.out.println("생성자 호출");
    }

    @RequestMapping("/test")  // URL 매핑 -> 테스트 요청이 오면 이 함수를 리턴해라
    public String test() {
        return "{msg: Hello World}";
    }
} // http://localhost:8080/test
