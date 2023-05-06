package com.sergeev.hmm.misc;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IPAReader {

    private final Document doc;
    private String IPAPath;

    public IPAReader(String ipaPath) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        this.IPAPath = ipaPath;
        doc = documentBuilder.parse(IPAPath);
    }

    public ArrayList<String> getAllGraphemes() {
        Node root = doc.getDocumentElement();
        Node phonemes = root.getFirstChild();

        ArrayList<String> graphemesString = new ArrayList<>();

        while (phonemes != null) {
            Node graphemes = phonemes.getFirstChild();
            while (graphemes != null) {
                if (Objects.equals(graphemes.getNodeName(), "grapheme") && !isInArray(graphemesString, graphemes.getTextContent())) {
                    graphemesString.add(graphemes.getTextContent());
                }
                graphemes = graphemes.getNextSibling();
            }
            phonemes = phonemes.getNextSibling();
        }
        return graphemesString;
    }

    public ArrayList<String> getAllPhonemes() {
        Node root = doc.getDocumentElement();
        Node phonemes = root.getFirstChild();

        ArrayList<String> graphemesString = new ArrayList<>();

        while (phonemes != null) {

            if (Objects.equals(phonemes.getNodeName(), "phoneme") && !isInArray(graphemesString, ((Element) phonemes).getAttribute("symbol"))) {
                graphemesString.add(((Element) phonemes).getAttribute("symbol"));
            }

            do {
                phonemes = phonemes.getNextSibling();
            } while (!(phonemes instanceof Element) && phonemes != null);
        }
        return graphemesString;
    }

    public ArrayList<String> getPossibleGraphemes(String phoneme) {
        Node root = doc.getDocumentElement();
        Node phonemes = root.getFirstChild();

        ArrayList<String> graphemesString = new ArrayList<>();

        while (phonemes != null) {

            if (Objects.equals(phonemes.getNodeName(), "phoneme") && phoneme.equals(((Element) phonemes).getAttribute("symbol"))) {
                Node graphemes = phonemes.getFirstChild();
                while (graphemes != null) {
                    if (Objects.equals(graphemes.getNodeName(), "grapheme") && !isInArray(graphemesString, graphemes.getTextContent())) {
                        graphemesString.add(graphemes.getTextContent());
                    }
                    graphemes = graphemes.getNextSibling();
                }
                break;
            }

            do {
                phonemes = phonemes.getNextSibling();
            } while (!(phonemes instanceof Element) && phonemes != null);
        }
        return graphemesString;
    }

    public static boolean isInArray(ArrayList<String> T, String key) {
        for (String s : T) {
            if (Objects.equals(s, key)) {
                return true;
            }
        }
        return false;
    }


    private static void constructIPA() throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.parse("src\\misc\\IPA.xml");
        Node root = doc.getDocumentElement();

        File file = new File("src\\misc\\test.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null) {
            String[] phonemeAndGrapheme = st.split("\\|");

            String phoneme = phonemeAndGrapheme[0];
            List<String> graphemes = new ArrayList<>(Arrays.asList(phonemeAndGrapheme[1].split(",")));

            Node phonemeRoot = null;

            phonemeRoot = doc.createElement("phoneme");

            for (String s : graphemes) {
                Node graphemeNode = doc.createElement("grapheme");
                graphemeNode.setTextContent(s);
                phonemeRoot.appendChild(graphemeNode);
            }
            root.appendChild(phonemeRoot);
            ((Element) phonemeRoot).setAttribute("symbol", phoneme);
            try {
                Source source = new DOMSource(doc);
                File xmlFile = new File("yourFile.xml");
                StreamResult result = new StreamResult(new OutputStreamWriter(
                        new FileOutputStream(xmlFile), "ISO-8859-1"));
                Transformer xformer = TransformerFactory.newInstance().newTransformer();
                xformer.transform(source, result);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public static String convertWordLetters(String word) {

        String[] letters = word.split("");

        for (int i = 0; i < letters.length; i++) {
            if (((Objects.equals(letters[i], "я") || letters[i].equals("е") ||
                    letters[i].equals("ё") || letters[i].equals("ю")) && i > 0)) {
                if (isVowel(letters[i - 1])) {
                    letters[i] = "й" + letters[i];
                }
            }
            if (((Objects.equals(letters[i], "я") || letters[i].equals("е") ||
                    letters[i].equals("ё") || letters[i].equals("ю")) && i == 0)) {
                letters[i] = "й" + letters[i];
            }

            if (i > 0 && letters[i].equals(letters[i - 1])) {
                letters[i] = "";
            }
        }

        StringBuilder sb = new StringBuilder();

        for (String l : letters) {
            sb.append(l);
        }

        return sb.toString();

    }

    public static boolean isVowel(String letter) {
        String[] vowels = {
                "ю",
                "ь",
                "и",
                "я",
                "э",
                "о",
                "а",
                "ы",
                "ъ",
                "е",
                "у",
                "й"
        };

        for (String vowel : vowels) {
            if (letter.equals(vowel)) {
                return true;
            }
        }
        return false;
    }

}
