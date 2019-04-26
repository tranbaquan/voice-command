package com.tranbaquan.voice.command.filter.window;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public abstract class WindowFunction {
    /**
     * Doing windowing function
     * @param data audio data
     * @return windowed audio data
     */
    public abstract float[] windowing(float[] data);

}
