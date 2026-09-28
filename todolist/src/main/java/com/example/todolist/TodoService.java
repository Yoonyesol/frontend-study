package com.example.todolist;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class TodoService {
    private final List<Todo> todos = new CopyOnWriteArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public List<Todo> findAll() {
        return todos;
    }

    public Optional<Todo> findById(Long id) {
        return todos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }

    public List<Todo> search(String keyword) {
        return todos.stream()
                .filter(t -> t.getTitle().contains(keyword))
                .toList();
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
