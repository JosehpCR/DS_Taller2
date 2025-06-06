package org.punto1a.Aplicacion;

import org.punto1a.Dominio.Curso;
import org.punto1a.Dominio.Estudiante;

import java.util.List;
import java.util.Optional;

public class ServicioEstudiante {

    public void agregarEstudiante(Curso curso, String nombreEstudiante) {
        curso.agregarEstudiante(new Estudiante(nombreEstudiante));

    }
    public Optional<Estudiante> buscarPorNombre(Curso curso, String nombreEstudiante){
        return curso.getEstudiantes().stream().filter(e -> e.getNombre().equals(nombreEstudiante)).findFirst();
    }
    public List<Estudiante> getEstudiantes(Curso curso){
        return curso.getEstudiantes();
    }
}
