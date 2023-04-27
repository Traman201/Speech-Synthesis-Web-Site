package com.sergeev.srp.site.controller;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.site.service.AudioHandler;
import com.sergeev.srp.site.service.tts.MaryTTS;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "/synthesis")
@Controller
@Log4j2
public class SpeechSynthesisController {

    @Autowired
    MaryTTS maryTTS;

    @Autowired
    AudioHandler audioHandler;

    @GetMapping
    public String mainView(Model model) {

        return "synthesis";
    }

    @PostMapping(value = "/synthesize")
    public @ResponseBody String requestSynthesis(@RequestBody TextToSpeech textToSpeech) {
        log.info("Получен запрос на синтез с текстом: {}", textToSpeech);
        textToSpeech = maryTTS.synthesize(textToSpeech);
        log.info("Получен ответ от модуля синтеза: {}", textToSpeech);
        return audioHandler.saveAudio(textToSpeech);

    }

}
