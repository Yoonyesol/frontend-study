package com.example.springfw01;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    @ResponseBody
    @RequestMapping("/test2")
    public String test2(Model model) {
        model.addAttribute("name", "Spring");
        return "hello"; // 이건 resources > templates > hello.html이 있어야 함
    }
}
