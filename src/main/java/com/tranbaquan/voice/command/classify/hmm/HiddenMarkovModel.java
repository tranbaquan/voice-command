package com.tranbaquan.voice.command.classify.hmm;

/*
 * Copyright (c) 2019.
 * @author tranbaquan
 */

import java.text.DecimalFormat;
import java.util.Arrays;

public class HiddenMarkovModel {
    private int numStates;
    private int numSymbols;
    private double[] pi;
    private double[][] a;
    private double[][] b;


    public HiddenMarkovModel(int numStates, int numSymbols) {
        this.numStates = numStates;
        this.numSymbols = numSymbols;
        this.pi = new double[numStates];
        this.a = new double[numStates][numStates];
        this.b = new double[numStates][numSymbols];
        initialize();
    }

    public void initialize() {
        pi[0] = 0.1;
        pi[1] = 0.3;
        pi[2] = 0.4;
        pi[3] = 0.1;
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
//                forward[j][i] = Math.log10(forward[j][i]);
//                forward[j][i] *=  b[j][obs[i]];
            }
        }
        System.out.println(Arrays.deepToString(forward));
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

    public int[] viterbi(int[] obs) {
        double[][] viterbi = new double[numStates][obs.length];
        double max;
        for (int i = 0; i < numStates; i++) {
            viterbi[i][0] = pi[i] * b[i][obs[0]];
        }
        for (int i = 1; i < obs.length; i++) {
            for (int j = 0; j < numStates; j++) {
                for (int k = 0; k < numStates; k++) {
                    max = viterbi[k][i - 1] * a[k][j] * b[j][obs[i]];
                    if (max > viterbi[j][i]) {
                        viterbi[j][i] = max;
                    }
                }
            }
        }
        int[] path = new int[obs.length];
        int index = 0;
        max = 0;
        for (int i = 0; i < viterbi.length; i++) {
            for (int j = 0; j < viterbi[i].length; j++) {
                if (viterbi[i][j] > max) {
                    max = viterbi[i][j];
                    index = j;
                }
            }
            path[i] = index;
        }
        return path;
    }


    public void train(int[] observations, int steps) {
        int T = observations.length;
        double[][] forward;
        double[][] backward;

        double pi1[] = new double[numStates];
        double a1[][] = new double[numStates][numStates];
        double b1[][] = new double[numStates][numSymbols];

        for (int s = 0; s < steps; s++) {
            // calculation of Forward- und Backward Variables from the current model
            forward = forward(observations);
            backward = backward(observations);

            // re-estimation of initial state probabilities
            for (int i = 0; i < numStates; i++)
                pi1[i] = gamma(i, 0, forward, backward);

            // re-estimation of transition probabilities
            for (int i = 0; i < numStates; i++) {
                for (int j = 0; j < numStates; j++) {
                    double num = 0;
                    double denom = 0;
                    for (int t = 0; t < T; t++) {
                        num += p(t, i, j, observations, forward, backward);
                        denom += gamma(i, t, forward, backward);
                    }
                    a1[i][j] = divide(num, denom);
                }
            }

            for (int i = 0; i < numStates; i++) {
                for (int k = 0; k < numSymbols; k++) {
                    double num = 0;
                    double denom = 0;

                    for (int t = 0; t < T; t++) {
                        double g = gamma(i, t, forward, backward);
                        num += g * (k == observations[t] ? 1 : 0);
                        denom += g;
                    }
                    b1[i][k] = divide(num, denom);
                }
            }
            System.arraycopy(pi1, 0, pi, 0, pi.length);
            System.arraycopy(a1, 0, a, 0, a.length);
            System.arraycopy(b1, 0, b, 0, b.length);
//            pi = pi1;
//            a = a1;
//            b = b1;
        }
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


    public double[][] forwardProc(int[] observations) {
        int T = observations.length;
        double[][] forward = new double[numStates][T];
        for (int i = 0; i < numStates; i++) {
            forward[i][0] = pi[i] * b[i][observations[0]];
        }
        for (int t = 0; t < T - 1; t++) {
            for (int j = 0; j < numStates; j++) {
                forward[j][t + 1] = 0;
                for (int i = 0; i < numStates; i++)
                    forward[j][t + 1] += (forward[i][t] * a[i][j]);
                forward[j][t + 1] *= b[j][observations[t + 1]];
            }
        }
        return forward;
    }

    public double[][] backwardProc(int[] observations) {
        int T = observations.length;
        double[][] backward = new double[numStates][T];

        /* initialization (time 0) */
        for (int i = 0; i < numStates; i++)
            backward[i][T - 1] = 1;

        /* induction */
        for (int t = T - 2; t >= 0; t--) {
            for (int i = 0; i < numStates; i++) {
                backward[i][t] = 0;
                for (int j = 0; j < numStates; j++)
                    backward[i][t] += (backward[j][t + 1] * a[i][j] * b[j][observations[t + 1]]);
            }
        }

        return backward;
    }

    public double p(int t, int i, int j, int[] o, double[][] forward, double[][] backward) {
        double num;
        if (t == o.length - 1)
            num = forward[i][t] * a[i][j];
        else
            num = forward[i][t] * a[i][j] * b[j][o[t + 1]] * backward[j][t + 1];

        double denom = 0;

        for (int k = 0; k < numStates; k++)
            denom += (forward[k][t] * backward[k][t]);

        return divide(num, denom);
    }

    public double xi(int t, int i, int j, int[] obs, double[][] forward, double[][] backward) {
        double num = 0;
        if (t == obs.length - 1)
            num = forward[i][t] * a[i][j];
        else
            num = forward[i][t] * a[i][j] * b[j][obs[t + 1]] * backward[j][t + 1];
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

        for (int j = 0; j < numStates; j++)
            denom += forward[j][t] * backward[j][t];

        return divide(num, denom);
    }

    /**
     * divides two doubles. 0 / 0 = 0!
     */
    public double divide(double n, double d) {
        if (n == 0)
            return 0;
        else
            return n / d;
    }

    public void print() {
        DecimalFormat fmt = new DecimalFormat();
        fmt.setMinimumFractionDigits(5);
        fmt.setMaximumFractionDigits(5);

        for (int i = 0; i < numStates; i++)
            System.out.println("pi(" + i + ") = " + fmt.format(pi[i]));
        System.out.println();

        for (int i = 0; i < numStates; i++) {
            for (int j = 0; j < numStates; j++)
                System.out.print("a(" + i + "," + j + ") = " +
                        fmt.format(a[i][j]) + "  ");
            System.out.println();
        }

        System.out.println();
        for (int i = 0; i < numStates; i++) {
            for (int k = 0; k < numSymbols; k++)
                System.out.print("b(" + i + "," + k + ") = " +
                        fmt.format(b[i][k]) + "  ");
            System.out.println();
        }
    }
}
