package com.sergeev.srp.site.controller;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.common.model.enums.Systems;
import com.sergeev.srp.common.model.marytts.MaryTTSParameters;
import com.sergeev.srp.site.entity.mary.MaryTTSVoice;
import com.sergeev.srp.site.service.AudioHandler;
import com.sergeev.srp.site.service.tts.MaryTTS;
import lombok.extern.log4j.Log4j2;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

        return "/site/speech-synthesis";
    }

    @PostMapping(value = "/synthesize")
    public @ResponseBody String requestSynthesis(@RequestBody TextToSpeech textToSpeech) {
        log.info("Получен запрос на синтез с текстом: {}", textToSpeech);
        textToSpeech = maryTTS.synthesize(textToSpeech);
        log.info("Получен ответ от модуля синтеза: {}", textToSpeech);
        return audioHandler.saveAudio(textToSpeech);

    }

    @GetMapping("/system/parameters")
    public String getMaryTTSAvailableParams(@RequestParam Systems system, Model model) {
        switch (system) {
            case MaryTTS -> {
                MaryTTSParameters parameters = maryTTS.getAvailableParameters();
                log.info("Успешно получены параметры от системы {}", "MaryTTS");

                model.addAttribute("parameters", parameters);
                model.addAttribute("locales", maryTTS.getAvailableLocales());

                return "/parts/module_settings/mary-tts.html :: mainMaryTTS";
            }
            case HMM -> {
                return "";
            }
            default -> {
                throw new NotYetImplementedException("Отсутствует система по умолчанию");
            }
        }
    }

    @GetMapping("/system/parameters/voices")
    public String getVoices(Model model, @RequestParam Systems system, @RequestParam String locale) {
        switch (system) {
            case MaryTTS -> {
                List<MaryTTSVoice> voices = maryTTS.getAvailableVoices(locale);
                model.addAttribute("voices", voices);

                return "/parts/module_settings/mary-tts.html :: maryTTSVoiceSelect";
            }
            case HMM -> {
                return "";
            }
            default -> {
                throw new NotYetImplementedException("Отсутствует система по умолчанию");
            }
        }
    }

}
