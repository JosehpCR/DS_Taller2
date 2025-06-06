package Infraestructura;

import Dominio.Repositories.IRepositorioFiguras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RepositorioFigurasMemoria<T> implements IRepositorioFiguras<T> {

    private final List<T> datos = new ArrayList<>();

    @Override
    public void agregar(T figura) {
        datos.add(figura);
    }

    @Override
    public List<T> obtenerTodas() {
        return Collections.unmodifiableList(datos);
    }
}