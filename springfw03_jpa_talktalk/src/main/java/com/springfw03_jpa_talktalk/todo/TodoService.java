package com.springfw03_jpa_talktalk.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;

    //private final List<Todo> todos = new CopyOnWriteArrayList<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public List<Todo> findAll(){
        return todoRepository.findAll();
    }

    public Optional<Todo> findById(Long id){
        return todoRepository.findById(id);
    }

    public Todo create(Todo todo){
        if (todo.getTitle() == null || todo.getTitle().isBlank() ){
            throw  new IllegalArgumentException("제목은 필수입니다.");
        }
        System.out.println("create () ");
        todo.setId(idGen.getAndIncrement());
        todo.setId(2L);
        todo.setDone(0);
        todoRepository.save(todo);
        return todo;
    }

    public Optional<Todo> toggleDone(Long id) {
        return findById(id).map(t -> {
            t.setDone(t.getDone() == 0 ? 1 : 0 );
            return t;
        });
    }

    public void delete(Long id) {
       todoRepository.deleteById(id);
    }
}

