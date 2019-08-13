package com.tranbaquan.voice.command.sphinx4;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */


import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;

import java.io.IOException;

public class Recognizer {
    private Configuration configuration;
    private LiveSpeechRecognizer recognizer;

    public Recognizer() throws IOException {
        this.configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("file:D:/Model/5668.dict");
        configuration.setLanguageModelPath("file:D:/Model/5668.lm");

        recognizer = new LiveSpeechRecognizer(configuration);
    }

    public LiveSpeechRecognizer getRecognizer() {
        return recognizer;
    }
}
