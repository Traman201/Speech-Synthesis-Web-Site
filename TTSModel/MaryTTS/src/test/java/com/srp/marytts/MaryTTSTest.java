package com.srp.marytts;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.SynthesisException;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MaryTTSTest {

    @Test
    public void test() throws SynthesisException, LineUnavailableException, IOException {

        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    MaryInterface maryInterface = new LocalMaryInterface();
                    maryInterface.generateAudio("Test synthesis audio");

                    Clip clip = AudioSystem.getClip();
                    clip.open(maryInterface.generateAudio("Test synthesis audio"));
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
