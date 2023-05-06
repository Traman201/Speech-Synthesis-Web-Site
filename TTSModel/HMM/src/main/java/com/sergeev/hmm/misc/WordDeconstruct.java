package com.sergeev.hmm.misc;


import java.util.ArrayList;
import java.util.Collections;

public class WordDeconstruct {

    private static ArrayList<String> listOfGraphemes;
    private String theWord;

//    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
//        Scanner scanner = new Scanner(System.in);
//        IPAReader reader = new IPAReader("C:\\Users\\serge\\Desktop\\SRP_All\\SRP33\\src\\main\\java\\misc\\RussianP2G.xml");
//        ArrayList<String> graphemes = reader.getAllGraphemes();
//
//        while (true) {
//            System.out.println("Enter the word: ");
//            String word = scanner.nextLine();
//
//            ArrayList<ArrayList<String>> sequences = new WordDeconstruct(word, reader).getSequences();
//
//            for (ArrayList<String> sequence : sequences) {
//                for (String grapheme : sequence) {
//                    System.out.print(grapheme + "-");
//                }
//                System.out.println();
//            }
//            System.out.println();
//
//        }
//    }

    public WordDeconstruct(String word, IPAReader reader) {
        this.theWord = word.toLowerCase();
        listOfGraphemes = reader.getAllGraphemes();
    }

    public ArrayList<ArrayList<String>> getSequences() {
        ArrayList<ArrayList<String>> sequences = new ArrayList<>();
        GraphemeNode[] lastLeaves = constructTree(theWord);
        for (GraphemeNode node : lastLeaves) {
            ArrayList<String> sequence = new ArrayList<>();
            do {
                sequence.add(node.grapheme);
                node = node.parent;
            } while (node.depth != 0);
            Collections.reverse(sequence);
            sequences.add(sequence);
        }
        return sequences;
    }

    private GraphemeNode[] constructTree(String word) {
        ArrayList<GraphemeNode> lastLeaves = new ArrayList<>();
        new GraphemeNode(word, lastLeaves);
        return lastLeaves.toArray(new GraphemeNode[lastLeaves.size()]);
    }

    class GraphemeNode {

        GraphemeNode parent = null;
        int depth;
        String grapheme;

        public GraphemeNode(String word, ArrayList<GraphemeNode> lastLeaves) {
            this.depth = 0;
            this.grapheme = "";

            if (word.length() == 0) {
                lastLeaves.add(this);
                return;
            }

            ArrayList<String> possibleChildren = new ArrayList<>();
            for (String g : WordDeconstruct.listOfGraphemes) {
                if (word.length() >= g.length() && word.substring(0, g.length()).equals(g)) {
                    possibleChildren.add(g);
                }
            }
            for (String g : possibleChildren) {
                String newWord = word.substring(g.length());
                new GraphemeNode(newWord, this, g, lastLeaves);
            }
        }

        private GraphemeNode(String word, GraphemeNode parent, String grapheme, ArrayList<GraphemeNode> lastLeaves) {
            this.parent = parent;
            this.depth = parent.depth + 1;
            this.grapheme = grapheme;

            if (word.length() == 0) {
                lastLeaves.add(this);
                return;
            }

            ArrayList<String> possibleChildren = new ArrayList<>();
            for (String g : WordDeconstruct.listOfGraphemes) {
                if (word.length() >= g.length() && word.substring(0, g.length()).equals(g)) {
                    possibleChildren.add(g);
                }
            }
            for (String g : possibleChildren) {
                String newWord = word.substring(g.length());
                new GraphemeNode(newWord, this, g, lastLeaves);
            }

        }
    }
}
