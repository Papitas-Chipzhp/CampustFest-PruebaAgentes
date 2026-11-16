package co.edu.unillanos.campusfest.application.port;

import java.time.LocalDateTime;

/**
 * Puerto de salida para obtener la fecha y hora actual sin acoplar el núcleo al reloj del sistema.
 */
public interface Reloj {

    /**
     * Devuelve la fecha y hora actual.
     */
    LocalDateTime ahora();
}
