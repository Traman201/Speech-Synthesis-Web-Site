package com.sergeev.srp.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sergeev.srp.common.model.enums.Systems;
import com.sergeev.srp.common.model.file.AudioFileMetadata;
import com.sergeev.srp.common.model.hmm.HmmParameters;
import com.sergeev.srp.common.model.marytts.MaryTTSParameters;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextToSpeech {

    String text;

    byte[] audio;

    String transcription;

    AudioFileMetadata metadata;

    MaryTTSParameters maryTTSParameters;

    HmmParameters hmmParameters;

    Systems system;

    @Override
    public String toString() {
        return "TextToSpeech{" +
                "text='" + text + '\'' +
                ", metadata=" + metadata +
                ", maryTTSParameters=" + maryTTSParameters +
                ", system=" + system +
                '}';
    }
}
