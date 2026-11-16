package co.edu.unillanos.campusfest.infrastructure.ui;

import java.time.format.DateTimeFormatter;

import co.edu.unillanos.campusfest.application.usecase.RegistrarIngreso;
import co.edu.unillanos.campusfest.domain.Asistencia;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controlador de la pantalla CU4 para registrar el ingreso mediante código QR.
 */
public class PantallaIngresoController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final RegistrarIngreso registrarIngreso;

    @FXML
    private TextField campoEventoId;

    @FXML
    private TextField campoCodigoQr;

    @FXML
    private Label etiquetaMensaje;

    public PantallaIngresoController(RegistrarIngreso registrarIngreso) {
        if (registrarIngreso == null) {
            throw new IllegalArgumentException("El caso de uso RegistrarIngreso es obligatorio.");
        }
        this.registrarIngreso = registrarIngreso;
    }

    @FXML
    private void registrarIngreso() {
        try {
            String eventoId = campoEventoId.getText().trim();
            String codigo = campoCodigoQr.getText().trim();
            Asistencia asistencia = registrarIngreso.registrar(eventoId, codigo);
            mostrarExito("Ingreso registrado correctamente (" + asistencia.getHora().format(FORMATO_HORA) + ").");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        campoEventoId.clear();
        campoCodigoQr.clear();
        limpiarMensaje();
    }

    private void mostrarExito(String mensaje) {
        etiquetaMensaje.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        etiquetaMensaje.getStyleClass().add("mensaje-exito");
        etiquetaMensaje.setText(mensaje);
    }

    private void mostrarError(String mensaje) {
        etiquetaMensaje.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        etiquetaMensaje.getStyleClass().add("mensaje-error");
        etiquetaMensaje.setText(mensaje);
    }

    private void limpiarMensaje() {
        etiquetaMensaje.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        etiquetaMensaje.setText("");
    }
}
