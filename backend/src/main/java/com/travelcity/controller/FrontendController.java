package com.travelcity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FrontendController {
    
    @GetMapping(value = {"/", "/login", "/register", "/province/**"})
    public String index() {
        return "forward:/index.html";
    }
}
