package com.tranbaquan.voice.command.classify.lloyd;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import java.util.LinkedList;

public class QuantizationVector {
    protected LinkedList<float[]> data;
    protected int featureSize;

    public QuantizationVector(int featureSize) {
        data = new LinkedList<>();
        this.featureSize = featureSize;
    }

    public void addFeature(float[] feature) {
        data.add(feature);
    }

    public int getFeatureSize() {
        return featureSize;
    }

    public LinkedList<float[]> getData() {
        return data;
    }

    public void setData(LinkedList<float[]> data) {
        this.data = data;
    }
}
