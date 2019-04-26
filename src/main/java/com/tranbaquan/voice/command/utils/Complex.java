package com.tranbaquan.voice.command.utils;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import java.util.Objects;

public class Complex {

    /**
     * the real part
     */
    private final double re;
    /**
     * the imaginary part
     */
    private final double im;

    /**
     * create a new object with the given real and imaginary parts
     * @param real
     * @param imag
     */
    public Complex(double real, double imag) {
        re = real;
        im = imag;
    }

    /**
     * create a new object with the given real parts, imaginary part equals 0
     * @param real
     */
    public Complex(double real) {
        re = real;
        im = 0;
    }

    // return a string representation of the invoking Complex object
    public String toString() {
        if (im == 0) return re + "";
        if (re == 0) return im + "i";
        if (im <  0) return re + " - " + (-im) + "i";
        return re + " + " + im + "i";
    }

    /**
     * calculate abs/modulus/magnitude
     * @return abs/modulus/magnitude
     */
    public double abs() {
        return Math.hypot(re, im);
    }

    /**
     * calculate angle/phase/argument, normalized to be between -pi and pi
     * @return angle/phase/argument, normalized to be between -pi and pi
     */
    public double phase() {
        return Math.atan2(im, re);
    }

    // return

    /**
     * do plus this with another complex
     * @param b another complex number
     * @return a new Complex object whose value is (this + b)
     */
    public Complex plus(Complex b) {
        Complex a = this;             // invoking object
        double real = a.re + b.re;
        double imag = a.im + b.im;
        return new Complex(real, imag);
    }

    /**
     * do minus this with another complex
     * @param b another complex number
     * @return a new Complex object whose value is (this - b)
     */
    public Complex minus(Complex b) {
        Complex a = this;
        double real = a.re - b.re;
        double imag = a.im - b.im;
        return new Complex(real, imag);
    }

    /**
     * do multiply this with another complex
     * @param b another complex number
     * @return a new Complex object whose value is (this * b)
     */
    public Complex times(Complex b) {
        Complex a = this;
        double real = a.re * b.re - a.im * b.im;
        double imag = a.re * b.im + a.im * b.re;
        return new Complex(real, imag);
    }

    /**
     * do multiply this with a number
     * @param alpha a number
     * @return a new Complex object whose value is (this * alpha)
     */
    public Complex scale(double alpha) {
        return new Complex(alpha * re, alpha * im);
    }

    /**
     * do conjugate
     * @return a new Complex object whose value is the conjugate of this
     */
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    /**
     * do reciprocal
     * @return a new Complex object whose value is the reciprocal of this
     */
    public Complex reciprocal() {
        double scale = re*re + im*im;
        return new Complex(re / scale, -im / scale);
    }

    public double getRe() {
        return re;
    }

    public double getIm() {
        return im;
    }

    /**
     * do divides with another complex
     * @param b another complex
     * @return a new Complex object whose value is (this / b)
     */
    public Complex divides(Complex b) {
        Complex a = this;
        return a.times(b.reciprocal());
    }

    /**
     * do exp
     * @return a new Complex object whose value is the complex exponential of this
     */
    public Complex exp() {
        return new Complex(Math.exp(re) * Math.cos(im), Math.exp(re) * Math.sin(im));
    }

    /**
     * sin(this)
     * @return a new Complex object whose value is the complex sine of this
     */
    public Complex sin() {
        return new Complex(Math.sin(re) * Math.cosh(im), Math.cos(re) * Math.sinh(im));
    }

    /**
     * cos(this)
     * @return a new Complex object whose value is the complex cosine of this
     */
    public Complex cos() {
        return new Complex(Math.cos(re) * Math.cosh(im), -Math.sin(re) * Math.sinh(im));
    }

    /**
     * tan(this)
     * @return a new Complex object whose value is the complex tangent of this
     */
    public Complex tan() {
        return sin().divides(cos());
    }

    /**
     * do a+b
     * @param a
     * @param b
     * @return a+b
     */
    public static Complex plus(Complex a, Complex b) {
        double real = a.re + b.re;
        double imag = a.im + b.im;
        Complex sum = new Complex(real, imag);
        return sum;
    }

    public boolean equals(Object x) {
        if (x == null) return false;
        if (this.getClass() != x.getClass()) return false;
        Complex that = (Complex) x;
        return (this.re == that.re) && (this.im == that.im);
    }

    public int hashCode() {
        return Objects.hash(re, im);
    }
}
