package org.punto1a.Dominio;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Estudiante {

    private final StringProperty nombre = new SimpleStringProperty();
    private final ObjectProperty<Nota<?>> nota = new SimpleObjectProperty<>();


    public Estudiante() {

    }

    public Estudiante(String nombre) {
        this.nombre.set(nombre);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    // — Getters/Setters de propiedad nota —
    public Nota<?> getNota() {
        return nota.get();
    }

    public void setNota(Nota<?> nota) {
        this.nota.set(nota);
    }

    public ObjectProperty<Nota<?>> notaProperty() {
        return nota;
    }

}
