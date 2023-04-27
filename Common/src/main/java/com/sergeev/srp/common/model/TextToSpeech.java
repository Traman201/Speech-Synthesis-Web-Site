package com.sergeev.srp.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sergeev.srp.common.model.file.AudioFileMetadata;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextToSpeech {

    String text;

    byte[] audio;

    AudioFileMetadata metadata;

    @Override
    public String toString() {
        return "TextToSpeech{" +
                "text='" + text + '\'' +
                ", soundLength=" + (audio == null ? 0 : audio.length) +
                '}';
    }
}
