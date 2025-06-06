package Aplicacion;

import Dominio.Entities.FiguraGeometrica;
import Dominio.Repositories.IRepositorioFiguras;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioOrdenarFiguras<T extends FiguraGeometrica> {

    private final IRepositorioFiguras<T> repository;

    public ServicioOrdenarFiguras(IRepositorioFiguras<T> repository) {
        this.repository = repository;
    }

    public List<T> ordenar() {
        return repository.obtenerTodas().stream()
                .sorted(Comparator.comparingDouble(FiguraGeometrica::calcularArea).reversed())
                .collect(Collectors.toList());
    }
}