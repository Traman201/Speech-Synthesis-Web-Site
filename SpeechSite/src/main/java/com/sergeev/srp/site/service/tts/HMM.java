package com.sergeev.srp.site.service.tts;

import com.sergeev.srp.common.model.TextToSpeech;
import org.springframework.stereotype.Service;

@Service
public class HMM implements ModuleCommunicator {

    @Override
    public TextToSpeech synthesize(TextToSpeech textToSpeech) {
        return null;
    }

    @Override
    public void initialize() {
        
    }
}
