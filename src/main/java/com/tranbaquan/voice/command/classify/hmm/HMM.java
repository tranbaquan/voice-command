package com.tranbaquan.voice.command.classify.hmm;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import com.tranbaquan.voice.command.trainning.Dictionary;

import java.io.Serializable;
import java.text.DecimalFormat;

public class HMM implements Serializable {
    private static final long serialVersionUID = 1L;

    private Dictionary dictionary;
    private int numStates;
    private int numSymbols;
    private double[] pi;
    private double[][] a;
    private double[][] b;

    public HMM(int numStates, int numSymbols) {
        this.numStates = numStates;
        this.numSymbols = numSymbols;
        this.pi = new double[numStates];
        this.a = new double[numStates][numStates];
        this.b = new double[numStates][numSymbols];
    }

    public int getNumStates() {
        return numStates;
    }

    public void setNumStates(int numStates) {
        this.numStates = numStates;
    }

    public int getNumSymbols() {
        return numSymbols;
    }

    public void setNumSymbols(int numSymbols) {
        this.numSymbols = numSymbols;
    }

    public double[] getPi() {
        return pi;
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public String toString() {
        DecimalFormat fmt = new DecimalFormat();
        fmt.setMinimumFractionDigits(5);
        fmt.setMaximumFractionDigits(5);

        String output = "";
        for (int i = 0; i < numStates; i++) {
            output += "pi(" + i + ") = " + fmt.format(pi[i]) + "\n";
        }
        output += "\n";

        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++) {
                output += "a(" + i + "," + j + ") = " + fmt.format(a[i][j]) + "  ";
            }
            output += "\n";
        }
        output += "\n";

        for (int i = 0; i < numStates; i++) {
            for (int k = 0; k < numSymbols; k++) {
                output += "b(" + i + "," + k + ") = " + fmt.format(b[i][k]) + "  ";
            }
            output += "\n";
        }
        return output;
    }

    public void setPi(double[] pi) {
        this.pi = pi;
    }

    public double[][] getA() {
        return a;
    }

    public void setA(double[][] a) {
        this.a = a;
    }

    public double[][] getB() {
        return b;
    }

    public void setB(double[][] b) {
        this.b = b;
    }

    public void initialize() {
        pi[0] = 0;
        pi[1] = 0.2;
        pi[2] = 0.5;
        pi[3] = 0.2;
        pi[4] = 0.1;
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i].length; j++) {
                a[i][j] = 1.0 / a[i].length;
            }
        }
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[i].length; j++) {
                b[i][j] = 1.0 / b[i].length;
            }
        }
    }

    public double prob(int[] obs) {
        double[][] forward = forward(obs);
        double[][] backward = backward(obs);
        double sum = 0;
        for (int i = 0; i < forward.length; i++) {
            for (int j = 0; j < forward[i].length; j++) {
                sum += forward[i][j] + backward[i][j];
            }
        }
        return sum;
    }

    //  P(O|λ) = Xm(i= 0: N) αT(i)
    public double[][] forward(int[] obs) {
        double[][] forward = new double[numStates][obs.length];
        for (int i = 0; i < numStates; i++) {
            forward[i][0] = pi[i] * b[i][obs[0]];
        }
        for (int i = 1; i < obs.length; i++) {
            for (int j = 0; j < numStates; j++) {
                for (int k = 0; k < numStates; k++) {
                    forward[j][i] += forward[k][i - 1] * a[k][j] * b[j][obs[i]];
                }
            }
        }
        return forward;
    }

    public double[][] backward(int[] obs) {
        double[][] backward = new double[numStates][obs.length];
        for (int i = 0; i < numStates; i++) {
            backward[i][obs.length - 1] = 1;
        }

        for (int i = obs.length - 2; i >= 0; i--) {
            for (int j = 0; j < numStates; j++) {
                for (int k = 0; k < numStates; k++) {
                    backward[j][i] += a[j][k] * b[k][obs[j + 1]] * backward[k][i + 1];
                }
            }
        }
        return backward;
    }

    public void reestimate(int[] obs, int step) {
        double[][] forward;
        double[][] backward;
        double num;
        double denom;
        for (int s = 0; s < step; s++) {
            forward = forward(obs);
            backward = backward(obs);
            for (int i = 0; i < numStates; i++) {
                pi[i] = gamma(i, 0, forward, backward);
            }
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    num = 0;
                    denom = 0;
                    for (int k = 0; k < obs.length - 1; k++) {
                        num += xi(k, i, j, obs, forward, backward);
                        denom += gamma(j, k, forward, backward);
                    }
                    a[i][j] = divide(num, denom);
                }
            }
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numSymbols; j++) {
                    num = 0;
                    denom = 0;
                    for (int k = 0; k < obs.length; k++) {
                        num += gamma(i, k, forward, backward) * (j == obs[k] ? 1 : 0);
                        denom += gamma(i, k, forward, backward);
                    }
                    b[i][j] = divide(num, denom);
                }
            }

        }
    }

    public double p(int t, int i, int j, int[] o, double[][] forward, double[][] backward) {
        double num;
        if (t == o.length - 1) {
            num = forward[i][t] * a[i][j];
        } else {
            num = forward[i][t] * a[i][j] * b[j][o[t + 1]] * backward[j][t + 1];
        }
        double denom = 0;

        for (int k = 0; k < numStates; k++) {
            denom += (forward[k][t] * backward[k][t]);
        }

        return divide(num, denom);
    }

    public double xi(int t, int i, int j, int[] obs, double[][] forward, double[][] backward) {
        double num = 0;
        if (t == obs.length - 1) {
            num = forward[i][t] * a[i][j];
        } else {
            num = forward[i][t] * a[i][j] * b[j][obs[t + 1]] * backward[j][t + 1];
        }
        double denom = 0;
        for (int k = 0; k < numStates; k++) {
            for (int l = 0; l < numStates; l++) {
                denom += forward[k][t] * a[k][l] * b[l][obs[t + 1]] * backward[l][t + 1];
            }
        }
        return divide(num, denom);
    }

    public double gamma(int i, int t, double[][] forward, double[][] backward) {
        double num = forward[i][t] * backward[i][t];
        double denom = 0;

        for (int j = 0; j < numStates; j++) {
            denom += forward[j][t] * backward[j][t];
        }

        return divide(num, denom);
    }

    /**
     * divides two doubles. 0 / 0 = 0!
     */
    public double divide(double n, double d) {
        if (n == 0) {
            return 0;
        } else {
            return n / d;
        }
    }
}
