package com.tranbaquan.voice.command.transform.filter;

public abstract class WindowFunction {
    /**
     * The property for window size in milliseconds, which has a default value of 25f.
     */
    private float windowSizeInMs;
    /**
     * The property for window shift in milliseconds, which has a default value of 10f.
     */
    private float windowShiftInMs;

    protected WindowFunction() {
        this.windowSizeInMs = 25f;
        this.windowShiftInMs = 10f;
    }

    /**
     * Doing windowing function
     * @param data audio data
     * @return windowed audio data
     */
    public abstract float[] windowing(float[] data, float sampleRate);

    /**
     * get window size
     * @param sampleRate audio frequency
     * @return number of samples in window
     */
    protected int getWindowSize(float sampleRate) {
        return (int) (windowSizeInMs / 1000 * sampleRate * 2);
    }

    /**
     * get window shift
     * @param sampleRate audio frequency
     * @return number of samples in shift window
     */
    protected int getWindowShift(float sampleRate) {
        return (int) (windowShiftInMs / 1000 * sampleRate * 2);
    }
}
