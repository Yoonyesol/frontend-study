package com.example.springfw02_viewCtl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {
    // db랑 연결 안 돼 있으니 리슽트에 저장
    private final List<Todo> todos = new CopyOnWriteArrayList<>(); // 쓰레드에 안전한 array list
    private final AtomicLong idGen = new AtomicLong(); // 쓰레드에 안전한 long 형

    public List<Todo> findAll() {
        return todos;
    }

    // null point 예외 방지를 위해 optional로 감싸줌
    public Optional<Todo> findById(Long id) {
        return todos.stream()
                .filter(t -> t.getId().equals(id)) // for문 안 쓰기 위해 stream 사용
                .findFirst(); // 하나라도 찾으면 바로 리턴
    }

    public Todo create(Todo todo) {
        if (todo.getTitle() == null || todo.getTitle().isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        todo.setId(idGen.getAndIncrement());
        todo.setDone(false);
        todos.add(todo);
        return todo;
    }

    public Optional<Todo> toggleDone(Long id) {
        return findById(id).map(t -> {
            t.setDone(!t.isDone());
            return t;
        });
    }

    public boolean delete(Long id) {
        return todos.removeIf(t -> t.getId().equals(id));
    }
}
