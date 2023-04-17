package com.sergeev.srp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "synthesis")
@Controller
public class SpeechSynthesisController {

    @GetMapping
    public String mainView(Model model){

        return "synthesis";
    }
}
