package Presentacion;

import Aplicacion.AlgorithmExecutor;
import Dominio.ScientificAlgorithm;
import Infraestructura.MatrixMultiplicationAlgorithm;
import Infraestructura.MonteCarloPiAlgorithm;

public class Main {
    public static void main(String[] args) {
        AlgorithmExecutor executor = new AlgorithmExecutor();

        ScientificAlgorithm piAlg = new MonteCarloPiAlgorithm(1_000_000_000);
        double piEstimate = executor.runAlgorithm(piAlg, 4);
        System.out.println("Estimación de π: " + piEstimate);

        int n = 10;
        double[][] a = new double[n][n];
        double[][] b = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = i + j;
            }
        }

        for (int i = 0; i < n; i++) {
            b[i][i] = 1;
        }

        ScientificAlgorithm mmAlg = new MatrixMultiplicationAlgorithm(a, b);
        double trace = executor.runAlgorithm(mmAlg, 4);
        System.out.println("Matriz A:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%6.2f ", a[i][j]);
            }
            System.out.println();
        }
        System.out.println("Matriz B:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%6.2f ", b[i][j]);
            }
            System.out.println();
        }
        System.out.println("Resultado de la multiplicación: " + trace);
    }
}