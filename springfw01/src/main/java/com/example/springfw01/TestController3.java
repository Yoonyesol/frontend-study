package com.example.springfw01;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController3 {
    @GetMapping("/test3")
    Mem test() {
        Mem m = new Mem();
        return m;
    }


}

class Mem {
    String name = "홍길동";
    int id = 1;

    public String getName() {
        return name;
    }
}