package org.punto1a.UI.Controladores;


import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.punto1a.Aplicacion.ServicioNota;
import org.punto1a.Dominio.Curso;
import org.punto1a.Dominio.Estudiante;
import org.punto1a.Infraestructura.IServicioPersistencia;

/**
 * Controlador del diálogo “Asignar/Editar Nota”. Permite:
 * - Mostrar el nombre del estudiante al que se le asignará edición de nota.
 * - Ingresar el valor de la nota (String).
 * - Guardar la nota usando ServicioNota (asigna según tipo de nota del curso).
 * - Persiste el cambio en el archivo .txt.
 * - Cierra automáticamente la ventana tras un guardado exitoso.
 */
public class ControladorNota {

    @FXML private Label lblEstudiante;   // muestra “Estudiante: {nombre}”
    @FXML private TextField txtValorNota; // para que el usuario escriba la nota
    @FXML private Button btnGuardar;     // botón “Guardar Nota”
    @FXML private Button btnCancelar;    // botón “Cancelar” (cierra sin guardar)

    // Servicios inyectados
    private ServicioNota servicioNota;
    private IServicioPersistencia persistencia;

    // Curso y Estudiante actual
    private Curso cursoActual;
    private Estudiante estudianteActual;

    /**
     * Este método lo llama ControladorEstudiante justo después de cargar el FXML.
     * Le pasamos el servicio de notas, persistencia, el curso y el estudiante.
     */
    public void setServicios(ServicioNota servicioNota,
                             IServicioPersistencia persistencia,
                             Curso cursoSeleccionado,
                             Estudiante estudianteSeleccionado) {
        this.servicioNota = servicioNota;
        this.persistencia = persistencia;
        this.cursoActual = cursoSeleccionado;
        this.estudianteActual = estudianteSeleccionado;

        lblEstudiante.setText("Estudiante: " + estudianteActual.getNombre());
        // Si el estudiante ya tiene nota, la mostramos
        if (estudianteActual.getNota() != null) {
            txtValorNota.setText(estudianteActual.getNota().getValor().toString());
        }
    }

    /**
     * Manejador del botón “Guardar Nota”. Valida y asigna la nota.
     * Si es correcta, persiste en el archivo y cierra la ventana.
     */
    @FXML
    private void onGuardarNota() {
        String valor = txtValorNota.getText().trim();
        if (valor.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Debe ingresar un valor para la nota.", ButtonType.OK);
            alerta.setHeaderText("Validación");
            alerta.showAndWait();
            return;
        }

        // Intentamos asignar la nota con el servicio (según tipo de curso)
        boolean ok = servicioNota.asignarNota(cursoActual, estudianteActual, valor);
        if (!ok) {
            Alert error = new Alert(Alert.AlertType.ERROR,
                    "El valor ingresado no es válido para el tipo de nota de este curso.", ButtonType.OK);
            error.setHeaderText("Valor de Nota Inválido");
            error.showAndWait();
            return;
        }

        // Si se asignó correctamente, guardamos todos los cursos (y sus estudiantes/notas) en el .txt
        try {
            persistencia.guardarCursos(cursoActual.getEstudiantes().isEmpty()
                    ? persistencia.cargarCursos()  // si faltara recargar lista, se podría recargar
                    : persistencia.cargarCursos());
            // (Más sencillo: en tu lógica, `cursoActual` ya pertenece a la lista en memoria
            //  de ServicioCurso; al persistir, sobreescribes con la lista actualizada.)
        } catch (Exception e) {
            // Si hay error al persistir, notificamos y no cerramos la ventana
            Alert err = new Alert(Alert.AlertType.ERROR, "Error al guardar en archivo: " + e.getMessage(), ButtonType.OK);
            err.setHeaderText("Error de Persistencia");
            err.showAndWait();
            return;
        }

        // Cerramos el diálogo al guardarse exitosamente
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }

    /**
     * Manejador del botón “Cancelar”. Cierra la ventana sin guardar cambios.
     */
    @FXML
    private void onCancelar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
