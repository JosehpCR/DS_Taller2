package org.punto1a.UI.App;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.punto1a.Aplicacion.ServicioCurso;
import org.punto1a.Aplicacion.ServicioEstudiante;
import org.punto1a.Aplicacion.ServicioNota;
import org.punto1a.Dominio.Curso;
import org.punto1a.Infraestructura.IServicioPersistencia;
import org.punto1a.Infraestructura.ServicioPersistencia;
import org.punto1a.UI.Controladores.ControladorCurso;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que arranca JavaFX y carga la primera ventana (gestión de cursos).
 * Inyecta los servicios de aplicación y persistencia a los controladores.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1) Crear la instancia de persistencia, indicando el archivo de texto
        //    (se asume que "cursos.txt" está en la raíz del proyecto o carpeta de trabajo).
        IServicioPersistencia persistencia = new ServicioPersistencia("cursos.txt");

        // 2) Cargar desde persistencia la lista de cursos (si ya existe) o dejar vacía
        List<Curso> listaCursos = null;
        try {
            listaCursos = new ArrayList<>(persistencia.cargarCursos());
        } catch (Exception e) {
            // Si da error (por ejemplo no existe el archivo), inicializamos lista vacía
            listaCursos = new ArrayList<>();
        }

        // 3) Crear los servicios de aplicación, inyectándoles la lista de cursos cargada
        ServicioCurso servicioCurso = new ServicioCurso(listaCursos);
        ServicioEstudiante servicioEstudiante = new ServicioEstudiante();
        ServicioNota servicioNota = new ServicioNota();

        // 4) Cargar FXML de vista de cursos
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/org.punto1a.UI.Vistas/VistaCurso.fxml"));
        Parent root = loader.load();

        // 5) Obtener el controlador y pasarle las dependencias (servicios y persistencia)
        ControladorCurso controladorCurso = loader.getController();
        controladorCurso.setServicios(servicioCurso, servicioEstudiante, servicioNota, persistencia);

        // 6) Mostrar la ventana principal
        Scene scene = new Scene(root);
        primaryStage.setTitle("Gestión de Cursos");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

