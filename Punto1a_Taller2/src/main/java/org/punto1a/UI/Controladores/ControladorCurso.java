package org.punto1a.UI.Controladores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.punto1a.Aplicacion.ServicioCurso;
import org.punto1a.Aplicacion.ServicioEstudiante;
import org.punto1a.Aplicacion.ServicioNota;
import org.punto1a.Dominio.Curso;
import org.punto1a.Dominio.Estudiante;
import org.punto1a.Dominio.Nota;
import org.punto1a.Infraestructura.IServicioPersistencia;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Controlador de la vista de cursos. Permite:
 * - Crear nuevos cursos (con nombre y tipo de nota).
 * - Ver la lista de cursos en un TableView.
 * - Guardar la lista completa de cursos en el archivo .txt.
 * - Abrir la ventana de gestión de estudiantes para el curso seleccionado.
 */
public class ControladorCurso {

    @FXML private TextField txtNombre;                       // para ingresar nombre del curso
    @FXML private ComboBox<Curso.TipoNota> cmbTipo;           // para seleccionar el tipo de nota
    @FXML private TableView<Curso> tblCursos;                 // tabla que muestra cursos existentes
    @FXML private TableColumn<Curso, String> colNombre;       // columna “Nombre”
    @FXML private TableColumn<Curso, Curso.TipoNota> colTipo; // columna “Tipo de Nota”

    @FXML private Button btnCrear;                            // botón “Crear curso”
    @FXML private Button btnGestionarEstudiantes;             // botón “Gestionar estudiantes”
    @FXML private Button btnGuardar;                          // botón “Guardar en archivo”
    @FXML private Button btnCargarPrueba;                      // botón "Cargar datos de prueba"

    // Servicios inyectados desde MainApp
    private ServicioCurso servicioCurso;
    private ServicioEstudiante servicioEstudiante;
    private ServicioNota servicioNota;
    private IServicioPersistencia persistencia;

    // Modelo observable para el TableView
    private final ObservableList<Curso> modeloCursos = FXCollections.observableArrayList();

    /**
     * Este método es llamado desde MainApp justo después de cargar el FXML.
     * Inyecta los servicios y prepara la tabla con los datos iniciales.
     */
    public void setServicios(ServicioCurso servicioCurso,
                             ServicioEstudiante servicioEstudiante,
                             ServicioNota servicioNota,
                             IServicioPersistencia persistencia) {
        this.servicioCurso = servicioCurso;
        this.servicioEstudiante = servicioEstudiante;
        this.servicioNota = servicioNota;
        this.persistencia = persistencia;
        inicializarTabla();
        cargarTabla();
    }

