package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Reserva de entrada de un estudiante para un evento, con plazo de confirmación.
 */
public class Reserva {

    private static final int MINUTOS_PLAZO = 10;

    private final String codigo;
    private final String estudianteId;
    private final LocalDateTime venceEn;
    private EstadoReserva estado;
    private Asistencia asistencia;

    private Reserva(String codigo, String estudianteId, LocalDateTime venceEn,
                    EstadoReserva estado, Asistencia asistencia) {
        this.codigo = codigo;
        this.estudianteId = estudianteId;
        this.venceEn = venceEn;
        this.estado = estado;
        this.asistencia = asistencia;
    }

    public static Reserva pendiente(String estudianteId, LocalDateTime ahora) {
        if (estudianteId == null || estudianteId.isBlank()) {
            throw new IllegalArgumentException("El identificador del estudiante es obligatorio.");
        }
        validarHora(ahora);
        String codigo = UUID.randomUUID().toString();
        return new Reserva(codigo, estudianteId, ahora.plusMinutes(MINUTOS_PLAZO),
                EstadoReserva.PENDIENTE, null);
    }

    public static Reserva reconstruir(String codigo, String estudianteId, LocalDateTime venceEn,
                                      EstadoReserva estado, Asistencia asistencia) {
        return new Reserva(codigo, estudianteId, venceEn, estado, asistencia);
    }

    public boolean estaVigente(LocalDateTime ahora) {
        validarHora(ahora);
        return ahora.isBefore(venceEn);
    }

    public boolean ocupaCupo(LocalDateTime ahora) {
        validarHora(ahora);
        if (estado == EstadoReserva.CONFIRMADA) {
            return true;
        }
        if (estado == EstadoReserva.PENDIENTE) {
            return estaVigente(ahora);
        }
        return false;
    }

    public void confirmar(LocalDateTime ahora) {
        validarHora(ahora);
        if (estado != EstadoReserva.PENDIENTE) {
            throw new IllegalStateException("Solo se puede confirmar una reserva en estado pendiente.");
        }
        if (!estaVigente(ahora)) {
            throw new IllegalStateException(
                    "El plazo de " + MINUTOS_PLAZO + " minutos para confirmar la reserva ya venció.");
        }
        this.estado = EstadoReserva.CONFIRMADA;
    }

    public void cancelar() {
        if (estado == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("La reserva ya se encuentra cancelada.");
        }
        if (estado == EstadoReserva.VENCIDA) {
            throw new IllegalStateException("No se puede cancelar una reserva vencida.");
        }
        this.estado = EstadoReserva.CANCELADA;
    }

    public Asistencia registrarIngreso(LocalDateTime ahora) {
        validarHora(ahora);
        if (estado != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo puede ingresar una reserva confirmada.");
        }
        if (asistencia != null) {
            throw new IllegalStateException("La reserva ya registró su ingreso.");
        }
        this.asistencia = new Asistencia(ahora);
        return this.asistencia;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public LocalDateTime getVenceEn() {
        return venceEn;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public Asistencia getAsistencia() {
        return asistencia;
    }

    private static void validarHora(LocalDateTime ahora) {
        if (ahora == null) {
            throw new IllegalArgumentException("La hora actual es obligatoria.");
        }
    }
}
