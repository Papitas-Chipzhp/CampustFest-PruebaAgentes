package co.edu.unillanos.campusfest.application.usecase;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.domain.Reserva;

/**
 * Caso de uso CU2: solicita una reserva temporal de entrada y permite confirmarla dentro del plazo.
 */
public class ReservarEntrada {

    private final RepositorioEventos repositorio;
    private final Reloj reloj;

    public ReservarEntrada(RepositorioEventos repositorio, Reloj reloj) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio de eventos es obligatorio.");
        }
        if (reloj == null) {
            throw new IllegalArgumentException("El reloj es obligatorio.");
        }
        this.repositorio = repositorio;
        this.reloj = reloj;
    }

    /**
     * Solicita una reserva pendiente para un estudiante en el evento indicado y la persiste.
     */
    public Reserva solicitarReserva(String eventoId, String estudianteId) {
        Evento evento = buscarEvento(eventoId);
        Reserva reserva = evento.reservar(estudianteId, reloj.ahora());
        repositorio.guardar(evento);
        return reserva;
    }

    /**
     * Confirma una reserva existente del evento antes de que venza su plazo y persiste el cambio.
     */
    public Reserva confirmarReserva(String eventoId, String codigoReserva) {
        Evento evento = buscarEvento(eventoId);
        Reserva reserva = evento.buscarReservaPorCodigo(codigoReserva);
        reserva.confirmar(reloj.ahora());
        repositorio.guardar(evento);
        return reserva;
    }

    private Evento buscarEvento(String eventoId) {
        if (eventoId == null || eventoId.isBlank()) {
            throw new IllegalArgumentException("El identificador del evento es obligatorio.");
        }
        Evento evento = repositorio.buscar(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("No existe un evento con el identificador " + eventoId + ".");
        }
        return evento;
    }
}
