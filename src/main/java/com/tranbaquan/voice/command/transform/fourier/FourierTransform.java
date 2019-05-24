package com.tranbaquan.voice.command.transform.fourier;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import com.tranbaquan.voice.command.utils.Complex;

public abstract class FourierTransform {

    /**
     * do fourier transform
     * @param data audio data
     * @return transformed data which type is Complex array.
     */
    public Complex[] transform(float[] data) {
        int n = data.length;
        Complex[] transformed = new Complex[n];
        for (int i = 0; i < n; i++) {
            transformed[i] = new Complex(data[i]);
        }
        return transform(transformed);
    }

    /**
     * do fourier transform
     * @param data audio data is converted to complex
     * @return transformed data which type is Complex array.
     */
    public abstract Complex[] transform(Complex[] data);

    /**
     * do invert fourier transform
     * @param data audio data
     * @return invert fourier data which typy is complex array
     */
    public Complex[] invert(float[] data) {
        int n = data.length;
        Complex[] transformed = new Complex[n];
        for (int i = 0; i < n; i++) {
            transformed[i] = new Complex(data[i]);
        }
        return invert(transformed);
    }

    /**
     * do invert fourier transform
     * @param data audio data
     * @return invert fourier data which type is complex array
     */
    public abstract Complex[] invert(Complex[] data);

    /**
     * convert complex data to real number
     * @param data complex array data
     * @return float array data
     */
    public float[] toMagnitude(Complex[] data) {
        float[] result = new float[data.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (float) data[i].abs();
        }
        return result;
    }
}
