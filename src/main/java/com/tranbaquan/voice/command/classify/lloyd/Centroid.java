package com.tranbaquan.voice.command.classify.lloyd;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class Centroid extends QuantizationVector {
    private double[] centroid;
    private double distortion;

    public Centroid() {
        super();
    }

    public Centroid(int featureSize) {
        super(featureSize);
    }

    public void updateCentroid(){
        double[] center = new double[featureSize];
        for (double[] feature : data) {
            for (int i = 0; i < featureSize; i++) {
                center[i] += feature[i];
            }
        }

        for (int i = 0; i < featureSize; i++) {
            center[i] /= data.size();
        }

        this.centroid = center;
    }
}
