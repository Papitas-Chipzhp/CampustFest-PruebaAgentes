package co.edu.unillanos.campusfest.infrastructure.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import co.edu.unillanos.campusfest.application.usecase.EditarEvento;
import co.edu.unillanos.campusfest.domain.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Controlador de la pantalla CU3 para consultar y editar los datos y el aforo de un evento.
 */
public class PantallaEventoController {

    private final EditarEvento editarEvento;

    @FXML
    private TextField campoEventoId;

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoFecha;

    @FXML
    private TextField campoLugar;

    @FXML
    private TextField campoAforo;

    @FXML
    private Label etiquetaMensaje;

    public PantallaEventoController(EditarEvento editarEvento) {
        if (editarEvento == null) {
            throw new IllegalArgumentException("El caso de uso EditarEvento es obligatorio.");
        }
        this.editarEvento = editarEvento;
    }

    @FXML
    private void initialize() {
        if (campoEventoId != null) {
            cargarEvento();
        }
    }

    @FXML
    private void cargarEvento() {
        try {
            String eventoId = campoEventoId.getText().trim();
            Evento evento = editarEvento.obtener(eventoId);
            campoNombre.setText(evento.getNombre());
            campoFecha.setText(evento.getFecha().toString());
            campoLugar.setText(evento.getLugar());
            campoAforo.setText(String.valueOf(evento.getAforo()));
            mostrarExito("Evento cargado: " + evento.getNombre());
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void guardarCambios() {
        try {
            String eventoId = campoEventoId.getText().trim();
            String nombre = campoNombre.getText().trim();
            LocalDateTime fecha = parsearFecha(campoFecha.getText().trim());
            String lugar = campoLugar.getText().trim();
            int nuevoAforo = parsearAforo(campoAforo.getText().trim());

            editarEvento.editar(eventoId, nombre, fecha, lugar, nuevoAforo);
            mostrarExito("Cambios guardados para el evento " + eventoId + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void limpiar() {
        campoEventoId.clear();
        campoNombre.clear();
        campoFecha.clear();
        campoLugar.clear();
        campoAforo.clear();
        limpiarMensaje();
    }

    private LocalDateTime parsearFecha(String texto) {
        if (texto.isBlank()) {
            throw new IllegalArgumentException("La fecha del evento es obligatoria.");
        }
        try {
            return LocalDateTime.parse(texto);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Usa AAAA-MM-DDTHH:MM.", e);
        }
    }

    private int parsearAforo(String texto) {
        if (texto.isBlank()) {
            throw new IllegalArgumentException("El aforo es obligatorio.");
        }
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El aforo debe ser un número entero válido.", e);
        }
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
