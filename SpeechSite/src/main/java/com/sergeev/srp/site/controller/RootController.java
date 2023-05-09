package com.sergeev.srp.site.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/")
@Controller
public class RootController {

    @GetMapping
    public String redirectToMain() {
        return "site/index";
    }
}
