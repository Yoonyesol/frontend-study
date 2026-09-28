package com.example.todolist;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class Todo {
    private Long id;
    private String title;
    private boolean done;
}
