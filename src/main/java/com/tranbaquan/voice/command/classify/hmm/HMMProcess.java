package com.tranbaquan.voice.command.classify.hmm;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import com.tranbaquan.voice.command.audio.AudioPlayer;
import com.tranbaquan.voice.command.classify.lloyd.CodeBook;
import com.tranbaquan.voice.command.classify.lloyd.QuantizationVector;
import com.tranbaquan.voice.command.feature.FeatureExtractor;
import com.tranbaquan.voice.command.feature.FeatureVector;
import com.tranbaquan.voice.command.trainning.Dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HMMProcess {
    private int numStates;
    private int numSymbols;
    private List<HMM> trainedModels;
    private QuantizationVector quantization;
    private CodeBook codeBook;

    //5 8
    public HMMProcess(int numStates, int numSymbols) {
        this.numStates = numStates;
        this.numSymbols = numSymbols;
        trainedModels = new LinkedList<>();
        quantization = new QuantizationVector(13);
        codeBook = new CodeBook();
    }

    public void generateCodeBook(String trainFolderPath) {
        try {
            Files.newDirectoryStream(Paths.get(trainFolderPath)).forEach(path -> {
                addQuantizationVectorFromTrainWords(path.toString());
            });
            codeBook.setQuantization(quantization);
            codeBook.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addQuantizationVectorFromTrainWords(String wordFolder) {
        try {
            Files.newDirectoryStream(Paths.get(wordFolder)).forEach(path -> {
                AudioPlayer player = new AudioPlayer(path.toString());
                FeatureExtractor extractor = new FeatureExtractor(player);
                extractor.extractFeature();
                FeatureVector vector = extractor.getFeatureVector();
                for (int i = 0; i < vector.getMfcc().length; i++) {
                    quantization.addFeature(vector.getMfcc()[i]);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void trainWord(String trainFolderPath, Dictionary dictionary) {
        try {
            System.out.println("Training...");
            HMM newHmm = new HMM(numStates, numSymbols);
            newHmm.initialize();
            newHmm.setDictionary(dictionary);

            Files.newDirectoryStream(Paths.get(trainFolderPath)).forEach(path -> {
                AudioPlayer player = new AudioPlayer(path.toString());
                FeatureExtractor extractor = new FeatureExtractor(player);
                extractor.extractFeature();
                FeatureVector vector = extractor.getFeatureVector();
                int[] q = codeBook.quantize(vector);
                System.out.println(Arrays.toString(q));
                newHmm.reestimate(q, 20);
//                System.out.println(newHmm);
            });

            trainedModels.add(newHmm);
            System.out.println("Next...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void trainAllWords(String trainFolderPath) {
        try {
            System.out.println("Starting...");
            Files.newDirectoryStream(Paths.get(trainFolderPath)).forEach(path -> {
                String s = path.toString();
                Dictionary dictionary = getDictionary(s.substring(s.lastIndexOf("\\")+1));
                trainWord(s, dictionary);
            });
            System.out.println("Finish!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Dictionary recognize(String audioPath) {
        AudioPlayer player = new AudioPlayer(audioPath);
        FeatureExtractor extractor = new FeatureExtractor(player);
        extractor.extractFeature();
        FeatureVector vector = extractor.getFeatureVector();
        int[] q = codeBook.quantize(vector);

        double prob = 0;
        double temp;
        Dictionary dictionary = Dictionary.UNKNOWN;
        for (HMM hmm : trainedModels) {
            temp = hmm.prob(q);
            if (temp > prob) {
                prob = temp;
                dictionary = hmm.getDictionary();
            }
        }

        return dictionary;
    }

    public Dictionary getDictionary(String key) {
        switch (key) {
            case "play":
                return Dictionary.PLAY;
            case "pause":
                return Dictionary.PAUSE;
            case "next":
                return Dictionary.NEXT;
            case "previous":
                return Dictionary.PREVIOUS;
            case "speak_up":
                return Dictionary.SPEAK_UP;
            case "speak_down":
                return Dictionary.SPEAK_DOWN;
            case "close":
                return Dictionary.CLOSE;
        }
        return Dictionary.UNKNOWN;
    }

    public static void main(String[] args) {
        HMMProcess process = new HMMProcess(5, 8);
        process.generateCodeBook("D:\\Train");
        process.trainAllWords("D:\\Train");
        HMM hmm1 = process.trainedModels.get(0);
        HMM hmm2 = process.trainedModels.get(1);
        System.out.println(hmm1);
        System.out.println(hmm2);

        String audioPath = "D:\\Train\\play\\play-2.wav";


//        System.out.println(hmm1.prob(q));
//        System.out.println(hmm2.prob(q));
        System.out.println(process.recognize(audioPath));

    }
}
