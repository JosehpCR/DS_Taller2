package org.punto1a.Infraestructura;

import org.punto1a.Dominio.Curso;
import org.punto1a.Dominio.Estudiante;
import org.punto1a.Dominio.Nota;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioPersistencia implements IServicioPersistencia {
    private String archivo;

    public ServicioPersistencia(String archivo) {
        this.archivo = archivo;
    }




    @Override
    public void guardarCursos(List<Curso> cursos) throws Exception {
        // Abrimos el archivo para escritura (sobrescribe todo)
        try (PrintWriter out = new PrintWriter(new FileWriter(archivo))) {
            for (Curso c : cursos) {
                // --- LÍNEA DE CURSO ---
                // Usamos c.getTipoNota().name() en lugar de c.tipoNotaProperty().toString()
                // y eliminamos espacios innecesarios alrededor del '|'
                String lineaCurso = "CURSO|"
                        + c.getNombreCurso().trim()
                        + "|"
                        + c.getTipoNota().name();
                out.println(lineaCurso);

                // --- LÍNEAS DE ESTUDIANTES ---
                for (Estudiante e : c.getEstudiantes()) {
                    // Si el estudiante tiene nota, obtenemos su valor, else cadena vacía
                    String notaTexto = "";
                    if (e.getNota() != null && e.getNota().getValor() != null) {
                        notaTexto = e.getNota().getValor().toString();
                    }
                    String lineaEst = "ESTUDIANTE|"
                            + e.getNombre().trim()
                            + "|"
                            + notaTexto.trim();
                    out.println(lineaEst);
                }
            }
        }
    }
    /*@Override
    public void guardarCursos(List<Curso> cursos) throws Exception{
        try (PrintWriter out = new PrintWriter(new FileWriter(archivo))) {
            for (Curso c : cursos) {
                out.println("CURSO| " + c.getNombreCurso() + "|" + c.tipoNotaProperty());
                for(Estudiante e : c.getEstudiantes()){
                    String nota = (e.getNota() != null) ? e.getNota().getValor().toString() : "";
                    out.println("ESTUDIANTE| " + e.getNombre() + "|"+nota);
                }
            }
        }
    }

     */


    @Override
    public List<Curso> cargarCursos() throws Exception {
        List<Curso> cursos = new ArrayList<>();
        File file = new File(archivo);
        if (!file.exists()) {
            return cursos; // Si no existe el archivo, devolvemos lista vacía
        }

        try (BufferedReader in = new BufferedReader(new FileReader(archivo))) {
            String linea;
            Curso actual = null;

            while ((linea = in.readLine()) != null) {
                // Particionamos por '|' y aplicamos trim() a cada parte
                String[] partes = linea.split("\\|");
                if (partes.length < 2) {
                    continue; // Línea mal formada; la ignoramos
                }
                String tipoRegistro = partes[0].trim();

                if (tipoRegistro.equals("CURSO")) {
                    // Esperamos al menos 3 partes: [0]="CURSO", [1]=nombre, [2]=tipoNota
                    if (partes.length < 3) {
                        continue; // No podemos crear curso sin tipo de nota
                    }
                    String nombreCurso = partes[1].trim();
                    String tipoNotaStr = partes[2].trim().toUpperCase();
                    // valueOf funcionará siempre que tipoNotaStr sea "CUANTITATIVA" o "CUALITATIVA"
                    Curso.TipoNota tipo = Curso.TipoNota.valueOf(tipoNotaStr);
                    actual = new Curso(nombreCurso, tipo);
                    cursos.add(actual);

                } else if (tipoRegistro.equals("ESTUDIANTE") && actual != null) {
                    // Esperamos al menos 2 partes: [0]="ESTUDIANTE", [1]=nombre
                    // [2] (opcional) = valor de nota
                    String nombreEst = partes[1].trim();
                    Estudiante e = new Estudiante(nombreEst);

                    // Si hay un valor de nota en partes[2], lo asignamos
                    if (partes.length >= 3) {
                        String notaStr = partes[2].trim();
                        if (!notaStr.isEmpty()) {
                            e.setNota(new Nota<>(notaStr));
                        }
                    }
                    actual.agregarEstudiante(e);
                }
            }
        }
        return cursos;
    }

    /*
    @Override
    public List<Curso> cargarCursos()  throws Exception{
        List<Curso> cursos = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(archivo))){
            String linea;
            Curso actual = null;
            while ((linea = in.readLine()) != null) {
                String[] partes = linea.split("\\|");
                if (partes[0].equals("CURSO")) {
                    Curso.TipoNota tipo = Curso.TipoNota.valueOf(partes[2].toUpperCase());
                    actual = new Curso(partes[1], tipo);
                    cursos.add(actual);
                } else if (partes[0].equals("ESTUDIANTE") && actual != null) {
                    Estudiante e = new Estudiante(partes[1]);
                    if (!partes[2].isEmpty()) e.setNota(new Nota(partes[2]));
                    actual.agregarEstudiante(e);
                }
            }
        }
        return cursos;
    }

     */
    public void imprimirArchivo() {
        File file = new File(archivo);
        if (!file.exists()) {
            System.out.println("El archivo de datos no existe.");
            return;
        }
        System.out.println("\n--- CONTENIDO DEL ARCHIVO " + archivo + " ---");
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }
        System.out.println("--- FIN DEL ARCHIVO ---\n");
    }
}
