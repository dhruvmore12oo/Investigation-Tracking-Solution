package com.example.Investigation_Tracking_Solution.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class testController {
    @RequestMapping("/hello")
    public String hello() {
        return "authentication";
    }
}
