package com.sergeev.srp.site.service.tts;

import com.sergeev.srp.common.model.TextToSpeech;

public interface ModuleCommunicator {
    public TextToSpeech synthesize(TextToSpeech textToSpeech);
}
