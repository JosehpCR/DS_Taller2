package Dominio.Repositories;


import java.util.List;

public interface IRepositorioFiguras<T> {
    void agregar(T figura);
    List<T> obtenerTodas();
}