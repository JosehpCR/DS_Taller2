package org.punto1a.Aplicacion;

import org.punto1a.Dominio.Curso;

import java.util.List;
import java.util.Optional;

public class ServicioCurso {

    private List<Curso> cursos;

    public ServicioCurso(List<Curso> cursos) {this.cursos = cursos;}

    public void crearCurso(String nombreCurso, Curso.TipoNota tipoNota) {
        this.cursos.add(new Curso(nombreCurso, tipoNota));

    }

    public List<Curso> getCursos() {return cursos;}

    public void setCursos(List<Curso> nuevaLista) {
        cursos.clear();
        cursos.addAll(nuevaLista);
    }

    public Optional<Curso> buscarPorNombreCurso(String nombreCurso) {
        return cursos.stream().filter(c -> c.getNombreCurso().equalsIgnoreCase(nombreCurso)).findFirst();
    }
}
