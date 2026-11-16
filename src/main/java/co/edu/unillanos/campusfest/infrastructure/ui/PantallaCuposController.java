package co.edu.unillanos.campusfest.infrastructure.ui;

import co.edu.unillanos.campusfest.application.usecase.ConsultarCupos;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controlador de la pantalla CU1 para consultar los cupos disponibles de un evento.
 */
public class PantallaCuposController {

    private final ConsultarCupos consultarCupos;

    @FXML
    private TextField campoEventoId;

    @FXML
    private Label etiquetaMensaje;

    public PantallaCuposController(ConsultarCupos consultarCupos) {
        if (consultarCupos == null) {
            throw new IllegalArgumentException("El caso de uso ConsultarCupos es obligatorio.");
        }
        this.consultarCupos = consultarCupos;
    }

    @FXML
    private void consultarCupos() {
        try {
            String eventoId = campoEventoId.getText().trim();
            int disponibles = consultarCupos.consultar(eventoId);
            mostrarExito("Cupos disponibles para " + eventoId + ": " + disponibles);
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        campoEventoId.clear();
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
