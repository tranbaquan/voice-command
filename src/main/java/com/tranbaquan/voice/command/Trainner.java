package com.tranbaquan.voice.command;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
import com.tranbaquan.voice.command.audio.AudioPlayer;
import com.tranbaquan.voice.command.feature.FeatureExtractor;
import com.tranbaquan.voice.command.feature.FeatureVector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Trainner {
    private List<List<ObservationVector>> lists;
    private Hmm<ObservationVector> hmm;

    public Trainner() {
        lists = new LinkedList<>();
    }

    public void train(String path) {
        try {
            Files.newDirectoryStream(Paths.get(path)).forEach(p -> {
                String s = p.toString();
                trainWord(s);
            });

            KMeansLearner<ObservationVector> kml = new KMeansLearner<>(2, new OpdfMultiGaussianFactory(13), lists);

            hmm = kml.iterate();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void trainWord(String path) {
        try {
            Files.newDirectoryStream(Paths.get(path)).forEach(p -> {
                AudioPlayer player = new AudioPlayer(p.toString());
                FeatureExtractor extractor = new FeatureExtractor(player);
                extractor.extractFeature();
                FeatureVector vector = extractor.getFeatureVector();
                List<ObservationVector> list = new LinkedList<>();
                for (float[] v : vector.getMfcc()) {
                    double[] s = new double[v.length];
                    for (int i = 0; i < v.length; i++) {
                        s[i] = v[i];
                    }
                    System.out.println(Arrays.toString(s));
                    list.add(new ObservationVector(s));
                }
                lists.add(list);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Trainner trainner = new Trainner();
        trainner.train("D:/Train");

        String audioPath = "D:\\Train\\play\\play-4.wav";
        AudioPlayer player = new AudioPlayer(audioPath);
        FeatureExtractor extractor = new FeatureExtractor(player);
        extractor.extractFeature();
        FeatureVector vector = extractor.getFeatureVector();

        List<ObservationVector> l = new LinkedList<>();
        for (float[] v : vector.getMfcc()) {
            double[] k = new double[v.length];
            for (int i = 0; i < v.length; i++) {
                k[i] = v[i];
            }
            l.add(new ObservationVector(k));
        }

        System.out.println(trainner.hmm);
        System.out.println(trainner.hmm.probability(l));
    }
}
