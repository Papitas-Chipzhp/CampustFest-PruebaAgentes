package co.edu.unillanos.campusfest.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Evento universitario con control de aforo, cupos bloqueados y reservas.
 */
public class Evento {

    private final String id;
    private String nombre;
    private LocalDateTime fecha;
    private String lugar;
    private int aforo;
    private final int bloqueados;
    private final List<Reserva> reservas;

    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados) {
        this(id, nombre, fecha, lugar, aforo, bloqueados, new ArrayList<>());
    }

    public Evento(String id, String nombre, LocalDateTime fecha, String lugar,
                  int aforo, int bloqueados, List<Reserva> reservas) {
        validarTexto(id, "El identificador del evento es obligatorio.");
        validarDatosEvento(nombre, fecha, lugar);
        if (aforo < 0) {
            throw new IllegalArgumentException("El aforo no puede ser negativo.");
        }
        if (bloqueados < 0) {
            throw new IllegalArgumentException("Los cupos bloqueados no pueden ser negativos.");
        }
        if (bloqueados > aforo) {
            throw new IllegalArgumentException("Los cupos bloqueados no pueden superar el aforo.");
        }
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.aforo = aforo;
        this.bloqueados = bloqueados;
        this.reservas = reservas == null ? new ArrayList<>() : new ArrayList<>(reservas);
    }

    public int contarOcupados(LocalDateTime ahora) {
        if (ahora == null) {
            throw new IllegalArgumentException("La hora actual es obligatoria.");
        }
        int ocupados = 0;
        for (Reserva reserva : reservas) {
            if (reserva.ocupaCupo(ahora)) {
                ocupados++;
            }
        }
        return ocupados;
    }

    public int cuposDisponibles(LocalDateTime ahora) {
        return aforo - bloqueados - contarOcupados(ahora);
    }

    public Reserva reservar(String estudianteId, LocalDateTime ahora) {
        validarTexto(estudianteId, "El identificador del estudiante es obligatorio.");
        if (cuposDisponibles(ahora) <= 0) {
            throw new IllegalStateException("No hay cupos disponibles para este evento.");
        }
        Reserva reserva = Reserva.pendiente(estudianteId, ahora);
        reservas.add(reserva);
        return reserva;
    }

    public Reserva buscarReservaPorCodigo(String codigo) {
        validarTexto(codigo, "El código de la reserva es obligatorio.");
        for (Reserva reserva : reservas) {
            if (reserva.getCodigo().equals(codigo)) {
                return reserva;
            }
        }
        throw new IllegalArgumentException(
                "No existe una reserva con el código " + codigo + " en este evento.");
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getLugar() {
        return lugar;
    }

    public int getAforo() {
        return aforo;
    }

    public int getBloqueados() {
        return bloqueados;
    }

    public List<Reserva> getReservas() {
        return Collections.unmodifiableList(reservas);
    }

    private void validarDatosEvento(String nombre, LocalDateTime fecha, String lugar) {
        validarTexto(nombre, "El nombre del evento es obligatorio.");
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha del evento es obligatoria.");
        }
        validarTexto(lugar, "El lugar del evento es obligatorio.");
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(mensaje);
        }
    }
}
