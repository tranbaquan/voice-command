package com.tranbaquan.voice.command.feature;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import com.tranbaquan.voice.command.audio.AudioPlayer;
import com.tranbaquan.voice.command.filter.emphasis.PreEmphasisFunction;
import com.tranbaquan.voice.command.filter.filterbanks.FilterBanks;
import com.tranbaquan.voice.command.filter.frame.FramingFunction;
import com.tranbaquan.voice.command.filter.pcm.PCM;
import com.tranbaquan.voice.command.filter.window.HammingWindow;
import com.tranbaquan.voice.command.filter.window.WindowFunction;
import com.tranbaquan.voice.command.transform.discrete_cosin.DiscreteCosineTransform;
import com.tranbaquan.voice.command.transform.fourier.FastFourierTransform;
import com.tranbaquan.voice.command.utils.Complex;

public class FeatureExtractor {
    private AudioPlayer audio;
    private float[][] mfcc;
    private FeatureVector featureVector;

    public FeatureExtractor(AudioPlayer audio) {
        this.audio = audio;
    }

    private void doMfcc(){
        PCM pcm = new PCM();
        float[] pcmData = pcm.nomalize(audio.getFloatsData());
        PreEmphasisFunction preEmphasis = new PreEmphasisFunction();
        float[] f = preEmphasis.transform(pcmData);
        FramingFunction frame = new FramingFunction();
        float[][] framed = frame.framing(f, audio.getFormat().getSampleRate());
        WindowFunction window = new HammingWindow();
        FastFourierTransform fourier = new FastFourierTransform();
        FilterBanks filterBanks = new FilterBanks();
        DiscreteCosineTransform discreteCosineTransform = new DiscreteCosineTransform();
        mfcc = new float[framed.length][];
        for (int i = 0; i < framed.length; i++) {
            float[] windowed = window.windowing(framed[i]);
            Complex[] fouriered = fourier.transform(windowed);
            float[] f1 = fourier.toMagnitude(fouriered);
            float[] f2 = filterBanks.doFilterBanks(f1, audio.getFormat().getSampleRate());
            mfcc[i] = discreteCosineTransform.transform(f2);
        }
        System.gc();
    }

    public void extractFeature() {
        doMfcc();
        this.featureVector = new FeatureVector();
        featureVector.setMfcc(this.mfcc);
    }

    public FeatureVector getFeatureVector() {
        return featureVector;
    }
}
