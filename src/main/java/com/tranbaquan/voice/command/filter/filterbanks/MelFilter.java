package com.tranbaquan.voice.command.filter.filterbanks;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import static java.util.Arrays.copyOfRange;

/**
 * define triangular mel-filter
 */
public class MelFilter {
    private final int offset;
    private final float[] weights;

    public MelFilter(float center, float delta, float[] melPoints) {
        int lastIndex = 0;
        int firstIndex = melPoints.length;
        float left = center - delta;
        float right = center + delta;
        float [] heights = new float[melPoints.length];

        for (int i = 0; i < heights.length; ++i) {
            if (left < melPoints[i] && melPoints[i] <= center) {
                heights[i] = (melPoints[i] - left) / (center - left);
                firstIndex = Math.min(i, firstIndex);
                lastIndex = i;
            }

            if (center < melPoints[i] && melPoints[i] < right) {
                heights[i] = (right - melPoints[i]) / (right - center);
                lastIndex = i;
            }
        }

        offset = firstIndex;
        weights = copyOfRange(heights, firstIndex, lastIndex + 1);
    }

    public float apply(float[] powerSpectrum) {
        float result = 0;
        for (int i = 0; i < weights.length; ++i)
            result += weights[i] * powerSpectrum[offset + i];

        return result;
    }
}
