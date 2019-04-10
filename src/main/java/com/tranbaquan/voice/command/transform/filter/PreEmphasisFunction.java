package com.tranbaquan.voice.command.transform.filter;


/*
 * Copyright (c) $year.
 * @author tranbaquan
 */
public class PreEmphasisFunction {

    /**
     * The property for the alpha value of the pre-emphasis.
     */
    private final float alpha;

    public PreEmphasisFunction() {
        alpha = 0.97f;
    }

    public PreEmphasisFunction(float alpha) {
        this.alpha = alpha;
    }

    /**
     * do pre-emphasis function
     * @param in audio data
     * @return pre-emphasised audio data
     */
    public float[] transform(float[] in) {
        if (in.length == 0) {
            return new float[0];
        }
        float[] out = new float[in.length];
        out[0] = in[0];
        for (int i = 1; i < in.length; i++) {
            out[i] = in[i] - alpha * in[i - 1];
        }
        return out;
    }
}
