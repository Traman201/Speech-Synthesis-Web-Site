package com.sergeev.srp.marytts.MaryTTS.controller;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.marytts.MaryTTS.Service.TextToSpeechService;
import marytts.exceptions.SynthesisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MainController {

    @Autowired
    TextToSpeechService speechService;

    @GetMapping
    public byte[] synthesizeText(@RequestBody TextToSpeech textToSpeech) throws SynthesisException, IOException {
        return speechService.textToSpeech(textToSpeech);
    }
}
