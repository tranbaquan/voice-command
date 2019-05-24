package com.tranbaquan.voice.command.transform.discrete_cosin;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class DiscreteCosineTransform {
    final static double LOG_FLOOR = 1e-4;

    public float[] transform(float[] data) {
        float[] d1 = new float[data.length];
        System.arraycopy(data, 0, d1, 0, data.length);
        int numberMelFilters = 40;
        int cepstrumSize = 13;
        float[][] melCosine = computeMelCosine(cepstrumSize, numberMelFilters);
        for (int i = 0; i < d1.length; ++i) {
            d1[i] = (float) Math.log(d1[i] + LOG_FLOOR);
        }
        return applyMelCosine(d1, cepstrumSize, numberMelFilters, melCosine);
    }

    private float[][] computeMelCosine(int cepstrumSize, int numberMelFilters) {
        float[][] melCosine = new float[cepstrumSize][numberMelFilters];
        double period = (double) 2 * numberMelFilters;
        for (int i = 0; i < cepstrumSize; i++) {
            double frequency = 2 * Math.PI * i / period;
            for (int j = 0; j < numberMelFilters; j++) {
                melCosine[i][j] = (float) Math.cos(frequency * (j + 0.5));
            }
        }
        return melCosine;
    }

    private float[] applyMelCosine(float[] melSpectrum, int cepstrumSize, int numberMelFilters, float[][] melCosine) {
        // create the cepstrum
        float[] cepstrum = new float[cepstrumSize];
        float beta = 0.5f;
        // apply the melcosine filter
        for (int i = 0; i < cepstrum.length; i++) {
            if (numberMelFilters > 0) {
                float[] melCosineI = melCosine[i];
                int j = 0;
                cepstrum[i] += (beta * melSpectrum[j] * melCosineI[j]);
                for (j = 1; j < numberMelFilters; j++) {
                    cepstrum[i] += (melSpectrum[j] * melCosineI[j]);
                }
                cepstrum[i] /= numberMelFilters;
            }
        }

        return cepstrum;
    }
}
