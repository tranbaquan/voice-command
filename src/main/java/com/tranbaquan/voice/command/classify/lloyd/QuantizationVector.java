package com.tranbaquan.voice.command.classify.lloyd;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import java.util.LinkedList;

public class QuantizationVector {
    protected LinkedList<double[]> data;
    protected int featureSize;

    public QuantizationVector() {
        data = new LinkedList<>();
    }

    public QuantizationVector(int featureSize) {
        data = new LinkedList<>();
        this.featureSize = featureSize;
    }

    public void addFeature(double[] feature) {
        data.add(feature);
    }

    public void setFeatureSize(int featureSize) {
        this.featureSize = featureSize;
    }
}
