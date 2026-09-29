package com.example.springfw02_viewCtl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller // 스프링에 컨트롤러임을 인식시키기
@RequestMapping("/todos")  // url 접두어
public class TodoViewController {

    // 비즈니스 로직이 필요하다... 객체 선언
    // 의존성 주입 받아야 함 (스프링에게 달라고 함)
    private final TodoService todoService;

    // final이므로 값을 한번은 할당해야 함. 아니면 오류 남
    public TodoViewController(TodoService todoService) {
        this.todoService = todoService;
    }

    // 목록: GET /todos
    @GetMapping
    public String list(Model model) { // view의 이름을 리턴하므로 반환형은 string일 수밖에 없다...
        model.addAttribute("todos", todoService.findAll());
        return "todos/list";          //resources/templates/todos/list.html
    }

    // 상세: GET /todos/1
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Todo todo = todoService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("todo", todo);
        return "todos/detail";  //resources/templates/todos/detail.html
    }

    // 등록 폼: GET /todos/new
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("todo", new Todo());   // th:object용 빈 객체
        return "todos/form";  //resources/templates/todos/form.html
    }

    // 등록 처리: POST /todos
    @PostMapping
    public String create(@ModelAttribute Todo todo, RedirectAttributes ra) {
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

    // 삭제
    @PostMapping("/{id}")
    public String delete(@PathVariable Long id, Model model) {
        todoService.delete(id);
        return "redirect:/detail";  //resources/templates/todos/detail.html
    }
}