package com.theredberrys.com.theredberrys.markov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MarkovChain<T> {
    private Map<T, Map<T, Double>> probabilities;
    private Random random;

    public MarkovChain(Map<T, Map<T, Double>> probabilities, Random random) {
        this.probabilities = probabilities;
        this.random = random;
    }

    public List<T> getSequence(T start, int count) {
        List<T> sequence = new ArrayList<>();
        T current = start;
        sequence.add(current);

        for (int i = 1; i < count; i++) {
            current = getNext(current);
            sequence.add(current);
        }

        return sequence;
    }

    public List<T> getCompleteSequence(T start, T end) {
        List<T> sequence = new ArrayList<>();
        T current = start;

        while (!current.equals(end)) {
            sequence.add(current);
            current = getNext(current);
        }

        return sequence;
    }

    private T getNext(T current) {
        Double probability = random.nextDouble();
        Map<T, Double> currProbabilities = probabilities.get(current);
        Double cumulativeProbability = 0.0;
        for (T key : currProbabilities.keySet()) {
            cumulativeProbability += currProbabilities.get(key);
            if (cumulativeProbability >= probability) {
                return key;
            }
        }

        return null;
    }
}
