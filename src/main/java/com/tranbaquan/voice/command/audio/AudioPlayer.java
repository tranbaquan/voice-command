package com.tranbaquan.voice.command.audio;

import com.sun.media.sound.AudioFloatConverter;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/*
 * Copyright (c) $year.
 * @author tranbaquan
 */
public class AudioPlayer {
    /**
     * size of the byte buffer used to read/write the audio stream
     */
    private static final int BUFFER_SIZE = 4096;
    private AudioFormat format;
    private DataLine.Info info;
    private SourceDataLine line;
    /**
     * audio floats amplitude
     */
    private float[] floatsData;
    /**
     * audio bytes amplitude
     */
    private byte[] bytesData;

    /**
     * constructor
     * @param audioPath audio path file
     */
    public AudioPlayer(String audioPath) {
        initFormat();
        loadAudio(audioPath);
    }

    /**
     * constructor
     * @param bytesData audio bytes data
     */
    public AudioPlayer(byte[] bytesData) {
        initFormat();
        loadAudio(bytesData);
    }

    /**
     * constructor
     * @param floatsData audio floats data
     */
    public AudioPlayer(float[] floatsData) {
        initFormat();
        loadAudio(floatsData);
    }

    public AudioFormat getFormat() {
        return format;
    }

    public void setFormat(AudioFormat format) {
        this.format = format;
    }

    public float[] getFloatsData() {
        return floatsData;
    }

    public void setFloatsData(float[] floatsData) {
        this.floatsData = floatsData;
    }

    /**
     * init default audio format, data line info and source data line info
     * default: Encoding.PCM_SIGNED, sample rate is 41.1kHz, sample size in bits is 16,
     * channels is 2, frame size is 4, frame rate is 41.1kHz, and big endian is false.
     */
    private void initFormat() {
        try {
            format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 41100.0f, 16, 2, 4, 41100.0f, false);
            this.info = new DataLine.Info(SourceDataLine.class, format);
            this.line = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Line is unavailable");
        }
    }

    private void loadAudio(String audioPath) {
        try {
            File audio = new File(audioPath);
            AudioInputStream stream = AudioSystem.getAudioInputStream(audio);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            stream.close();
            this.bytesData = byteArrayOutputStream.toByteArray();
            this.floatsData = toFloatArray(this.bytesData);
            byteArrayOutputStream.close();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            System.out.println("File is unsupported");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Stream error");
        }
    }

    private void loadAudio(byte[] data) {
        this.bytesData = data;
        this.floatsData = toFloatArray(data);
    }

    private void loadAudio(float[] data) {
        this.floatsData = data;
        this.bytesData = toByteArray(data);
    }

    /**
     * play audio
     */
    public void play() {
        play(bytesData);
    }

    /**
     * play audio
     * @param data audio floats data
     */
    public void play(float[] data) {
        play(toByteArray(data));
    }

    /**
     * play audio
     * @param data audio bytes data
     */
    public void play(byte[] data) {
        try {
            line.open(format);
            line.start();
            line.write(data, 0, data.length);
            line.drain();
            line.close();
        } catch (LineUnavailableException e) {
            System.out.println("Line is unavailable");
            e.printStackTrace();
        }
    }

    /**
     * write audio to file
     * @param data audio bytes data
     * @param filePath file output path
     */
    public void writeToFile(byte[] data, String filePath) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            AudioInputStream stream = new AudioInputStream(byteArrayInputStream, format, data.length);
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, new File(filePath));
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * write audio to file
     * @param data audio floats data
     * @param filePath file output path
     */
    public void writeToFile(float[] data, String filePath) {
        writeToFile(toByteArray(data), filePath);
    }

    private byte[] toByteArray(float[] data) {
        AudioFloatConverter converter = AudioFloatConverter.getConverter(this.format);
        byte[] bytes = new byte[data.length*2];
        converter.toByteArray(data, bytes);
        return bytes;
    }

    private float[] toFloatArray(byte[] data) {
        AudioFloatConverter converter = AudioFloatConverter.getConverter(this.format);
        float[] floats = new float[data.length/2];
        converter.toFloatArray(data, floats);
        return floats;
    }
}
