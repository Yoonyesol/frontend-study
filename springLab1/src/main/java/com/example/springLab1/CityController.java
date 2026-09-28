package com.example.springLab1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController // 스프링에게 관리받을 수 있게
public class CityController {
    ArrayList<City> arr = new ArrayList<>();

    @GetMapping("/city")
    ArrayList<City> getCity() {
        return arr;
    }

    @PostMapping("/city")
    // 포스트 요청 보낼때 이 바디가 같이 오면 이걸 이 변수에 담아라
    City createCity(@RequestBody City city) {
        arr.add(city);
        return city;
    }
}
