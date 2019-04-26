package com.tranbaquan.voice.command.filter.window;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class HammingWindow extends WindowFunction {
    /**
     * The property for the alpha value of the Window.
     */
    private float alpha;

    private static double TWO_PI = Math.PI * 2;

    public HammingWindow() {
        alpha = 0.46f;
    }

    @Override
    public float[] windowing(float[] data) {
        float[] window = new float[data.length];
        out:
        for (int i = 0; i < data.length; i++) {
            window[i] = (float) ((1 - alpha) - alpha * Math.cos(TWO_PI * i/ (data.length - 1)));
            window[i] *= data[i];
        }
        return window;
    }

}
