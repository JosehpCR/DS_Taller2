package org.punto1a.Dominio;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Curso {


    public enum TipoNota {CUANTITATIVA, CUALITATIVA}


    private final StringProperty nombreCurso = new SimpleStringProperty();
    private final ObjectProperty<TipoNota> tipoNota = new SimpleObjectProperty<>();

    private final List<Estudiante> estudiantes = new ArrayList<>();

    public Curso() {

    }

    public Curso(String nombreCurso, TipoNota tipoNota) {
        this.nombreCurso.set(nombreCurso);
        this.tipoNota.set(tipoNota);
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso.set(nombreCurso);
    }
    public String getNombreCurso() {
        return nombreCurso.get();
    }

    public StringProperty nombreCursoProperty() {
        return this.nombreCurso;
    }

    public void setTipoNota(TipoNota tipoNota) {
        this.tipoNota.set(tipoNota);
    }
    public ObjectProperty<TipoNota> tipoNotaProperty() {
        return this.tipoNota;
    }
    public TipoNota getTipoNota() {
        return tipoNota.get();
    }

    public List<Estudiante> getEstudiantes() {return estudiantes;}

    public void agregarEstudiante(Estudiante e) {estudiantes.add(e);}
}
