package Dominio.Entities;

public class Circulo implements FiguraGeometrica {
    private final double radio;

    public Circulo(double radio) {
        this.radio = radio;
    }

    @Override
    public double calcularArea() {
        return Math.PI * radio * radio;
    }

    @Override
    public String getNombre() {
        return "Círculo (radio=" + radio + ")";
    }
}