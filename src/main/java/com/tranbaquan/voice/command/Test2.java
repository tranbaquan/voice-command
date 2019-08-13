package com.tranbaquan.voice.command;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.result.WordResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Test2 {
    private static final Map<String, Integer> DIGITS =
        new HashMap<String, Integer>();

    static {
        DIGITS.put("oh", 0);
        DIGITS.put("zero", 0);
        DIGITS.put("one", 1);
        DIGITS.put("two", 2);
        DIGITS.put("three", 3);
        DIGITS.put("four", 4);
        DIGITS.put("five", 5);
        DIGITS.put("six", 6);
        DIGITS.put("seven", 7);
        DIGITS.put("eight", 8);
        DIGITS.put("nine", 9);
    }

    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("file:D:/Model/5668.dict");
        configuration.setLanguageModelPath("file:D:/Model/5668.lm");

        LiveSpeechRecognizer liveSpeechRecognizer = new LiveSpeechRecognizer(configuration);
        liveSpeechRecognizer.startRecognition(true);
        while (true) {
            String utterance = liveSpeechRecognizer.getResult().getHypothesis();

            if (utterance.equals("CLOSE")) {
                break;
            }

            System.out.println(utterance);
            liveSpeechRecognizer.stopRecognition();
            liveSpeechRecognizer.startRecognition(true);
        }

        liveSpeechRecognizer.stopRecognition();

//        try {
//            StreamSpeechRecognizer recognizer = new StreamSpeechRecognizer(configuration);
//            InputStream stream = new FileInputStream(new File("D:/Train/pronunciation_en_play-2.mp3"));
//            recognizer.startRecognition(stream);
//            SpeechResult result;
//            while ((result = recognizer.getResult()) != null) {
//                System.out.format("Hypothesis: %s\n", result.getHypothesis());
//                System.out.println(result.getWords());
//            }
//            recognizer.stopRecognition();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
