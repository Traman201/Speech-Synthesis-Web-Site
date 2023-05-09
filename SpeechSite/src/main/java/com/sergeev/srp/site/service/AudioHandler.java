package com.sergeev.srp.site.service;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.common.model.enums.Systems;
import com.sergeev.srp.site.entity.site.Audio;
import com.sergeev.srp.site.repository.AudioRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Log4j2
public class AudioHandler {

    @Value("${synthesis.audio-root}")
    String audioFolderPath;

    private final AudioRepository audioRepository;

    public AudioHandler(@Autowired AudioRepository audioRepository) {
        this.audioRepository = audioRepository;
    }

    public String saveAudio(TextToSpeech textToSpeech, HttpSession session) {
        try {
            log.info("Сохранение синтезированного звука");

            AudioFormat format = new AudioFormat(textToSpeech.getMetadata().getSampleRate(),
                    textToSpeech.getMetadata().getSampleSizeInBits(), textToSpeech.getMetadata().getChannels(),
                    textToSpeech.getMetadata().isSigned(),
                    textToSpeech.getMetadata().isBigEndian());
            InputStream b_in = new ByteArrayInputStream(textToSpeech.getAudio());
            AudioInputStream stream = new AudioInputStream(b_in, format,
                    textToSpeech.getAudio().length);
            File file = new File(audioFolderPath + "audio_" + LocalDateTime.now() + "_" + session.getId() + "_" + UUID.randomUUID() + ".wav");
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
            log.info("Файл сохранен: {}", file.getPath());

            Audio audio = new Audio();
            audio.setFilePath(file.getName());
            audio.setTime(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            audio.setSystemName(Systems.MaryTTS);
            audio.setRawText(textToSpeech.getText());
            audio.setSessionId(session.getId());
            audioRepository.save(audio);

            return file.getName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
