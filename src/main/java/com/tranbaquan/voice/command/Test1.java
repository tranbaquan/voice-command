package com.tranbaquan.voice.command;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.apps.sample.SimpleExample;
import be.ac.ulg.montefiore.run.jahmm.draw.GenericHmmDrawerDot;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
import be.ac.ulg.montefiore.run.jahmm.toolbox.KullbackLeiblerDistanceCalculator;
import be.ac.ulg.montefiore.run.jahmm.toolbox.MarkovGenerator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Test1 {
    public static void main(String[] argv) throws IOException {
        Hmm<ObservationInteger> hmm = new Hmm<>(5, new OpdfIntegerFactory(4));

        hmm.setPi(0, 0.2);
        hmm.setPi(1, 0.2);
        hmm.setPi(2, 0.2);
        hmm.setPi(3, 0.2);
        hmm.setPi(4, 0.2);

        hmm.setOpdf(0, new OpdfInteger(new double[]{0.2, 0.3, 0.3, 0.2}));
        hmm.setOpdf(1, new OpdfInteger(new double[]{0.2, 0.3, 0.3, 0.2}));
        hmm.setOpdf(2, new OpdfInteger(new double[]{0.2, 0.3, 0.3, 0.2}));
        hmm.setOpdf(3, new OpdfInteger(new double[]{0.2, 0.3, 0.3, 0.2}));
        hmm.setOpdf(4, new OpdfInteger(new double[]{0.2, 0.3, 0.3, 0.2}));

        hmm.setAij(0, 0, 0.2);
        hmm.setAij(0, 1, 0.2);
        hmm.setAij(0, 2, 0.2);
        hmm.setAij(0, 3, 0.2);
        hmm.setAij(0, 4, 0.2);

        hmm.setAij(1, 0, 0.2);
        hmm.setAij(1, 1, 0.2);
        hmm.setAij(1, 2, 0.2);
        hmm.setAij(1, 3, 0.2);
        hmm.setAij(1, 4, 0.2);

        hmm.setAij(2, 0, 0.2);
        hmm.setAij(2, 1, 0.2);
        hmm.setAij(2, 2, 0.2);
        hmm.setAij(2, 3, 0.2);
        hmm.setAij(2, 4, 0.2);

        hmm.setAij(3, 0, 0.2);
        hmm.setAij(3, 1, 0.2);
        hmm.setAij(3, 2, 0.2);
        hmm.setAij(3, 3, 0.2);
        hmm.setAij(3, 4, 0.2);

        hmm.setAij(4, 0, 0.2);
        hmm.setAij(4, 1, 0.2);
        hmm.setAij(4, 2, 0.2);
        hmm.setAij(4, 3, 0.2);
        hmm.setAij(4, 4, 0.2);

//        KMeansLearner<ObservationInteger> kml = new KMeansLearner<>()

    }
}