package org.punto1a.UI.Controladores;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.punto1a.Aplicacion.ServicioEstudiante;
import org.punto1a.Aplicacion.ServicioNota;
import org.punto1a.Dominio.Curso;
import org.punto1a.Dominio.Estudiante;
import org.punto1a.Infraestructura.IServicioPersistencia;

import java.io.IOException;

/**
 * Controlador de la vista de estudiantes. Permite:
 * - Mostrar todos los estudiantes de un curso.
 * - Agregar un estudiante nuevo (por nombre) al curso.
 * - Abrir una ventana para asignar/editar la nota de un estudiante seleccionado.
 * - Volver a la ventana de cursos.
 */
public class ControladorEstudiante {

    @FXML private Label lblCurso;                          // muestra nombre del curso
    @FXML private TextField txtNombreEstudiante;           // para ingresar nombre del estudiante
    @FXML private Button btnAgregar;                       // botón “Agregar estudiante”
    @FXML private Button btnGestionarNota;                 // botón “Asignar/Editar Nota”
    @FXML private Button btnVolver;                        // botón “Volver a cursos”
    @FXML private TableView<Estudiante> tblEstudiantes;    // tabla con lista de estudiantes
    @FXML private TableColumn<Estudiante, String> colNombre; // columna “Nombre”
    @FXML private TableColumn<Estudiante, String> colNota;   // columna “Nota” (texto)

    // Servicios inyectados
    private ServicioEstudiante servicioEstudiante;
    private ServicioNota servicioNota;
    private IServicioPersistencia persistencia;

    // Curso actual (al que pertenecen estos estudiantes)
    private Curso cursoActual;

    // Modelo observable para el TableView
    private final ObservableList<Estudiante> modeloEstudiantes = FXCollections.observableArrayList();

    /**
     * Este método lo llama ControladorCurso justo después de cargar el FXML.
     * Le paso el servicio de estudiantes y notas, la persistencia y el curso seleccionado.
     */
    public void setServicios(ServicioEstudiante servicioEstudiante,
                             ServicioNota servicioNota,
                             IServicioPersistencia persistencia,
                             Curso cursoSeleccionado) {
        this.servicioEstudiante = servicioEstudiante;
        this.servicioNota = servicioNota;
        this.persistencia = persistencia;
        this.cursoActual = cursoSeleccionado;

        lblCurso.setText("Curso: " + cursoActual.getNombreCurso());
        inicializarTabla();
        cargarTabla();
    }

    @FXML
    private void initialize() {
        // Columna “Nombre” vincula al property nombreProperty() de Estudiante
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        // Para la columna “Nota”, creamos un SimpleStringProperty con el valor o con cadena vacía
        colNota.setCellValueFactory(cellData -> {
            Estudiante est = cellData.getValue();
            String textoNota = (est.getNota() != null && est.getNota().getValor() != null)
                    ? est.getNota().getValor().toString()
                    : "";
            return new SimpleStringProperty(textoNota);
        });

        tblEstudiantes.setItems(modeloEstudiantes);
    }

    private void inicializarTabla() {
        // Si hiciera falta alguna configuración extra en la tabla, se haría aquí.
    }

    private void cargarTabla() {
        modeloEstudiantes.setAll(servicioEstudiante.getEstudiantes(cursoActual));
    }

    /**
     * Manejador del botón “Agregar estudiante”. Crea uno nuevo y recarga la tabla.
     */
    @FXML
    private void onAgregarEstudiante() {
        String nombre = txtNombreEstudiante.getText().trim();
        if (nombre.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "El nombre del estudiante no puede estar vacío.", ButtonType.OK);
            alerta.setHeaderText("Validación");
            alerta.showAndWait();
            return;
        }
        servicioEstudiante.agregarEstudiante(cursoActual, nombre);
        cargarTabla();
        txtNombreEstudiante.clear();
    }

    /**
     * Manejador del botón “Asignar/Editar Nota”. Abre un diálogo para ingresar la nota
     * del estudiante seleccionado.
     */
    @FXML
    private void onGestionarNota() {
        Estudiante seleccionado = tblEstudiantes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Debe seleccionar antes un estudiante.", ButtonType.OK);
            alerta.setHeaderText("Sin Selección");
            alerta.showAndWait();
            return;
        }

        try {
            // Cargar el FXML de VistaNota
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org.punto1a.UI.Vistas/VistaNota.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la ventana de nota y pasarle dependencias
            ControladorNota controladorNota = loader.getController();
            controladorNota.setServicios(
                    servicioNota,
                    persistencia,
                    cursoActual,
                    seleccionado
            );

            // Crear un Stage modal para asignar la nota
            Stage stage = new Stage();
            stage.setTitle("Asignar nota a: " + seleccionado.getNombre());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            // Al cerrar el diálogo de nota, recargamos la tabla para ver la nota actualizada
            cargarTabla();

        } catch (IOException e) {
            e.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la ventana de nota.", ButtonType.OK);
            error.setHeaderText("Error de Carga");
            error.showAndWait();
        }
    }

    /**
     * Manejador del botón “Volver a cursos”. Cierra esta ventana y retorna a la lista de cursos.
     */
    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        stage.close();
    }
}
