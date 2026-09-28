package com.example.springfw01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRestController4 {
    @Autowired
    TestService testService;

    @GetMapping("/test4")
    Mem test4() {
        return testService.getMem();
    }
}

