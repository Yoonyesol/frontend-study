package com.springfw03_jpa_talktalk.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoViewController {

    private final TodoService todoService;

    @GetMapping
    public String List(Model model) {
        model.addAttribute("todos", todoService.findAll());
        return "todos/list";
    }

    // 상세: GET /todos/1
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Todo todo = todoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("todo", todo);
        return "todos/detail";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("todo", new Todo());
        return "todos/form";
    }

    @PostMapping
    public String create(@ModelAttribute Todo todo, RedirectAttributes ra) {
        System.out.println(" TodoViewController create () ");
        todoService.create(todo);
        ra.addFlashAttribute("message", "등록되었습니다.");
        return "redirect:/todos";          // PRG 패턴
    }

    // 완료 토글: POST /todos/1/toggle
    @PostMapping("/{id}/toggle")
    public String toggle(@PathVariable Long id) {
        todoService.toggleDone(id);
        return "redirect:/todos";
    }

    // 삭제 : Post /todos/1
    @PostMapping("/{id}")
    public String delete(@PathVariable Long id, Model model)  {
        todoService.delete(id);
        return "redirect:/todos";
    }
}