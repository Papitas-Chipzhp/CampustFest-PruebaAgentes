package co.edu.unillanos.campusfest;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.application.usecase.ConsultarCupos;
import co.edu.unillanos.campusfest.application.usecase.EditarEvento;
import co.edu.unillanos.campusfest.application.usecase.RegistrarIngreso;
import co.edu.unillanos.campusfest.application.usecase.ReservarEntrada;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.infrastructure.persistence.RepositorioEventosH2;
import co.edu.unillanos.campusfest.infrastructure.reloj.RelojDelSistema;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaCuposController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaEventoController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaIngresoController;
import co.edu.unillanos.campusfest.infrastructure.ui.PantallaReservaController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Ensamblador principal de CampusFest: conecta H2, reloj, casos de uso, controladores y vistas JavaFX.
 */
public class App extends Application {

    private static final String RUTA_VISTAS = "/co/edu/unillanos/campusfest/vistas/";
    private static final String RUTA_ESTILOS = RUTA_VISTAS + "estilos.css";
    private static final String URL_BASE_DATOS = "jdbc:h2:file:./datos/campusfest";
    private static final String USUARIO_BASE_DATOS = "sa";
    private static final String CLAVE_BASE_DATOS = "";

    private Connection conexion;

    @Override
    public void start(Stage escenario) {
        try {
            conexion = abrirConexion();
            RepositorioEventos repositorio = new RepositorioEventosH2(conexion);
            Reloj reloj = new RelojDelSistema();
            precargarEventos(repositorio);

            ConsultarCupos consultarCupos = new ConsultarCupos(repositorio, reloj);
            ReservarEntrada reservarEntrada = new ReservarEntrada(repositorio, reloj);
            EditarEvento editarEvento = new EditarEvento(repositorio, reloj);
            RegistrarIngreso registrarIngreso = new RegistrarIngreso(repositorio, reloj);

            TabPane pestanas = new TabPane();
            pestanas.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            pestanas.getTabs().addAll(
                    crearPestana("Consultar cupos", "pantalla-cupos.fxml",
                            new PantallaCuposController(consultarCupos)),
                    crearPestana("Reservar entrada", "pantalla-reserva.fxml",
                            new PantallaReservaController(reservarEntrada)),
                    crearPestana("Editar evento", "pantalla-evento.fxml",
                            new PantallaEventoController(editarEvento)),
                    crearPestana("Ingreso QR", "pantalla-ingreso.fxml",
                            new PantallaIngresoController(registrarIngreso))
            );

            Scene escena = new Scene(pestanas, 700, 560);
            escena.getStylesheets().add(App.class.getResource(RUTA_ESTILOS).toExternalForm());
            escenario.setTitle("CampusFest — Gestión de eventos");
            escenario.setScene(escena);
            escenario.show();
        } catch (IllegalStateException e) {
            mostrarErrorFatal(e.getMessage());
            throw e;
        }
    }

    @Override
    public void stop() {
        if (conexion == null) {
            return;
        }
        try {
            if (!conexion.isClosed()) {
                conexion.close();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("No fue posible cerrar la conexión con la base de datos.", e);
        }
    }

    private Connection abrirConexion() {
        try {
            return DriverManager.getConnection(URL_BASE_DATOS, USUARIO_BASE_DATOS, CLAVE_BASE_DATOS);
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No fue posible abrir la base de datos local de CampusFest.", e);
        }
    }

    private void mostrarErrorFatal(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error de inicio — CampusFest");
        alerta.setHeaderText("No fue posible iniciar la aplicación");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private Tab crearPestana(String titulo, String archivoFxml, Object controlador) {
        try {
            FXMLLoader cargador = new FXMLLoader(App.class.getResource(RUTA_VISTAS + archivoFxml));
            cargador.setController(controlador);
            Parent contenido = cargador.load();
            Tab pestana = new Tab(titulo, contenido);
            pestana.setClosable(false);
            return pestana;
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible cargar la pantalla " + archivoFxml + ".", e);
        }
    }

    private void precargarEventos(RepositorioEventos repositorio) {
        if (repositorio.buscar("ev-1") == null) {
            repositorio.guardar(new Evento(
                    "ev-1",
                    "Concierto de bienvenida",
                    LocalDateTime.of(2026, 11, 28, 19, 0),
                    "Auditorio Barcelona",
                    100,
                    10
            ));
        }
        if (repositorio.buscar("ev-2") == null) {
            repositorio.guardar(new Evento(
                    "ev-2",
                    "Feria de emprendimiento",
                    LocalDateTime.of(2026, 12, 5, 9, 0),
                    "Plazoleta central",
                    50,
                    5
            ));
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
