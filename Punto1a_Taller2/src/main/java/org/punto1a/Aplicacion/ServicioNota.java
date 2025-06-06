package org.punto1a.Aplicacion;

import org.punto1a.Dominio.Curso;
import org.punto1a.Dominio.Estudiante;
import org.punto1a.Dominio.Nota;

public class ServicioNota {
    public boolean asignarNota(Curso curso, Estudiante estudiante, String valor) {
        Curso.TipoNota tipoNota = curso.tipoNotaProperty().get();
        if(tipoNota == Curso.TipoNota.CUANTITATIVA){
            try {
                double v = Double.parseDouble(valor);
                if(v < 0 || v > 5) return false;
                estudiante.setNota(new Nota<>(v));
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        else{
            String texto = valor.trim();
            if (texto.equalsIgnoreCase("Aprobó")
                    || texto.equalsIgnoreCase("Reprobó")
                    || texto.equalsIgnoreCase("Pendiente")) {
                // Almacenamos la nota como texto -> usar Nota<String>
                estudiante.setNota(new Nota<>(texto));
                return true;
            }
            return false;
        }
    }
}
