package com.tranbaquan.voice.command.classify.hmm;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class HiddenMarkovModel {
    static final double MIN_PROBABILITY = 0.00001;
    static final int DELTA = 2;
    private int observationSize;
    private int numStates;
    private int numSymbols;
    private int[][] observation;
    private int[] current;
    private int numObservation;
    private double[][] transiton;
    private double[][] output;
    private double[] pi;
    private double[][] alpha;
    private double[][] beta;

    public void baumWelch() {
        double[][] newTranslation = new double[numStates][numStates];
        double[][] newOutput = new double[numStates][numSymbols];

    }
}
