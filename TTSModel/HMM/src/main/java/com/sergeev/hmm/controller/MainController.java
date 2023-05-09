package com.sergeev.hmm.controller;

import com.sergeev.hmm.service.HmmService;
import com.sergeev.srp.common.model.TextToSpeech;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("/")
public class MainController {

    @Autowired
    private HmmService hmmService;

    @PostMapping
    public TextToSpeech transcribe(@RequestBody TextToSpeech text) throws IOException, ParserConfigurationException, SAXException {
        text.setTranscription(hmmService.transcribe(text));
        return text;
    }
}
