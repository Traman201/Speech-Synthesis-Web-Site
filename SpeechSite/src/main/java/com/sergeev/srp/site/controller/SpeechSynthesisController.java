package com.sergeev.srp.site.controller;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.site.service.tts.MaryTTS;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@RequestMapping(value = "/synthesis")
@Controller
@Log4j2
public class SpeechSynthesisController {

    @Autowired
    MaryTTS maryTTS;

    @GetMapping
    public String mainView(Model model) {

        return "synthesis";
    }

    @PostMapping(value = "/synthesize")
    public @ResponseBody String requestSynthesis(@RequestBody TextToSpeech textToSpeech) {
        log.debug("Получен запрос на синтез с текстом: {}", textToSpeech.getText());
        textToSpeech = maryTTS.synthesize(textToSpeech);
        log.debug("Получен ответ от модуля синтеза");

        try {
            log.debug("Сохранение синтезированного звука");
            byte[] resultArray = textToSpeech.getSound();
            InputStream b_in = new ByteArrayInputStream(resultArray);

            AudioFormat format = new AudioFormat(8000f, 16, 1, true, false);
            AudioInputStream stream = new AudioInputStream(b_in, format,
                    resultArray.length);
            File file = new File(this.getClass().getResource("/").getPath() + "test.wav");
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);

            log.debug("Звуковой файл сохранен {}", file.getPath());
            return file.getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
