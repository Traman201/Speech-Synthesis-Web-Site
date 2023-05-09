package com.sergeev.srp.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hmm")
public class HMMController {

    @GetMapping
    public String mainView(Model model) {


        return "/site/hmm";
    }
}
