package Infraestructura;

import Dominio.ScientificAlgorithm;

import java.util.Random;

public class MonteCarloPiAlgorithm implements ScientificAlgorithm {
    private long totalPoints;

    public MonteCarloPiAlgorithm(long totalPoints) {
        this.totalPoints = totalPoints;
    }

    @Override
    public double execute(int threads) {
        long pointsPerThread = totalPoints / threads;
        long[] insideCounts = new long[threads];
        Thread[] workers = new Thread[threads];

        for (int i = 0; i < threads; i++) {
            final int index = i;
            workers[i] = new Thread(new Runnable() {
                @Override
                public void run() {
                    long inside = 0;
                    Random rnd = new Random();
                    for (long j = 0; j < pointsPerThread; j++) {
                        double x = rnd.nextDouble();
                        double y = rnd.nextDouble();
                        if (x * x + y * y <= 1.0) {
                            inside++;
                        }
                    }
                    insideCounts[index] = inside;
                }
            });
            workers[i].start();
        }

        for (int i = 0; i < threads; i++) {
            try {
                workers[i].join();
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + e.getMessage());
            }
        }

        long totalInside = 0;
        for (long count : insideCounts) {
            totalInside += count;
        }

        return (4.0 * totalInside) / totalPoints;
    }
}