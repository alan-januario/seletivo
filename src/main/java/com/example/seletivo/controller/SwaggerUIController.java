package com.example.seletivo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SwaggerUIController {

    @GetMapping("/custom-swagger-ui")
    public String customSwaggerUI() {
        return "redirect:/custom-swagger-ui.html";
    }
}
