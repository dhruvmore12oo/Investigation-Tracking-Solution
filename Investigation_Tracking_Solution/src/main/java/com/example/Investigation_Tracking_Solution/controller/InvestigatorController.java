package com.example.Investigation_Tracking_Solution.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvestigatorController {

    @GetMapping("/investigator/test")
    public String testApi() {
        return "Hello World";
    }
}
