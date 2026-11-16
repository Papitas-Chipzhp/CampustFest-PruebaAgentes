package co.edu.unillanos.campusfest.infrastructure.persistence;

import java.util.HashMap;
import java.util.Map;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;

/**
 * Adaptador de persistencia en memoria del puerto RepositorioEventos.
 */
public class RepositorioEventosEnMemoria implements RepositorioEventos {

    private final Map<String, Evento> eventos = new HashMap<>();

    /**
     * Crea un repositorio en memoria vacío.
     */
    public RepositorioEventosEnMemoria() {
    }

    /**
     * Busca un evento por su identificador o retorna {@code null} si no existe.
     */
    @Override
    public Evento buscar(String eventoId) {
        if (eventoId == null) {
            return null;
        }
        return eventos.get(eventoId);
    }

    /**
     * Inserta o actualiza el evento en el mapa en memoria.
     */
    @Override
    public void guardar(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento es obligatorio.");
        }
        eventos.put(evento.getId(), evento);
    }
}
