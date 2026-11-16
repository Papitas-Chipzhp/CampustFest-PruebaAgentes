package co.edu.unillanos.campusfest.application.usecase;

import java.time.LocalDateTime;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;

/**
 * Caso de uso CU3: consulta los datos de un evento y permite actualizar sus datos generales y aforo.
 */
public class EditarEvento {

    private final RepositorioEventos repositorio;
    private final Reloj reloj;

    public EditarEvento(RepositorioEventos repositorio, Reloj reloj) {
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
     * Obtiene el evento solicitado para cargarlo en pantalla, sin persistir cambios.
     */
    public Evento obtener(String eventoId) {
        return buscarEvento(eventoId);
    }

    /**
     * Actualiza los datos básicos y el aforo del evento, persistiendo solo si todas las reglas se cumplen.
     */
    public void editar(String eventoId, String nombre, LocalDateTime fecha, String lugar, int nuevoAforo) {
        Evento evento = buscarEvento(eventoId);
        evento.cambiarDatos(nombre, fecha, lugar);
        evento.cambiarAforo(nuevoAforo, reloj.ahora());
        repositorio.guardar(evento);
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
