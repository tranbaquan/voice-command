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
    public abstract Complex[] transform(float[] data);

    public abstract Complex[] transform(Complex[] data);

    public abstract Complex[] invert(float[] data);

    public abstract Complex[] invert(Complex[] data);
}
