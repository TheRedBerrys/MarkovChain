package com.theredberrys;

import com.theredberrys.com.theredberrys.markov.FileMarkovChainGenerator;
import com.theredberrys.com.theredberrys.markov.MarkovChain;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        String filepath = "src/com/theredberrys/com/theredberrys/markov/resources/girl-names.txt";
        int iterations = 100;
        String start = FileMarkovChainGenerator.START;
        String end = FileMarkovChainGenerator.END;

        MarkovChain<String> chain = FileMarkovChainGenerator.generateChain(filepath);
        StringBuilder builder = new StringBuilder();
        while (iterations > 0) {
            List<String> name = chain.getCompleteSequence(start, end);
            System.out.println(getString(builder, name));
            iterations--;
        }
    }

    private static String getString(StringBuilder builder, List<String> sequence) {
        builder.setLength(0);
        sequence.forEach(c -> builder.append(c));
        return builder.toString();
    }
}
