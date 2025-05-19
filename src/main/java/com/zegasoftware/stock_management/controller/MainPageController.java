package com.zegasoftware.stock_management.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainPageController {

    @GetMapping("/secured")
    public String mainPage() {
        return "Welcome to the main page";
    }
}
