package com.tranbaquan.voice.command.filter.frame;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

public class FramingFunction {
    /**
     * The property for window size in milliseconds, which has a default value of 25f.
     */
    private float windowSizeInMs;
    /**
     * The property for window shift in milliseconds, which has a default value of 10f.
     */
    private float windowShiftInMs;

    public FramingFunction() {
        this.windowSizeInMs = 25f;
        this.windowShiftInMs = 10f;
    }

    /**
     * splits audio to short times frame when we can say it don't change
     * @param data audio data
     * @param sampleRate audio frequency
     * @return frames of audio
     */
    public float[][] framing(float[] data, float sampleRate) {
//        int frameSize = getFrameSize(sampleRate);
//        int frameShift = getFrameShift(sampleRate);
        int frameSize = 512;
        int frameShift = 170;
        int frameCount = (data.length - frameSize) / frameShift;
        frameCount += (data.length % frameSize) == 0 ? 0 : 1;
        float[][] output = new float[frameCount][frameSize];
        for (int i = 0; i < output.length - 1; i++) {
            for (int j = 0; j < output[0].length; j++) {
                output[i][j] = data[i * frameShift + j];
            }
        }

        int i = output.length - 1;
        for (int j = 0; j < frameSize; j++) {
            if ((i * frameShift + j) >= data.length) break;
            output[i][j] = data[i * frameShift + j];
        }
        return output;
    }

    /**
     * get frame size
     *
     * @param sampleRate audio frequency
     * @return number of samples in window
     */
    private int getFrameSize(float sampleRate) {
        return (int) (windowSizeInMs / 1000 * sampleRate * 2);
    }

    /**
     * get frame shift
     *
     * @param sampleRate audio frequency
     * @return number of samples in shift window
     */
    private int getFrameShift(float sampleRate) {
        return (int) (windowShiftInMs / 1000 * sampleRate * 2);
    }
}
