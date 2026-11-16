package co.edu.unillanos.campusfest.infrastructure.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.unillanos.campusfest.application.usecase.ReservarEntrada;
import co.edu.unillanos.campusfest.domain.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * Controlador de la pantalla CU2 para solicitar y confirmar reservas temporales de entradas.
 */
public class PantallaReservaController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ReservarEntrada reservarEntrada;

    @FXML
    private TextField campoEventoId;

    @FXML
    private TextField campoEstudianteId;

    @FXML
    private TextField campoCodigoReserva;

    @FXML
    private Label etiquetaMensaje;

    public PantallaReservaController(ReservarEntrada reservarEntrada) {
        if (reservarEntrada == null) {
            throw new IllegalArgumentException("El caso de uso ReservarEntrada es obligatorio.");
        }
        this.reservarEntrada = reservarEntrada;
    }

    @FXML
    private void solicitarReserva() {
        try {
            String eventoId = campoEventoId.getText().trim();
            String estudianteId = campoEstudianteId.getText().trim();
            Reserva reserva = reservarEntrada.solicitarReserva(eventoId, estudianteId);
            campoCodigoReserva.setText(reserva.getCodigo());
            LocalDateTime venceEn = LocalDateTime.now().plusMinutes(10);
            mostrarExito("Reserva pendiente creada. Código: " + reserva.getCodigo()
                    + ". Vence: " + venceEn.format(FORMATO_HORA) + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void confirmarReserva() {
        try {
            String eventoId = campoEventoId.getText().trim();
            String codigoReserva = campoCodigoReserva.getText().trim();
            Reserva reserva = reservarEntrada.confirmarReserva(eventoId, codigoReserva);
            mostrarExito("Reserva confirmada para " + reserva.getEstudianteId()
                    + ". Código QR: " + reserva.getCodigo());
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void copiarCodigo() {
        String codigo = campoCodigoReserva.getText().trim();
        if (codigo.isBlank()) {
            mostrarError("No hay ningún código de reserva para copiar.");
            return;
        }
        ClipboardContent contenido = new ClipboardContent();
        contenido.putString(codigo);
        Clipboard.getSystemClipboard().setContent(contenido);
        mostrarExito("Código QR copiado al portapapeles: " + codigo);
    }

    @FXML
    private void limpiar() {
        campoEventoId.clear();
        campoEstudianteId.clear();
        campoCodigoReserva.clear();
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
