package com.sergeev.hmm.model;


import com.sergeev.hmm.misc.IPAReader;
import com.sergeev.hmm.misc.LearningThread;
import com.sergeev.hmm.misc.WordDeconstruct;
import com.sergeev.srp.common.math.Matrix;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class SpeechModel extends HMM {

    public SpeechModel(String file, IPAReader reader) throws ParserConfigurationException, IOException, SAXException {
        super(reader);
        loadFromFile(file);
    }

    public SpeechModel(String words, String transcriptions, IPAReader reader) {
        super(reader);
        ArrayList<String> readWords, readTranscriptions;
        readTranscriptions = new ArrayList<>();
        readWords = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(words));
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                readWords.add(s);
            }
            bufferedReader = new BufferedReader(new FileReader(transcriptions));
            while ((s = bufferedReader.readLine()) != null) {
                readTranscriptions.add(s);
            }
            buildTransitionMatrix(readTranscriptions);
            buildEmissionMatrix(readWords, readTranscriptions);
            buildInitialProbabilityDistribution();
            //train(readWords, readTranscriptions);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        try {
            saveHMM("russian.xml", "russian");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//        IPAReader.setIPAPath("C:\\Users\\serge\\Desktop\\SRP_All\\SRP33\\src\\main\\java\\misc\\RussianP2G.xml");
