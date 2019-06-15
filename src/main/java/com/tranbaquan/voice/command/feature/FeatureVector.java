package com.tranbaquan.voice.command.feature;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class FeatureVector {
    private float[][] mfcc;

    public FeatureVector() {
    }

    public void setMfcc(float[][] mfcc) {
        this.mfcc = mfcc;
    }

    public float[][] getMfcc() {
        return mfcc;
    }
}
