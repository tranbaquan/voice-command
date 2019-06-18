package com.tranbaquan.voice.command.classify.lloyd;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import java.util.LinkedList;

public class Centroid extends QuantizationVector {
    private float[] centroid;
    private double distortion;

    public Centroid(int featureSize) {
        super(featureSize);
        centroid = new float[featureSize];
        distortion = 0;
    }

    public void updateCentroid(){
        float[] center = new float[featureSize];
        for (float[] feature : data) {
            for (int i = 0; i < featureSize; i++) {
                center[i] += feature[i];
            }
        }

        for (int i = 0; i < featureSize; i++) {
            if(data.size() == 0) {
                center[i] = centroid[i];
            }else {
                center[i] /= data.size();
            }

        }

        this.centroid = center;
        distortion = 0;
        data = new LinkedList<>();
    }

    public void addDistortion(double distance) {
        distortion+= distance;
    }

    public double getDistortion() {
        return distortion;
    }

    public void setDistortion(double distortion) {
        this.distortion = distortion;
    }

    public float[] getCentroid() {
        return centroid;
    }

    public void setCentroid(float[] centroid) {
        this.centroid = centroid;
    }
}
