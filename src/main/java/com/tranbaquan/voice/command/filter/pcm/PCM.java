package com.tranbaquan.voice.command.filter.pcm;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class PCM {
    public float[] nomalize(float[] data) {
        float[] output = new float[data.length];
        float max = data[0];
        for (int i = 1; i < data.length; i++) {
            if (max < Math.abs(data[i])) {
                max = Math.abs(data[i]);
            }
        }
        for (int i = 0; i < data.length; i++) {
            output[i] = data[i] / max;
        }
        return output;
    }
}
