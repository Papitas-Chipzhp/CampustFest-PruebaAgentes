package co.edu.unillanos.campusfest.application.usecase;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Asistencia;
import co.edu.unillanos.campusfest.domain.EstadoReserva;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.domain.Reserva;

/**
 * Caso de uso CU4: registra el ingreso de un estudiante con reserva confirmada.
 */
public class RegistrarIngreso {

    private final RepositorioEventos repositorio;
    private final Reloj reloj;

    public RegistrarIngreso(RepositorioEventos repositorio, Reloj reloj) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio de eventos es obligatorio.");
        }
        if (reloj == null) {
            throw new IllegalArgumentException("El reloj es obligatorio.");
        }
        this.repositorio = repositorio;
        this.reloj = reloj;
    }

    public Asistencia registrar(String eventoId, String codigo) {
        Evento evento = buscarEvento(eventoId);
        Reserva reserva = evento.buscarReservaPorCodigo(codigo);
        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo pueden ingresar reservas confirmadas.");
        }
        Asistencia asistencia = reserva.registrarIngreso(reloj.ahora());
        repositorio.guardar(evento);
        return asistencia;
    }

    private Evento buscarEvento(String eventoId) {
        if (eventoId == null || eventoId.isBlank()) {
            throw new IllegalArgumentException("El identificador del evento es obligatorio.");
        }
        Evento evento = repositorio.buscar(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("No existe un evento con el identificador indicado.");
        }
        return evento;
    }
}