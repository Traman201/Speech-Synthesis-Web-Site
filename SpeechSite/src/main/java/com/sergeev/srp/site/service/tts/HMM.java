package com.sergeev.srp.site.service.tts;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.common.model.marytts.MaryTTSParameters;
import com.sergeev.srp.site.repository.LocaleRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HMM implements ModuleCommunicator {

    private final Logger log = LoggerFactory.getLogger(MaryTTS.class);

    private RestTemplate restTemplate;

    private MaryTTSParameters parameters;

    @Autowired
    private LocaleRepository localeRepository;

    @Value("${hmm.module-path}")
    private String defaultUrl;

    @Value("${hmm.user}")
    private String user;

    @Value("${hmm.pwd}")
    private String pwd;


    @Override
    @PostConstruct
    public void initialize() {
        restTemplate = new RestTemplateBuilder()
                .basicAuthentication(user, pwd)
                .build();
        parameters = null;
    }

    @Override
    public TextToSpeech synthesize(TextToSpeech textToSpeech) {
        return restTemplate.postForObject(defaultUrl, textToSpeech, TextToSpeech.class);
    }


}
