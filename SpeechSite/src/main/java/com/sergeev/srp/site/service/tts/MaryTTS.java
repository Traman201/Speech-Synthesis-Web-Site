package com.sergeev.srp.site.service.tts;

import com.sergeev.srp.common.model.TextToSpeech;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MaryTTS implements ModuleCommunicator {

    private Logger log = LoggerFactory.getLogger(MaryTTS.class);

    private RestTemplate restTemplate;

    @Value("${mary-tts.module-path}")
    private String defaultUrl;

    public MaryTTS() {
        restTemplate = new RestTemplate();
    }

    @Override
    public TextToSpeech synthesize(TextToSpeech textToSpeech) {
        return restTemplate.postForObject(defaultUrl, textToSpeech, TextToSpeech.class);
    }
}
