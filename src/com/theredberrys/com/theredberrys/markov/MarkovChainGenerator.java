package com.theredberrys.com.theredberrys.markov;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MarkovChainGenerator<T> {
    public MarkovChain<T> generateMarkovChain(List<List<T>> sequences, T start, T end) {
        Map<T, Map<T, Integer>> counts = getCounts(sequences, start, end);
        Map<T, Map<T, Double>> probabilities = getProbabilities(counts);
        return new MarkovChain<>(probabilities, new Random());
    }

    private Map<T, Map<T, Integer>> getCounts(List<List<T>> sequences, T start, T end) {
        Map<T, Map<T, Integer>> counts = new HashMap<>();

        for (List<T> sequence : sequences) {
            processSequence(sequence, counts, start, end);
        }

        return counts;
    }

    private void processSequence(List<T> sequence, Map<T, Map<T, Integer>> counts, T start, T end) {
        if (sequence == null || sequence.isEmpty()) {
            processState(counts, start, end);
            return;
        }

        processState(counts, start, sequence.get(0));
        for (int i = 0; i < sequence.size() - 1; i++) {
            processState(counts, sequence.get(i), sequence.get(i + 1));
        }
        processState(counts, sequence.get(sequence.size() - 1), end);
    }

    private void processState(Map<T, Map<T, Integer>> counts, T first, T second) {
        Map<T, Integer> innerCounts = counts.computeIfAbsent(first, k -> new HashMap<>());

        if (innerCounts.containsKey(second)) {
            innerCounts.put(second, innerCounts.get(second) + 1);
        } else {
            innerCounts.put(second, 1);
        }
    }

    private Map<T, Map<T, Double>> getProbabilities(Map<T, Map<T, Integer>> counts) {
        Map<T, Map<T, Double>> probabilities = new HashMap<>();

        for (T key : counts.keySet()) {
            probabilities.put(key, getStateProbabilities(counts.get(key)));
        }

        return probabilities;
    }

    private Map<T, Double> getStateProbabilities(Map<T, Integer> stateCounts) {
        Map<T, Double> stateProbabilities = new HashMap<>();

        Double total = stateCounts.values().stream().mapToDouble(i -> i).sum();
        for (T key : stateCounts.keySet()) {
            stateProbabilities.put(key, stateCounts.get(key) / total);
        }

        return stateProbabilities;
    }
}
