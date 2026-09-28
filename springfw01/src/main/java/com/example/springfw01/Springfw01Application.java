package com.example.springfw01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class Springfw01Application {

	public static void main(String[] args) {
		SpringApplication.run(Springfw01Application.class, args);

		CategoryModel c = new CategoryModel();
		c.setId("1");
		c.setName("홍길동");
		System.out.println(c);

		log.info(c.toString());
	}
}