    /**
     * Inicializa las columnas del TableView vinculándolas a las propiedades de Curso.
     * Se invoca automáticamente cuando JavaFX inyecta los @FXML antes de llamar a initialize().
     */
    @FXML
    private void initialize() {
        // Configuramos el ComboBox de tipo de nota
        cmbTipo.getItems().setAll(Curso.TipoNota.values());
        // Vinculamos las columnas a las propiedades de Curso
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreCursoProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoNotaProperty());
        // Asignamos el modelo de datos al TableView
        tblCursos.setItems(modeloCursos);
    }

    /**
     * Carga los cursos desde el servicio y los coloca en el modelo observable.
     */
    private void cargarTabla() {
        modeloCursos.setAll(servicioCurso.getCursos());
    }

    /**
     * Configura la tabla (si hubiese lógica extra de inicialización, se haría aquí).
     */
    private void inicializarTabla() {
        // Actualmente, JavaFX ya invocó el initialize() que vincula columnas y modelo.
        // Si se necesitara más configuración de la tabla, iría aquí.
    }

    /**
     * Manejador del botón “Crear curso”. Valida campos, crea el curso y refresca la tabla.
     */
    @FXML
    private void onCrearCurso() {
        String nombre = txtNombre.getText().trim();
        Curso.TipoNota tipo = cmbTipo.getValue();

        if (nombre.isEmpty()) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "El nombre del curso no puede estar vacío.", ButtonType.OK);
            alerta.setHeaderText("Validación");
            alerta.showAndWait();
            return;
        }
        if (tipo == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Debe seleccionar un tipo de nota.", ButtonType.OK);
            alerta.setHeaderText("Validación");
            alerta.showAndWait();
            return;
        }

        // Llamamos al servicio para crear el curso
        servicioCurso.crearCurso(nombre, tipo);
        // Refrescamos la tabla
        cargarTabla();
        // Limpiamos los campos de texto
        txtNombre.clear();
        cmbTipo.getSelectionModel().clearSelection();
    }

    /**
     * Manejador del botón “Guardar”. Persiste la lista completa de cursos en el archivo .txt.
     */
    @FXML
    private void onGuardar() {
        try {
            persistencia.guardarCursos(servicioCurso.getCursos());
            Alert info = new Alert(Alert.AlertType.INFORMATION, "Cursos guardados correctamente en el archivo.", ButtonType.OK);
            info.setHeaderText("Guardado Exitoso");
            info.showAndWait();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR, "Error al guardar los cursos: " + e.getMessage(), ButtonType.OK);
            error.setHeaderText("Error de Persistencia");
            error.showAndWait();
        }
    }

    @FXML
    private void onCargarCursos() {
        try {
            // Invocar el método de persistencia para leer cursos del .txt
            List<Curso> listaDesdeArchivo = persistencia.cargarCursos();

            // Reemplazar la lista interna en servicioCurso
            servicioCurso.setCursos(listaDesdeArchivo);

            // Refrescar la tabla con los datos recién cargados
            cargarTabla();

            Alert info = new Alert(Alert.AlertType.INFORMATION,
                    "Cursos cargados exitosamente desde el archivo.", ButtonType.OK);
            info.setHeaderText("Carga Exitosa");
            info.showAndWait();
        } catch (Exception e) {
            Alert error = new Alert(Alert.AlertType.ERROR,
                    "Error al cargar cursos desde el archivo: " + e.getMessage(), ButtonType.OK);
            error.setHeaderText("Error de Carga");
            error.showAndWait();
        }
    }



    /**
     * Manejador del botón “Gestionar estudiantes”. Abre una nueva ventana para administrar los estudiantes del curso seleccionado.
     */
    @FXML
    private void onGestionarEstudiantes() {
        Curso seleccionado = tblCursos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING, "Debe seleccionar primero un curso de la lista.", ButtonType.OK);
            alerta.setHeaderText("Sin Selección");
            alerta.showAndWait();
            return;
        }

        try {
            // Cargar el FXML de VistaEstudiante
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/org.punto1a.UI.Vistas/VistaEstudiante.fxml"));
            Parent root = loader.load();

            // Obtener su controlador y pasarle servicios + curso seleccionado
            ControladorEstudiante controladorEst = loader.getController();
            controladorEst.setServicios(
                    servicioEstudiante,
                    servicioNota,
                    persistencia,
                    seleccionado
            );

            // Crear una nueva ventana (Stage) para la gestión de estudiantes
            Stage stage = new Stage();
            stage.setTitle("Estudiantes de: " + seleccionado.getNombreCurso());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait();

            // Al cerrar la ventana de estudiantes, refrescamos la tabla de cursos
            cargarTabla();

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            Alert error = new Alert(Alert.AlertType.ERROR, "No se pudo cargar la ventana de estudiantes.", ButtonType.OK);
            error.setHeaderText("Error de Carga");
            error.showAndWait();
        }
    }

    @FXML
    private void onCargarDatosPrueba() {
        // Llama al método que carga datos de prueba solo si el archivo está vacío o no existe
        List<Curso> listaActual = servicioCurso.getCursos();
        File archivoTxt = new File("cursos.txt");
        if (!archivoTxt.exists() || archivoTxt.length() == 0) {
            cargarDatosPrueba(listaActual);
            servicioCurso.setCursos(listaActual);
            cargarTabla();

            Alert info = new Alert(Alert.AlertType.INFORMATION,
                    "Se han cargado datos de prueba porque el archivo estaba vacío.", ButtonType.OK);
            info.setHeaderText("Datos de Prueba Cargados");
            info.showAndWait();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING,
                    "El archivo 'cursos.txt' ya contiene datos. Reinicia o vacía el archivo para cargar datos de prueba.", ButtonType.OK);
            alerta.setHeaderText("Archivo No Vacío");
            alerta.showAndWait();
        }
    }

    /**
     * Método que genera cursos de ejemplo en la lista recibida y guarda en disco.
     * Se basa en la implementación que proporcionaste.
     */
    private void cargarDatosPrueba(List<Curso> cursos) {
        // Creación de cursos de ejemplo
        Curso matematicas = new Curso("Matemáticas 1", Curso.TipoNota.CUANTITATIVA);
        Estudiante e1 = new Estudiante("Juan Perez");
        e1.setNota(new Nota<>(4.5));
        Estudiante e2 = new Estudiante("Ana Ruiz");
        e2.setNota(new Nota<>(3.7));
        matematicas.agregarEstudiante(e1);
        matematicas.agregarEstudiante(e2);

        Curso etica = new Curso("Ética", Curso.TipoNota.CUALITATIVA);
        Estudiante e3 = new Estudiante("Carlos Cadena");
        e3.setNota(new Nota<>("Aprobó"));
        Estudiante e4 = new Estudiante("Sofía Quintero");
        e4.setNota(new Nota<>("Reprobó"));
        etica.agregarEstudiante(e3);
        etica.agregarEstudiante(e4);

        // Limpiamos la lista (por si tenía algo) y agregamos los de prueba
        cursos.clear();
        cursos.add(matematicas);
        cursos.add(etica);

        // Guardamos inmediatamente en disco usando el mismo persistencia
        try {
            persistencia.guardarCursos(cursos);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



