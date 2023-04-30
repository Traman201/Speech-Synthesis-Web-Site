package com.sergeev.srp.site.service.tts;


import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.common.model.marytts.MaryTTSParameters;
import com.sergeev.srp.site.entity.mary.MaryTTSLocale;
import com.sergeev.srp.site.entity.mary.MaryTTSVoice;
import com.sergeev.srp.site.repository.LocaleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class MaryTTS implements ModuleCommunicator {

    private final Logger log = LoggerFactory.getLogger(MaryTTS.class);

    private final RestTemplate restTemplate;

    private MaryTTSParameters parameters;

    @Autowired
    private LocaleRepository localeRepository;

    @Value("${mary-tts.module-path}")
    private String defaultUrl;

    public MaryTTS() {
        restTemplate = new RestTemplate();
        parameters = null;
    }

    @Override
    public TextToSpeech synthesize(TextToSpeech textToSpeech) {
        return restTemplate.postForObject(defaultUrl, textToSpeech, TextToSpeech.class);
    }

    public MaryTTSParameters getAvailableParameters() {
        if (parameters == null) {
            parameters = restTemplate.getForObject(defaultUrl + "parameters", MaryTTSParameters.class);
        }

        return parameters;
    }

    public List<MaryTTSLocale> getAvailableLocales() {
        List<MaryTTSLocale> locales = localeRepository.findAll();
        log.info("Получены доступные языки: {}", locales);
        return locales;
    }

    public List<MaryTTSVoice> getAvailableVoices(String locale) {
        List<MaryTTSVoice> voices = localeRepository.findByName(locale).getVoices();
        log.info("Получены доступные голоса для языка {} : {}", locale, voices);
        return voices;
    }
}
