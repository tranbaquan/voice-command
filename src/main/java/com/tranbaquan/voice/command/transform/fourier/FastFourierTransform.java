package com.tranbaquan.voice.command.transform.fourier;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import com.tranbaquan.voice.command.utils.Complex;

public class FastFourierTransform extends FourierTransform {

    @Override
    public Complex[] transform(Complex[] data) {
        return fft(data, false, 9);
    }

    @Override
    public Complex[] invert(Complex[] data) {
        return fft(data, true, 9);
    }

    private int inverseNBit(int n, int index) {
        int i = 0;
        int j = n - 1;
        for (; i <= j; i++, j--) {
            if ((index >> i & 1) != (index >> j & 1)) {
                index ^= 1 << i;
                index ^= 1 << j;
            }
        }
        return index;
    }

    private Complex[] fft(Complex[] data, boolean isInvert, int exponent) {
        int n = data.length;
        Complex[] transformed = new Complex[data.length];
        System.arraycopy(data, 0, transformed, 0, data.length);
        int j;
        for (int i = 0; i < n; i++) {
            // FIXME need to change
            j = inverseNBit(exponent, i);
            if (i < j) {
                transformed[i] = data[j];
                transformed[j] = data[i];
            }
        }
        Complex[] wn = new Complex[n];
        Complex[] next = new Complex[n];

        for (int step = 1; step < n; step <<= 1) {
            double angle = Math.PI / step;
            if(isInvert) angle = -angle;
            Complex w = new Complex(1);
            Complex wi = new Complex(Math.cos(angle), Math.sin(angle));
            for (int i = 0; i < step; i++) {
                wn[i] = w;
                w = w.times(wi);
            }
            int startEven = 0;
            int startOdd = step;
            while (startEven < n) {
                for (int i = 0; i < step; i++) {
                    next[startEven + i] = transformed[startEven + i].plus(wn[i].times(transformed[startOdd + i]));
                    next[startEven + i + step] = transformed[startEven + i].minus(wn[i].times(transformed[startOdd + i]));
                }
                startEven += 2 * step;
                startOdd = startEven + step;
            }
            System.arraycopy(next, 0, transformed,0, n);
        }
        if(isInvert) {
            Complex x = new Complex(n);
            for (int i = 0; i < n; i++) {
                transformed[i] = transformed[i].divides(x);
            }
        }
        return transformed;
    }
}
