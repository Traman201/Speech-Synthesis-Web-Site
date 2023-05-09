package com.sergeev.hmm.service;

import com.sergeev.hmm.misc.IPAReader;
import com.sergeev.hmm.model.SpeechModel;
import com.sergeev.srp.common.model.TextToSpeech;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@Log4j2
public class HmmService {

    private SpeechModel english;

    private SpeechModel russian;

    public HmmService() throws IOException, ParserConfigurationException, SAXException, URISyntaxException {

        IPAReader russianReader = new IPAReader(Paths
                .get(
                        Objects.requireNonNull(
                                getClass().getClassLoader()
                                        .getResource("alphabet/P2G.xml")
                        ).toURI()).toString());

        IPAReader englishReader = new IPAReader(Paths
                .get(
                        Objects.requireNonNull(
                                getClass().getClassLoader()
                                        .getResource("alphabet/IPA.xml")
                        ).toURI()).toString());

        String IPAv1ModelPath = Paths
                .get(
                        Objects.requireNonNull(
                                getClass().getClassLoader()
                                        .getResource("model/english/IPAv1.xml")
                        ).toURI()).toString();

        String J2PModelPath = Paths
                .get(
                        Objects.requireNonNull(
                                getClass().getClassLoader()
                                        .getResource("model/russian/J2Pv1.xml")
                        ).toURI()).toString();

        russian = new SpeechModel(J2PModelPath, russianReader);
        english = new SpeechModel(IPAv1ModelPath, englishReader);
    }

    public String transcribe(TextToSpeech text) throws IOException, ParserConfigurationException, SAXException {
        log.info("Получен запрос на синтез: {}", text.getText());
        String sanitized = text.getText().replaceAll("[^A-Za-zА-Яа-я ]+", "");
        log.info("Очищенный текст {}", sanitized);
        sanitized = sanitized.replaceAll("x", "ks");
        String[] words = sanitized.split(" ");

        StringBuilder builder = new StringBuilder();

        SpeechModel model;

        switch (text.getHmmParameters().getModel()) {
            case ENGLISH -> model = english;
            case RUSSIAN -> model = russian;
            default -> throw new IOException("Отсутствует система");
        }
        log.info("Выбрана система {}", text.getHmmParameters().getModel());

        for (String word : words) {
            StringBuilder wordBuilder = new StringBuilder();
            model.getPhonemes(word).forEach(s -> wordBuilder.append(s).append("-"));
            wordBuilder.deleteCharAt(wordBuilder.length() - 1);
            builder.append(wordBuilder).append("   ");
            log.info("Обработано слово {} --- {}", word, wordBuilder);
        }
        log.info("Сконструирована транскрипция {}", builder);

        return builder.toString();
    }
}
