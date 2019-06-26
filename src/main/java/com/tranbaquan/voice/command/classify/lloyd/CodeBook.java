package com.tranbaquan.voice.command.classify.lloyd;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import com.tranbaquan.voice.command.feature.FeatureVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CodeBook {
    private QuantizationVector quantization;
    private List<Centroid> centroids;
    private final int size = 8;
    private final double splitRate = 0.01;

    public CodeBook() {
        quantization = new QuantizationVector(13);
        centroids = new LinkedList<>();
    }

    public int getSize() {
        return size;
    }

    public QuantizationVector getQuantization() {
        return quantization;
    }

    public void setQuantization(QuantizationVector quantization) {
        this.quantization = quantization;
    }

    public void initialize() {
        double beforeDistortion;
        double afterDistortion;
        Centroid centroid = new Centroid(13);
        centroid.setCentroid(quantization.getData().getFirst());
        centroid.setData(quantization.getData());
        centroid.updateCentroid();
        centroids.add(centroid);
        while (centroids.size() < size) {
            spitCentroids();
            do {
                beforeDistortion = 0;
                afterDistortion = 0;
                for (Centroid c: centroids) {
                    beforeDistortion += c.getDistortion();
                    c.updateCentroid();
                }

                for (float[] feature: quantization.getData()) {
                    findClosestCentroid(centroids, feature);
                }

                for (Centroid c: centroids) {
                    afterDistortion += c.getDistortion();
                }
            } while (Math.abs(beforeDistortion-afterDistortion) > 1);
        }
        for (Centroid c: centroids) {
            System.out.println(Arrays.toString(c.getCentroid()));
        }
    }

    public void spitCentroids() {
        List<Centroid> splitter = new ArrayList<>();
        for (Centroid centroid : centroids) {
            Centroid c1 = new Centroid(13);
            Centroid c2 = new Centroid(13);
            float[] feature1 = new float[centroid.getFeatureSize()];
            float[] feature2 = new float[centroid.getFeatureSize()];
            for (int i = 0; i < centroid.getCentroid().length; i++) {
                feature1[i] = (float) (centroid.getCentroid()[i]*(1 + splitRate));
                feature2[i] = (float) (centroid.getCentroid()[i]*(1 - splitRate));
            }
            c1.setCentroid(feature1);
            c2.setCentroid(feature2);
            splitter.add(c1);
            splitter.add(c2);
        }

        for (float[] feature: quantization.getData()) {
            findClosestCentroid(splitter, feature);
        }
        centroids = splitter;
    }

    private void findClosestCentroid(List<Centroid> splitter, float[] feature) {
        int index = findClosestCentroidIndex(splitter, feature);
        splitter.get(index).addFeature(feature);
        double distortion = calculateDistance(splitter.get(index).getCentroid(), feature);
        splitter.get(index).addDistortion(distortion);
    }

    private int findClosestCentroidIndex(List<Centroid> splitter, float[] feature) {
        double min = 10000;
        double distance;
        int index = 0;
        for (int i = 0; i < splitter.size(); i++) {
            distance = calculateDistance(splitter.get(i).getCentroid(), feature);
            if(min > distance) {
                min = distance;
                index = i;
            }
        }
        return index;
    }

    private double calculateDistance(float[] centroid, float[] feature) {
        double distance = 0;
        for (int i = 0; i < centroid.length; i++) {
            distance += Math.pow(centroid[i] - feature[i], 2);
        }
        return Math.sqrt(distance);
    }

    public int[] quantize(FeatureVector featureVector){
        int[] output = new int[featureVector.getMfcc().length];
        for (int i = 0; i < featureVector.getMfcc().length; i++) {
            output[i] = findClosestCentroidIndex(centroids, featureVector.getMfcc()[i]);
        }
        return output;
    }
}
