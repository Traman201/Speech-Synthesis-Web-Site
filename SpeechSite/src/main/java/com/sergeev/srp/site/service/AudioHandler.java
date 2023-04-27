package com.sergeev.srp.site.service;

import com.sergeev.srp.common.model.TextToSpeech;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
@Log4j2
public class AudioHandler {

    @Value("${synthesis.audio-root}")
    String audioFolderPath;

    public String saveAudio(TextToSpeech textToSpeech) {
        try {
            log.info("Сохранение синтезированного звука");

            AudioFormat format = new AudioFormat(textToSpeech.getMetadata().getSampleRate(),
                    textToSpeech.getMetadata().getSampleSizeInBits(), textToSpeech.getMetadata().getChannels(),
                    textToSpeech.getMetadata().isSigned(),
                    textToSpeech.getMetadata().isBigEndian());
            InputStream b_in = new ByteArrayInputStream(textToSpeech.getAudio());
            AudioInputStream stream = new AudioInputStream(b_in, format,
                    textToSpeech.getAudio().length);
            File file = new File(audioFolderPath + "test.wav");
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
            log.info("Файл сохранен: {}", file.getPath());

            return file.getName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
