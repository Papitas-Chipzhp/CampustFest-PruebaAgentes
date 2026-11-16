package co.edu.unillanos.campusfest.application.port;

import co.edu.unillanos.campusfest.domain.Evento;

/**
 * Puerto de salida para consultar y persistir eventos junto con sus reservas y asistencias.
 */
public interface RepositorioEventos {

    /**
     * Busca un evento por su identificador o retorna {@code null} si no existe.
     */
    Evento buscar(String eventoId);

    /**
     * Inserta o actualiza el evento con todas sus reservas y asistencias.
     */
    void guardar(Evento evento);
}
