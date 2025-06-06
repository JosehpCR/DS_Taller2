package org.punto1a.Dominio;

/**
 * Clase genérica que almacena un valor de nota (puede ser número, texto, etc.).
 */
public class Nota<T> {

    private T valor;

    /** Constructor sin-argumentos para JavaFX o frameworks que lo requieran. */
    public Nota() {
        // valor queda en null
    }

    public Nota(T valor) {
        this.valor = valor;
    }

    public T getValor() {
        return valor;
    }

    public void setValor(T valor) {
        this.valor = valor;
    }

    /**
     * Sobrescribimos toString() para que al mostrar esta nota en un TableView
     * se vea directamente valor.toString() (o cadena vacía si valor es null).
     */
    @Override
    public String toString() {
        return (valor != null) ? valor.toString() : "";
    }
}
