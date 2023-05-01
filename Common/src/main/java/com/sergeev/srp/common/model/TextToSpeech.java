package com.sergeev.srp.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sergeev.srp.common.model.enums.Systems;
import com.sergeev.srp.common.model.file.AudioFileMetadata;
import com.sergeev.srp.common.model.marytts.MaryTTSParameters;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextToSpeech {

    String text;

    byte[] audio;

    AudioFileMetadata metadata;

    MaryTTSParameters maryTTSParameters;

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
