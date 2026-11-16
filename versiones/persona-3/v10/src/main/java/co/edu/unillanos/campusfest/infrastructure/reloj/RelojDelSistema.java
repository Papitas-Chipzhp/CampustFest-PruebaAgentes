package co.edu.unillanos.campusfest.infrastructure.reloj;

import java.time.LocalDateTime;

import co.edu.unillanos.campusfest.application.port.Reloj;

/**
 * Adaptador de salida que obtiene la fecha y hora actual del reloj del sistema.
 */
public class RelojDelSistema implements Reloj {

    /**
     * Retorna la fecha y hora actual del sistema.
     */
    @Override
    public LocalDateTime ahora() {
        return LocalDateTime.now();
    }
}
