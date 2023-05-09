package com.sergeev.srp.site.controller;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.site.entity.hmm.HmmModel;
import com.sergeev.srp.site.repository.HmmModelRepository;
import com.sergeev.srp.site.service.RequestHandler;
import com.sergeev.srp.site.service.tts.HMM;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("/hmm")
public class HMMController {


    private final HMM hmm;

    private final RequestHandler audioHandler;

    private final HmmModelRepository hmmModelRepository;

    public HMMController(@Autowired HMM hmm, @Autowired RequestHandler audioHandler, @Autowired HmmModelRepository hmmModelRepository) {
        this.hmm = hmm;
        this.audioHandler = audioHandler;
        this.hmmModelRepository = hmmModelRepository;
    }

    @GetMapping
    public String mainView(Model model) {
        List<HmmModel> modelList = hmmModelRepository.findAll();
        model.addAttribute("models", modelList);
        return "/site/hmm";
    }

    @PostMapping(value = "/synthesize")
    public @ResponseBody String requestSynthesis(@RequestBody TextToSpeech textToSpeech, HttpSession session) {
        log.info("Получен запрос на синтез с текстом: {}", textToSpeech);
        if (textToSpeech.getText().length() > 100) {
            log.error("Слишком много символов в запросе");
            return "Слишком много символов";
        }
        textToSpeech = hmm.synthesize(textToSpeech);
        log.info("Получен ответ от модуля синтеза: {}", textToSpeech);
        return audioHandler.savePhonemes(textToSpeech, session);

    }
}
