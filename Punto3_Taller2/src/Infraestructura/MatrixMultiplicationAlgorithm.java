package Infraestructura;

import Dominio.ScientificAlgorithm;

public class MatrixMultiplicationAlgorithm implements ScientificAlgorithm {
    private double[][] matrixA;
    private double[][] matrixB;

    public MatrixMultiplicationAlgorithm(double[][] matrixA, double[][] matrixB) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
    }

    @Override
    public double execute(int threads) {
        int n = matrixA.length;
        double[][] result = new double[n][n];
        Thread[] workers = new Thread[threads];
        int rowsPerThread = n / threads;

        for (int t = 0; t < threads; t++) {
            final int startRow = t * rowsPerThread;
            final int endRow = (t == threads - 1) ? n : startRow + rowsPerThread;
            workers[t] = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = startRow; i < endRow; i++) {
                        for (int j = 0; j < n; j++) {
                            double sum = 0;
                            for (int k = 0; k < n; k++) {
                                sum += matrixA[i][k] * matrixB[k][j];
                            }
                            result[i][j] = sum;
                        }
                    }
                }
            });
            workers[t].start();
        }

        for (int i = 0; i < threads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        double trace = 0;
        for (int i = 0; i < n; i++) {
            trace += result[i][i];
        }

        return trace;
    }
}