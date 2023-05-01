package com.sergeev.srp.marytts.MaryTTS.controller;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.common.model.marytts.MaryTTSParameters;
import com.sergeev.srp.marytts.MaryTTS.Service.TextToSpeechService;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    TextToSpeechService speechService;

    @PostMapping
    public @ResponseBody TextToSpeech synthesizeText(@RequestBody TextToSpeech textToSpeech) throws SynthesisException, IOException, MaryConfigurationException {
        return speechService.textToSpeech(textToSpeech);
    }

    @GetMapping("/parameters")
    public @ResponseBody MaryTTSParameters getAvailableParams() throws MaryConfigurationException {
        return speechService.getAvailableParams();
    }
}
