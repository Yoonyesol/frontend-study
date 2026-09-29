package com.example.springfw02_viewCtl;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Todo {
    private Long id; // 할 일 번호
    private String title; // 할 일
    private boolean done; // 완료 여부
}
