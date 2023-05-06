package com.sergeev.hmm.model;

import com.sergeev.hmm.misc.IPAReader;
import com.sergeev.hmm.misc.WordDeconstruct;
import com.sergeev.srp.common.math.Matrix;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class HMM {

    ProbabilityMatrix emissionMatrix, transitionMatrix, initialProbabilityDistribution;
    public String modelName;

    protected IPAReader reader;

    public HMM(IPAReader reader) {
        training = new Training();
        emissionMatrix = new ProbabilityMatrix();
        transitionMatrix = new ProbabilityMatrix();
        initialProbabilityDistribution = new ProbabilityMatrix();
        this.reader = reader;
    }

    protected GraphNode findNode(ArrayList<GraphNode> nodes, String phoneme) {
        for (GraphNode n : nodes) {
            if (n.phoneme.equals(phoneme)) {
                return n;
            }
        }
        System.out.println("Failed to find " + phoneme);
        return null;
    }

    Training training;

    public void loadFromFile(String file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.parse(file);

        Node root = doc.getDocumentElement();
        modelName = ((Element) root).getAttribute("name");

        Node matrix = root.getFirstChild();
        Node phoneme, grapheme;
        int x = 0;
        do {
            switch (matrix.getNodeName()) {
                case "transitionMatrix":
                    phoneme = matrix.getFirstChild();
                    while (phoneme != null) {
                        while (!(phoneme instanceof Element) && phoneme != null) {
                            phoneme = phoneme.getNextSibling();
                        }
                        if (phoneme != null) {
                            grapheme = phoneme.getFirstChild();
                            while (grapheme != null) {
                                while (!(grapheme instanceof Element) && grapheme != null) {
                                    grapheme = grapheme.getNextSibling();
                                }
                                if (grapheme != null) {
                                    transitionMatrix.setProbability(((Element) phoneme).getAttribute("phoneme"),
                                            ((Element) grapheme).getAttribute("phoneme"),
                                            Double.parseDouble(((Element) grapheme).getAttribute("probability")));
                                    grapheme = grapheme.getNextSibling();
                                }
                            }
                            phoneme = phoneme.getNextSibling();
                        }
                    }

                    break;
                case "emissionMatrix":
                    phoneme = matrix.getFirstChild();
                    while (phoneme != null) {
                        while (!(phoneme instanceof Element) && phoneme != null) {
                            phoneme = phoneme.getNextSibling();
                        }
                        if (phoneme != null) {
                            grapheme = phoneme.getFirstChild();
                            while (grapheme != null) {
                                while (!(grapheme instanceof Element) && grapheme != null) {
                                    grapheme = grapheme.getNextSibling();
                                }
                                if (grapheme != null) {
                                    emissionMatrix.setProbability(((Element) phoneme).getAttribute("phoneme"),
                                            ((Element) grapheme).getAttribute("grapheme"),
                                            Double.parseDouble(((Element) grapheme).getAttribute("probability")));
                                    grapheme = grapheme.getNextSibling();
                                }
                            }
                            phoneme = phoneme.getNextSibling();
                        }
                    }

                    break;
                case "initialDistribution":
                    grapheme = matrix.getFirstChild();
                    while (grapheme != null) {
                        while (!(grapheme instanceof Element) && grapheme != null) {
                            grapheme = grapheme.getNextSibling();
                        }
                        if (grapheme != null) {
                            initialProbabilityDistribution.setProbability("1",
                                    ((Element) grapheme).getAttribute("symbol"),
                                    Double.parseDouble(((Element) grapheme).getAttribute("probability")));
                            grapheme = grapheme.getNextSibling();
                        }
                    }
                    break;
            }
            matrix = matrix.getNextSibling();
        } while (matrix != null);
    }

    public void saveHMM(String fileName, String modelName) throws ParserConfigurationException, TransformerException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = documentBuilder.newDocument();

        Element root = doc.createElement("HMM");
        root.setAttribute("name", modelName);
        doc.appendChild(root);

        Element transitionMatrix = doc.createElement("transitionMatrix");
        root.appendChild(transitionMatrix);

        for (String row : reader.getAllPhonemes()) {
            Element rowNode = doc.createElement("row");
            rowNode.setAttribute("phoneme", row);
            for (String col : reader.getAllPhonemes()) {
                Element colNode = doc.createElement("col");
                colNode.setAttribute("phoneme", col);
                colNode.setAttribute("probability", Double.toString(this.transitionMatrix.findProbability(row, col)));
                rowNode.appendChild(colNode);
            }
            transitionMatrix.appendChild(rowNode);
        }

        Element emission = doc.createElement("emissionMatrix");
        root.appendChild(emission);

        for (String row : reader.getAllPhonemes()) {
            Element rowNode = doc.createElement("row");
            rowNode.setAttribute("phoneme", row);
            for (String col : reader.getAllGraphemes()) {
                Element colNode = doc.createElement("col");
                colNode.setAttribute("grapheme", col);
                colNode.setAttribute("probability", Double.toString(this.emissionMatrix.findProbability(row, col)));
                rowNode.appendChild(colNode);
            }
            emission.appendChild(rowNode);
        }

        Element initialDistribution = doc.createElement("initialDistribution");
        root.appendChild(initialDistribution);

        for (String row : reader.getAllPhonemes()) {
            Element phoneme = doc.createElement("phoneme");
            phoneme.setAttribute("symbol", row);
            phoneme.setAttribute("probability", Double.toString(initialProbabilityDistribution.findProbability("1", row)));
            initialDistribution.appendChild(phoneme);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(fileName));
        transformer.transform(domSource, streamResult);


    }

    public ArrayList<String> getPhonemes(String word) throws IOException, ParserConfigurationException, SAXException {
        return viterbi(deconstructWord(word)).transcription;
    }

    public ArrayList<String> deconstructWord(String word) {
        ArrayList<ArrayList<String>> sequences = new WordDeconstruct(word, reader).getSequences();
        sequences.addAll(new WordDeconstruct(IPAReader.convertWordLetters(word), reader).getSequences());

        double maxProbability = -1;
        ArrayList<String> bestSequence = null;
        for (ArrayList<String> s : sequences) {
            double probability = forward(s);
            if (maxProbability < probability) {
                maxProbability = probability;
                bestSequence = s;
            }
        }
        return bestSequence;

    }

    protected void startTraining() {
        training.start();
    }

    public double calcNewInitialDistribution(String state, ArrayList<String> observations) {
        return gamma(state, 0, observations);
    }

    public double calcNewEmissionProbability(String state, String observation, ArrayList<String> observations) {
        double enumerator = 0, denumerator = 0;
        for (int t = 0; t < observations.size(); t++) {
            if (observation == observations.get(t)) {
                enumerator += gamma(state, t, observations);
            }
            denumerator += gamma(state, t, observations);
        }
        return enumerator / denumerator;
    }

    public void estimateTransitionProbability(String[] graphemes, String[] transcription) {
        ArrayList<String> graphemic = new ArrayList<>();
        Collections.addAll(graphemic, graphemes);

        for (int i = 0; i < graphemic.size() - 1; i++) {
            double szhe = szhe(transcription[i], transcription[i + 1], i, graphemic);
            synchronized (training.lock) {
                training.szheMatrix.addCell(transcription[i], transcription[i + 1], szhe, i);
            }
        }

    }

    public double calcNewTransitionProbability(String row, String col, ArrayList<String> observation) {
        double enumerator = 0, denumerator = 0;
        for (int t = 0; t < observation.size() - 1; t++) {
            enumerator += szhe(row, col, t, observation);
            denumerator += gamma(row, t, observation);
        }
        return enumerator / denumerator;
    }

    public double gamma(String phoneme, int time, ArrayList<String> observation) {
        double denumerator = 0;
        ArrayList<String> phonemes = reader.getAllPhonemes();
        for (String s : phonemes) {
            denumerator += alpha(s, time, observation) * betta(s, time, observation);
        }
        return (alpha(phoneme, time, observation) * betta(phoneme, time, observation)) / denumerator;

    }

    public double szhe(String first, String second, int time, ArrayList<String> observation) {
        double numenator = alpha(first, time, observation) * transitionMatrix.findProbability(first, second) * emissionMatrix.findProbability(second, observation.get(time + 1)) *
                betta(second, time + 1, observation);
        double denumenator = 0;
        ArrayList<String> phonemes = reader.getAllPhonemes();

        for (String j : phonemes) {
            denumenator += alpha(j, time, observation) * betta(j, time, observation);
        }
        /*for(String i : phonemes){
            for (String j : phonemes) {
                denumenator += alpha(i, time, observation) * transitionMatrix.findProbability(i, j) *
                        emissionMatrix.findProbability(j, observation.get(time + 1)) * betta(j, time + 1, observation);
            }
        }*/
        return numenator / denumenator;
    }

    public double betta(String phoneme, int time, ArrayList<String> observations) {
        if (time == observations.size() - 1) {
            return 1;
        }
        ArrayList<String> phonemes = reader.getAllPhonemes();
        double betta = 0;
        for (int i = 0; i < phonemes.size(); i++) {
            if (transitionMatrix.findProbability(phoneme, phonemes.get(i)) != 0 && emissionMatrix.findProbability(phonemes.get(i), observations.get(time + 1)) != 0) {
                betta += transitionMatrix.findProbability(phoneme, phonemes.get(i)) * emissionMatrix.findProbability(phonemes.get(i), observations.get(time + 1)) *
                        betta(phonemes.get(i), time + 1, observations);
            }
        }
        return betta;
    }

    public double alpha(String phoneme, int time, ArrayList<String> observations) {
        if (time == 0) {
            return initialProbabilityDistribution.findProbability("1", phoneme) * emissionMatrix.findProbability(phoneme, observations.get(0));
        }

        double alpha = 0;
        ArrayList<String> phonemes = reader.getAllPhonemes();
        for (int i = 0; i < phonemes.size(); i++) {
            if (transitionMatrix.findProbability(phonemes.get(i), phoneme) != 0 && emissionMatrix.findProbability(phoneme, observations.get(time)) != 0) {
                alpha += alpha(phonemes.get(i), time - 1, observations) * transitionMatrix.findProbability(phonemes.get(i), phoneme) *
                        emissionMatrix.findProbability(phoneme, observations.get(time));
            }

        }
        return alpha;
    }

    public double forward(ArrayList<String> observations) {
        ArrayList<String> phonemes = reader.getAllPhonemes();

        Matrix forward = new Matrix(phonemes.size(), observations.size());
        for (int s = 1; s <= phonemes.size(); s++) {
            double f = initialProbabilityDistribution.findProbability("1", phonemes.get(s - 1)) *
                    emissionMatrix.findProbability(phonemes.get(s - 1), observations.get(0));
            forward.set(s, 1, f);
        }
        for (int t = 2; t <= observations.size(); t++) {
            for (int s = 1; s <= phonemes.size(); s++) {
                double summ = 0;
                for (int st = 1; st <= phonemes.size(); st++) {
                    summ += forward.get(st, t - 1) *
                            transitionMatrix.findProbability(phonemes.get(st - 1), phonemes.get(t - 1)) *
                            emissionMatrix.findProbability(phonemes.get(s - 1), observations.get(t - 1));
                }

                forward.set(s, t, summ);
            }
        }
        double finalProbability = 0;

        for (int s = 1; s <= phonemes.size(); s++) {
            finalProbability += forward.get(s, observations.size());
        }
        return finalProbability;

    }

    public Transcription viterbi(ArrayList<String> observations) {
        ArrayList<String> phonemes = reader.getAllPhonemes();

        Matrix viterbi = new Matrix(phonemes.size(), observations.size());
        ArrayList<ArrayList<Integer>> backpointer = new ArrayList<>();
        Matrix initialDistribution = getInitialProbabilityDistribution();
        for (int i = 1; i <= phonemes.size(); i++) {
            double v = initialDistribution.get(1, i) * emissionMatrix.findProbability(phonemes.get(i - 1), observations.get(0));
            viterbi.set(i, 1, v);
            backpointer.add(new ArrayList<>());
        }
        for (int t = 2; t <= observations.size(); t++) {
            for (int s = 1; s <= phonemes.size(); s++) {
                double maxV = 0;
                int bestBackpoint = -1;
                for (int st = 1; st <= phonemes.size(); st++) {
                    double curr = viterbi.get(st, t - 1) *
                            transitionMatrix.findProbability(phonemes.get(st - 1), phonemes.get(s - 1)) *
                            emissionMatrix.findProbability(phonemes.get(s - 1), observations.get(t - 1));
                    if (maxV < curr) {
                        maxV = curr;
                        bestBackpoint = st - 1;
                    }
                }
                viterbi.set(s, t, maxV);
                backpointer.get(t - 1).add(bestBackpoint);
            }
        }
        double bestPathProb = 0;
        int bestLastPhoneme = -1;
        for (int i = 1; i <= phonemes.size(); i++) {
            if (viterbi.get(i, observations.size()) > bestPathProb) {
                bestPathProb = viterbi.get(i, observations.size());
                bestLastPhoneme = i - 1;
            }
        }
        Transcription t = new Transcription();
        t.probability = bestPathProb;
        t.transcription.add(phonemes.get(bestLastPhoneme));

        for (int i = observations.size() - 1; i > 0; i--) {
            bestLastPhoneme = backpointer.get(i).get(bestLastPhoneme);
            t.transcription.add(phonemes.get(bestLastPhoneme));
        }
        Collections.reverse(t.transcription);
        return t;
    }

    public Matrix getEmissionMatrix() {
        return null;
    }

    public Matrix getTransitionMatrix() {
        int numSides = reader.getAllPhonemes().size();
        Matrix m = new Matrix(numSides, numSides);
        int i = 1, j = 1;
        for (String row : reader.getAllPhonemes()) {
            for (String col : reader.getAllPhonemes()) {
                double probability = transitionMatrix.findProbability(row, col);
                m.set(i, j, probability);
                j++;
            }
            j = 1;
            i++;
        }
        return m;
    }

    public Matrix getInitialProbabilityDistribution() {
        int numCols = reader.getAllPhonemes().size();
        Matrix m = new Matrix(1, numCols);
        int i = 1;
        for (String col : reader.getAllPhonemes()) {
            double probability = initialProbabilityDistribution.findProbability("1", col);
            m.set(1, i, probability);
            i++;
        }
        return m;
    }

    protected void printTransitionMatrix() {
        for (ProbabilityMatrix.Cell c : transitionMatrix.cells) {
            System.out.println(c.phonemeRow + "->" + c.phonemeColumn + "=" + c.transitionProbability);
        }
    }

    protected void printEmissionMatrix() {
        for (ProbabilityMatrix.Cell c : emissionMatrix.cells) {
            System.out.println(c.phonemeRow + "->" + c.phonemeColumn + "=" + c.transitionProbability);
        }
    }

    class Training {
        ExpectationMatrix szheMatrix;
        final Object lock = new Object();

        void start() {
            szheMatrix = new ExpectationMatrix();
        }
    }

    class ProbabilityMatrix {
        ArrayList<Cell> cells;

        ProbabilityMatrix() {
            cells = new ArrayList<>();
        }

        public void addCell(String row, String col, double transitionProbability) {
            cells.add(new Cell(row, col, transitionProbability));
        }

        public double findProbability(String row, String col) {
            for (Cell c : cells) {
                if (c.phonemeRow.equals(row) && c.phonemeColumn.equals(col)) {
                    return c.transitionProbability;
                }
            }
            return 0;
        }

        public void setProbability(String row, String col, double newProbability) {
            for (Cell c : cells) {
                if (c.phonemeRow.equals(row) && c.phonemeColumn.equals(col)) {
                    c.transitionProbability = newProbability;
                    return;
                }
            }
            addCell(row, col, newProbability);
        }

        private class Cell {
            String phonemeRow, phonemeColumn;
            double transitionProbability;

            Cell(String phonemeRow, String phonemeColumn, double transitionProbability) {
                this.phonemeRow = phonemeRow;
                this.phonemeColumn = phonemeColumn;
                this.transitionProbability = transitionProbability;
            }
        }
    }

    class ExpectationMatrix extends ProbabilityMatrix {
        ArrayList<Cell> cells;

        ExpectationMatrix() {
            cells = new ArrayList<>();
        }

        public double findProbability(String first, String second, int time) {
            for (Cell c : cells) {
                if (c.phonemeRow.equals(first) && c.phonemeColumn.equals(second) && c.time == time) {
                    return c.transitionProbability;
                }
            }
            return 0;
        }

        public void addCell(String row, String col, double transitionProbability, int time) {
            for (Cell c : cells) {
                if (c.phonemeRow.equals(row) && c.phonemeColumn.equals(col) && c.time == time) {
                    c.addProbability(transitionProbability);
                    return;
                }
            }
            cells.add(new Cell(row, col, transitionProbability, time));
        }

        public void setProbability(String row, String col, double newProbability, int time) {
            for (Cell c : cells) {
                if (c.phonemeRow.equals(row) && c.phonemeColumn.equals(col) && c.time == time) {
                    c.transitionProbability = newProbability;
                }
            }
        }

        class Cell {
            String phonemeRow, phonemeColumn;
            double transitionProbability;
            int time;
            double probabilitySum = 0;
            int probabilityCount = 0;

            Cell(String phonemeRow, String phonemeColumn, double transitionProbability, int time) {
                this.phonemeRow = phonemeRow;
                this.phonemeColumn = phonemeColumn;
                this.probabilitySum = transitionProbability;
                this.time = time;
                this.probabilityCount++;
                this.transitionProbability = transitionProbability;
            }

            void addProbability(double probability) {
                this.probabilitySum += probability;
                this.probabilityCount++;
                this.transitionProbability = probabilitySum / probabilityCount;
            }
        }
    }

    protected class GraphNode {
        String phoneme;
        ArrayList<Transition> transitions;
        int numTransitions = 0;

        public GraphNode(String phoneme) {
            this.phoneme = phoneme;
            transitions = new ArrayList<>();
        }

        public void addTransition(GraphNode next) {
            numTransitions++;
            for (Transition transition : transitions) {
                if (transition.nextNode.equals(next)) {
                    transition.addCount();
                    return;
                }
            }
            transitions.add(new Transition(next));
        }

        public class Transition {
            int numTransitions = 0;
            GraphNode nextNode = null;

            public Transition(GraphNode nextNode) {
                this.nextNode = nextNode;
                numTransitions = 1;
            }

            public void addCount() {
                numTransitions++;
            }

        }
    }

    class Transcription {
        ArrayList<String> transcription;
        double probability;

        Transcription() {
            transcription = new ArrayList<>();
        }
    }

}
