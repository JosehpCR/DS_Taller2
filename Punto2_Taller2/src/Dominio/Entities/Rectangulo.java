package Dominio.Entities;

public class Rectangulo implements FiguraGeometrica {
    private final double ancho;
    private final double alto;

    public Rectangulo(double ancho, double alto) {
        this.ancho = ancho;
        this.alto = alto;
    }

    @Override
    public double calcularArea() {
        return ancho * alto;
    }

    @Override
    public String getNombre() {
        return "Rectángulo (ancho=" + ancho + ", alto=" + alto + ")";
    }
}