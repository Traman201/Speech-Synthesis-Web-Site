package com.sergeev.srp.marytts.MaryTTS.Service;

import com.sergeev.srp.common.model.TextToSpeech;
import marytts.MaryInterface;
import marytts.exceptions.SynthesisException;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;

@Service
public class TextToSpeechService {
    private MaryInterface marytts;

    public byte[] textToSpeech(TextToSpeech text) throws SynthesisException, IOException {
        
        try (AudioInputStream audioInputStream = marytts.generateAudio(text.getText())) {
            return audioInputStream.readAllBytes();
        }
    }
}
