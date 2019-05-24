package com.tranbaquan.voice.command.filter.filterbanks;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class FilterBanks {
    private int numberFilters = 40;
    private float minFrequency = 130;
    private float maxFrequency = 6800;
    private MelFilter[] melFilters;

    /**
     * convert linear frequency to mel-frequency
     * @param linearFrequency
     * @return mel-frequency
     */
    public float linearToMel(double linearFrequency) {
        return (float) (1127.0 * (Math.log1p(linearFrequency / 700.0)));
    }

    private void buildFilterBank(int windowLength, float sampleRate) {
        float minFrequencyMel = linearToMel(minFrequency);
        float maxFrequencyMel = linearToMel(maxFrequency);
        float deltaFrequencyMel = (maxFrequencyMel - minFrequencyMel) / (numberFilters + 1);
        float deltaFrequency = sampleRate / windowLength;
        float[] melPoints = new float[windowLength / 2];
        melFilters = new MelFilter[numberFilters];
        for (int i = 0; i < melPoints.length; i++) {
            melPoints[i] = linearToMel(i * deltaFrequency);
        }
        for (int i = 0; i < numberFilters; i++) {
            float centerMel = minFrequencyMel + (i + 1) * deltaFrequencyMel;
            melFilters[i] = new MelFilter(centerMel, deltaFrequencyMel, melPoints);
        }
    }

    public float[] doFilterBanks(float[] data, float sampleRate) {
        int windowLength = (data.length - 1) << 1;
        buildFilterBank(windowLength, sampleRate);
        float[] output = new float[numberFilters];
        for (int i = 0; i < numberFilters; i++) {
            output[i] = melFilters[i].apply(data);
        }
        return output;
    }

    public float[] nonLinearTransform(float[] filterBank) {
        float f[] = new float[filterBank.length];
        final float FLOOR = -50;
        for (int i = 0; i < filterBank.length; i++){
            f[i] = (float) Math.log(filterBank[i]);
            if (f[i] < FLOOR) f[i] = FLOOR;
        }
        return f;
    }
}