//
//
//        //SpeechModel model = new SpeechModel("C:\\Users\\serge\\Desktop\\SRP_All\\SRP33\\src\\main\\resources\\russiandata\\russianwords4.txt",
//        //"C:\\Users\\serge\\Desktop\\SRP_All\\SRP33\\src\\main\\resources\\russiandata\\russianwordstranscribed4.txt");
//        SpeechModel model = new SpeechModel("C:\\Users\\serge\\Desktop\\SRP_All\\SRP33\\russian.xml");
//
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.println("Enter word(model): ");
//            String word;
//            word = scanner.nextLine();
//            for (String s : model.getPhonemes(word)) {
//                System.out.print(s + "-");
//            }
//            System.out.println();
//        }
//
//    }

    private void train(ArrayList<String> readWords, ArrayList<String> readTranscriptons) throws IOException, ParserConfigurationException, SAXException {
        training.start();
        ArrayList<LearningThread> threads = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            threads.add(new LearningThread(this));
        }
        for (int i = 10; i < readWords.size(); i++) {
            if (readWords.get(i).length() < 2) {
                continue;
            }
            String[] graphemes = findTrueGraphemeSequence(readTranscriptons.get(i).split(""), readWords.get(i));
            startCalc(LearningThread.functions.TRANSITION, graphemes, readTranscriptons.get(i).split(""), threads);
            System.out.println("Currently at " + i);
        }
        boolean isOver = false;
        while (!isOver) {
            isOver = true;
            for (LearningThread th : threads) {
                isOver = isOver && th.isDone;
            }
        }
    }

    private void startCalc(LearningThread.functions f, String[] observation, String[] transciption, ArrayList<LearningThread> threads) {
        do {
            for (LearningThread th : threads) {
                if (th.isDone) {
                    th.changeParams(f, observation, transciption);
                    if (th.isOver) {
                        th.toggleTurn();
                        new Thread(th).start();
                    }
                    return;
                }
            }
            try {
                Thread.sleep(1500);
                System.out.flush();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (true);
    }

    private void buildInitialProbabilityDistribution() {
        initialProbabilityDistribution = new ProbabilityMatrix();
        ArrayList<String> phonemes = reader.getAllPhonemes();
        int numNodes = phonemes.size();

        Matrix transitionProbability = getTransitionMatrix();
        Matrix initialDistribution = new Matrix(1, numNodes);
        initialDistribution.set(1, 1, 1);


        double eps = 0.0005;
        Matrix m = initialDistribution.mul(transitionProbability);
        double error = findError(initialDistribution, m);
        do {
            Matrix m1 = m.mul(transitionProbability);
            error = findError(m, m1);
            m = m1;
        } while (error > eps);

        for (int i = 0; i < m.cols(); i++) {
            initialProbabilityDistribution.addCell("1", phonemes.get(i), m.get(1, i + 1));
        }
    }

    private double findError(Matrix m1, Matrix m2) {
        double error = 0;
        for (int i = 1; i <= m1.rows(); i++) {
            for (int j = 1; j <= m1.cols(); j++) {
                if (error < (m1.get(i, j) - m2.get(i, j))) {
                    error = (m1.get(i, j) - m2.get(i, j));
                }
            }
        }
        error = Math.abs(error);
        return error;
    }

    private void buildEmissionMatrix(ArrayList<String> readWords, ArrayList<String> readTranscriptions) throws IOException, ParserConfigurationException, SAXException {
        emissionMatrix = new ProbabilityMatrix();
        ArrayList<GraphNode> phonemeNodes = new ArrayList<>();
        for (String phoneme : reader.getAllPhonemes()) {
            phonemeNodes.add(new GraphNode(phoneme));
        }
        ArrayList<GraphNode> graphemeNodes = new ArrayList<>();
        for (String g : reader.getAllGraphemes()) {
            graphemeNodes.add(new GraphNode(g));
        }
        int failed = 0;
        mainloop:
        for (int i = 0; i < readTranscriptions.size(); i++) {
            String[] transcription = getTranscription(readTranscriptions.get(i));
            String[] graphemes = findTrueGraphemeSequence(transcription, readWords.get(i));
            for (int j = 0; j < transcription.length; j++) {
                if (graphemes == null) {
                    System.out.println(i);
                    for (String a : transcription) {
                        System.out.print(a);
                    }
                    failed++;
                    continue mainloop;
                }
                findNode(phonemeNodes, transcription[j]).addTransition(findNode(graphemeNodes, graphemes[j]));
            }
        }
        System.out.println("Не удалось обработать " + failed + " слов");
        for (GraphNode node : phonemeNodes) {
            for (GraphNode.Transition t : node.transitions) {
                emissionMatrix.addCell(node.phoneme, t.nextNode.phoneme, (double) t.numTransitions / (double) node.numTransitions);
            }
        }
    }

    private String[] findTrueGraphemeSequence(String[] transcription, String word) throws IOException, ParserConfigurationException, SAXException {
        ArrayList<ArrayList<String>> possibleGraphemes;
        WordDeconstruct deconstruct = new WordDeconstruct(word, reader);
        possibleGraphemes = deconstruct.getSequences();

        deconstruct = new WordDeconstruct(IPAReader.convertWordLetters(word), reader);
        possibleGraphemes.addAll(deconstruct.getSequences());

        possibleGraphemes.removeIf(s -> s.size() != transcription.length);
        if (possibleGraphemes.size() == 1) {
            return possibleGraphemes.get(0).toArray(new String[0]);
        }
        for (ArrayList<String> possibleSequence : possibleGraphemes) {
            for (int i = 0; i < possibleSequence.size(); i++) {
                if (!IPAReader.isInArray(reader.getPossibleGraphemes(transcription[i]), possibleSequence.get(i))) {
                    break;
                }
                if (i + 1 == possibleSequence.size()) {
                    return possibleSequence.toArray(new String[0]);
                }

            }
        }
        return null;
    }

    private void buildTransitionMatrix(ArrayList<String> readTranscriptions) {
        transitionMatrix = new ProbabilityMatrix();
        ArrayList<GraphNode> nodes = new ArrayList<>();
        for (String phoneme : reader.getAllPhonemes()) {
            nodes.add(new GraphNode(phoneme));
        }
        for (String word : readTranscriptions) {
            String[] phonemes = getTranscription(word);
            for (int i = 0; i < phonemes.length - 1; i++) {
                findNode(nodes, phonemes[i]).addTransition(findNode(nodes, phonemes[i + 1]));
            }
        }

        for (GraphNode node : nodes) {
            for (GraphNode.Transition t : node.transitions) {
                transitionMatrix.addCell(node.phoneme, t.nextNode.phoneme, (double) t.numTransitions / (double) node.numTransitions);
            }
        }
    }

    private String[] getTranscription(String word) {
        String[] symbols = word.split("");
        String[] truePhonemes = word.split("'");

        int l = 0;
        for (String s : truePhonemes) {
            l += s.length();
        }

        String[] phonemes = new String[l];
        int restriction = 0;
        for (int i = 0; i < symbols.length; i++) {
            if (i + 1 != symbols.length && symbols[i + 1].equals("'")) {
                phonemes[i - restriction] = symbols[i] + symbols[i + 1];
                i++;
                restriction++;
            } else {
                phonemes[i - restriction] = symbols[i];
            }

        }
        return phonemes;
    }


}
