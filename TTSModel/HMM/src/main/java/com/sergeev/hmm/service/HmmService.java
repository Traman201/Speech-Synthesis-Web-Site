package com.sergeev.hmm.service;

import com.sergeev.hmm.misc.IPAReader;
import com.sergeev.hmm.model.SpeechModel;
import com.sergeev.srp.common.model.TextToSpeech;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.Objects;

@Service
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
        return english.getPhonemes(text.getText()).toString();
    }
}
