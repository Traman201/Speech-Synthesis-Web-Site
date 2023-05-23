package com.sergeev.srp.site.controller;


import com.sergeev.srp.site.entity.site.Audio;
import com.sergeev.srp.site.entity.site.Phonemes;
import com.sergeev.srp.site.repository.AudioRepository;
import com.sergeev.srp.site.repository.PhonemeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {

    private final AudioRepository audioRepository;

    private final PhonemeRepository phonemeRepository;

    public HistoryController(@Autowired AudioRepository audioRepository, @Autowired PhonemeRepository phonemeRepository) {
        this.audioRepository = audioRepository;
        this.phonemeRepository = phonemeRepository;
    }

    @GetMapping
    public String getHistory(Model model, HttpSession session) {
        List<Phonemes> phonemeList = phonemeRepository.findBySessionId(session.getId());
        List<Audio> audioList = audioRepository.findBySessionId(session.getId());

        model.addAttribute("phonemeList", phonemeList);
        model.addAttribute("audioList", audioList);
        return "site/history";

    }
}
