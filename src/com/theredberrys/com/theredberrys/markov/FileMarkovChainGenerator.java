package com.theredberrys.com.theredberrys.markov;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileMarkovChainGenerator {
    public static final String START = "";
    public static final String END = "END";

    public static MarkovChain<String> generateChain(String filepath) {
        List<List<String>> sequences = getSequences(filepath);

        MarkovChainGenerator<String> generator = new MarkovChainGenerator<>();
        return generator.generateMarkovChain(sequences, START, END);
    }

    private static List<List<String>> getSequences(String filepath) {
        List<List<String>> sequences = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
            stream.forEach(sequence -> sequences.add(processSequence(sequence)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sequences;
    }

    private static List<String> processSequence(String sequence) {
        List<String> chars = new ArrayList<>();
        for (int i = 0; i < sequence.length(); i++) {
            chars.add(Character.toString(sequence.charAt(i)));
        }
        return chars;
    }
}
