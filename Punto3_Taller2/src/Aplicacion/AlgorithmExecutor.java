package Aplicacion;

import Dominio.ScientificAlgorithm;

public class AlgorithmExecutor {
    public double runAlgorithm(ScientificAlgorithm algorithm, int threads) {
        return algorithm.execute(threads);
    }
}