package com.sergeev.srp.site;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

@Log4j2
public class FileSaveTest {

    @Test
    public void audioFileSaveTest() throws UnsupportedAudioFileException, IOException {
        File audioFile = new File("src/test/resources/audio/sample-3s.wav");
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile)) {
            log.info("Размер чанка: {}", audioInputStream.getFormat().getFrameSize());
            log.info("Каналов: {}", audioInputStream.getFormat().getChannels());
            log.info("Частота дискретизации: {}", audioInputStream.getFormat().getFrameRate());
            log.info("Формат: {}", audioInputStream.getFormat().getEncoding());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
