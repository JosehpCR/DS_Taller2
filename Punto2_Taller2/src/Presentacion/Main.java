package Presentacion;

import Aplicacion.ServicioOrdenarFiguras;
import Dominio.Entities.Circulo;
import Dominio.Entities.FiguraGeometrica;
import Dominio.Entities.Rectangulo;
import Dominio.Repositories.IRepositorioFiguras;
import Infraestructura.RepositorioFigurasMemoria;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        IRepositorioFiguras<FiguraGeometrica> repo = new RepositorioFigurasMemoria<>();
        ServicioOrdenarFiguras<FiguraGeometrica> servicio = new ServicioOrdenarFiguras<>(repo);

        // Agregar algunas figuras
        repo.agregar(new Circulo(2.0));
        repo.agregar(new Rectangulo(3.0, 4.0));
        repo.agregar(new Circulo(1.0));
        repo.agregar(new Rectangulo(2.5, 2.5));

        // Ordenar y obtener lista
        List<FiguraGeometrica> resultado = servicio.ordenar();

        // Mostrar en consola
        System.out.println("Figuras ordenadas (área descendente):");
        for (FiguraGeometrica f : resultado) {
            System.out.printf(" - %s: área = %.2f%n", f.getNombre(), f.calcularArea());
        }
    }
}