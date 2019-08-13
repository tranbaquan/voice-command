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
