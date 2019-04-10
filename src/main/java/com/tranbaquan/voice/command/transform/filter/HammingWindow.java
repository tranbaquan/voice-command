package com.tranbaquan.voice.command.transform.filter;

public class HammingWindow extends WindowFunction {
    /**
     * The property for the alpha value of the Window.
     */
    private float alpha;

    private static double TWO_PI = Math.PI * 2;

    public HammingWindow() {
        super();
        alpha = 0.46f;
    }

    @Override
    public float[] windowing(float[] data, float sampleRate) {
        float[] window = new float[data.length];
        int windowSize = getWindowSize(sampleRate);
        int windowShift = getWindowShift(sampleRate);
        int s = 0;
        out:
        while (true) {
            for (int i = s; i < windowSize + s; i++) {
                if (i == data.length) {
                    break out;
                }
                window[i] = (float) ((1 - alpha) - alpha * Math.cos(TWO_PI * (i - s) / (windowSize - 1)));
                window[i] *= data[i];
            }
            s += windowShift;
        }
        return window;
    }
}
