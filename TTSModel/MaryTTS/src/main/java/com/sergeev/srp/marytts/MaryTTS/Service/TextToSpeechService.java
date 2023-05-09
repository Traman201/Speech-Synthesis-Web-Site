package com.sergeev.srp.marytts.MaryTTS.Service;

import com.sergeev.srp.common.model.TextToSpeech;
import com.sergeev.srp.common.model.file.AudioFileMetadata;
import com.sergeev.srp.common.model.marytts.Effect;
import com.sergeev.srp.common.model.marytts.MaryTTSParameters;
import lombok.extern.log4j.Log4j2;
import marytts.LocalMaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.features.MaryGenericFeatureProcessors;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.util.*;

@Service
@Log4j2
public class TextToSpeechService {

    public TextToSpeechService() throws MaryConfigurationException {

    }

    public TextToSpeech textToSpeech(TextToSpeech text) throws SynthesisException, IOException, MaryConfigurationException {
        LocalMaryInterface marytts = new LocalMaryInterface();
        Locale locale = (Locale) text.getMaryTTSParameters().getLocales().toArray()[0];
        log.info("Создан язык {}", locale);
        marytts.setLocale(locale);
        String voice = text.getMaryTTSParameters().getVoices().toArray()[0].toString();
        marytts.setVoice(voice);
        log.info("Установлен голос {}", voice);


        StringBuilder effectsStringBuilder = new StringBuilder();

        for (Effect effect : text.getMaryTTSParameters().getAudioEffects()) {
            if (!effect.getIsActive()) {
                continue;
            }
            effectsStringBuilder.append(effect.getName());
            effectsStringBuilder.append("(");

            effect.getEffect().forEach((k, v) -> {
                effectsStringBuilder.append(k).append(":").append(v).append(",");
            });
            effectsStringBuilder.deleteCharAt(effectsStringBuilder.length() - 1);
            effectsStringBuilder.append(")+");
            log.info("Получен эффект " + effect.getName());
        }
        if (effectsStringBuilder.length() > 0) {
            effectsStringBuilder.deleteCharAt(effectsStringBuilder.length() - 1);
            log.info("Собрана строка дополнительных эффектов: {}", effectsStringBuilder.toString());
            marytts.setAudioEffects(effectsStringBuilder.toString());
        } else {
            log.info("Эффекты не получены");
        }


        try (AudioInputStream audioInputStream = marytts.generateAudio(text.getText())) {
            log.info("Успешно сгенерирован речевой сигнал длины {}", audioInputStream.getFrameLength());

            text.setAudio(IOUtils.toByteArray(audioInputStream));
            AudioFileMetadata metadata = new AudioFileMetadata();
            metadata.setChannels(audioInputStream.getFormat().getChannels());
            metadata.setSampleRate(audioInputStream.getFormat().getSampleRate());
            metadata.setSampleSizeInBits(audioInputStream.getFormat().getSampleSizeInBits());
            metadata.setSigned(true);
            metadata.setBigEndian(audioInputStream.getFormat().isBigEndian());
            text.setMetadata(metadata);
            return text;
        }
    }

    public MaryTTSParameters getAvailableParams() throws MaryConfigurationException {
        LocalMaryInterface marytts = new LocalMaryInterface();
        log.info("Язык по умолчанию: {}", marytts.getLocale());
        MaryTTSParameters parameters = new MaryTTSParameters();

        MaryGenericFeatureProcessors.Style style = new MaryGenericFeatureProcessors.Style();

        List<Effect> effects = new ArrayList<>();
        for (AudioEffect e : AudioEffects.getEffects()) {
            String effectsString = e.getExampleParameters();
            log.debug("Получен эффект {}", e.getName());
            String[] individualEffectString = effectsString.split(";");

            Map<String, Double> values = new HashMap<>();
            for (String parameter : individualEffectString) {
                log.debug("Обработка параметра {}", parameter);
                String[] parameterValue = parameter.split(":");
                if (parameterValue.length < 2) {
                    continue;
                }
                values.put(parameterValue[0], Double.parseDouble(parameterValue[1]));
            }
            effects.add(new Effect(e.getName(), values, false));
        }


        parameters.setAudioEffects(effects);
        parameters.setLocales(marytts.getAvailableLocales());
        parameters.setVoices(marytts.getAvailableVoices());
        parameters.setInputTypes(marytts.getAvailableInputTypes());
        parameters.setOutputTypes(marytts.getAvailableOutputTypes());


        return parameters;
    }
}
