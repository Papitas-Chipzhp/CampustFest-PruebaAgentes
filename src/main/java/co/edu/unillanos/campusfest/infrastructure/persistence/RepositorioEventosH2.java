package co.edu.unillanos.campusfest.infrastructure.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Asistencia;
import co.edu.unillanos.campusfest.domain.EstadoReserva;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.domain.Reserva;

/**
 * Adaptador de persistencia H2 del puerto RepositorioEventos.
 */
public class RepositorioEventosH2 implements RepositorioEventos {

    private static final String RUTA_ESQUEMA_CLASSPATH = "/db/schema.sql";
    private static final String RUTA_ESQUEMA_DISCO = "src/main/resources/db/schema.sql";

    private static final String SQL_BUSCAR_EVENTO = """
            SELECT ID, NOMBRE, FECHA, LUGAR, AFORO, BLOQUEADOS
            FROM EVENTO
            WHERE ID = ?
            """;

    private static final String SQL_BUSCAR_RESERVAS = """
            SELECT r.CODIGO, r.ESTUDIANTE_ID, r.VENCE_EN, r.ESTADO, a.HORA
            FROM RESERVA r
            LEFT JOIN ASISTENCIA a ON a.RESERVA_CODIGO = r.CODIGO
            WHERE r.EVENTO_ID = ?
            ORDER BY r.VENCE_EN, r.CODIGO
            """;

    private static final String SQL_GUARDAR_EVENTO = """
            MERGE INTO EVENTO (ID, NOMBRE, FECHA, LUGAR, AFORO, BLOQUEADOS)
            KEY (ID)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String SQL_GUARDAR_RESERVA = """
            MERGE INTO RESERVA (CODIGO, EVENTO_ID, ESTUDIANTE_ID, VENCE_EN, ESTADO)
            KEY (CODIGO)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String SQL_GUARDAR_ASISTENCIA = """
            MERGE INTO ASISTENCIA (RESERVA_CODIGO, HORA)
            KEY (RESERVA_CODIGO)
            VALUES (?, ?)
            """;

    private final Connection conexion;

    /**
     * Crea el adaptador H2 sobre una conexión abierta e inicializa el esquema si no existe.
     */
    public RepositorioEventosH2(Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("La conexión a la base de datos es obligatoria.");
        }
        this.conexion = conexion;
        inicializarEsquema();
    }

    /**
     * Busca un evento por su identificador y reconstruye todas sus reservas y asistencias.
     */
    @Override
    public Evento buscar(String eventoId) {
        if (eventoId == null || eventoId.isBlank()) {
            return null;
        }
        try (PreparedStatement consulta = conexion.prepareStatement(SQL_BUSCAR_EVENTO)) {
            consulta.setString(1, eventoId);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }
                List<Reserva> reservas = buscarReservas(eventoId);
                return reconstruirEvento(resultado, reservas);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No fue posible consultar el evento en la base de datos.", e);
        }
    }

    /**
     * Inserta o actualiza el evento junto con sus reservas y asistencias de forma transaccional.
     */
    @Override
    public void guardar(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento es obligatorio.");
        }
        boolean autoCommitPrevio = true;
        boolean transaccionPropia = false;
        try {
            autoCommitPrevio = conexion.getAutoCommit();
            if (autoCommitPrevio) {
                conexion.setAutoCommit(false);
                transaccionPropia = true;
            }
            guardarEvento(evento);
            for (Reserva reserva : evento.getReservas()) {
                guardarReserva(evento.getId(), reserva);
                guardarAsistencia(reserva.getCodigo(), reserva.getAsistencia());
            }
            if (transaccionPropia) {
                conexion.commit();
            }
        } catch (SQLException e) {
            if (transaccionPropia) {
                deshacerTransaccion();
            }
            throw new IllegalStateException(
                    "No fue posible guardar el evento en la base de datos.", e);
        } finally {
            if (transaccionPropia) {
                restaurarAutoCommit(autoCommitPrevio);
            }
        }
    }

    private void deshacerTransaccion() {
        try {
            if (!conexion.isClosed()) {
                conexion.rollback();
            }
        } catch (SQLException ignorada) {
            // Se conserva la excepción original que originó el rollback.
        }
    }

    private void restaurarAutoCommit(boolean autoCommitPrevio) {
        try {
            if (!conexion.isClosed()) {
                conexion.setAutoCommit(autoCommitPrevio);
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No fue posible restaurar el modo de confirmación automática.", e);
        }
    }

    private void guardarEvento(Evento evento) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_GUARDAR_EVENTO)) {
            sentencia.setString(1, evento.getId());
            sentencia.setString(2, evento.getNombre());
            sentencia.setTimestamp(3, Timestamp.valueOf(evento.getFecha()));
            sentencia.setString(4, evento.getLugar());
            sentencia.setInt(5, evento.getAforo());
            sentencia.setInt(6, evento.getBloqueados());
            sentencia.executeUpdate();
        }
    }

    private void guardarReserva(String eventoId, Reserva reserva) throws SQLException {
        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_GUARDAR_RESERVA)) {
            sentencia.setString(1, reserva.getCodigo());
            sentencia.setString(2, eventoId);
            sentencia.setString(3, reserva.getEstudianteId());
            sentencia.setTimestamp(4, Timestamp.valueOf(reserva.getVenceEn()));
            sentencia.setString(5, reserva.getEstado().name());
            sentencia.executeUpdate();
        }
    }

    private void guardarAsistencia(String codigoReserva, Asistencia asistencia) throws SQLException {
        if (asistencia == null) {
            return;
        }
        try (PreparedStatement sentencia = conexion.prepareStatement(SQL_GUARDAR_ASISTENCIA)) {
            sentencia.setString(1, codigoReserva);
            sentencia.setTimestamp(2, Timestamp.valueOf(asistencia.getHora()));
            sentencia.executeUpdate();
        }
    }

    private List<Reserva> buscarReservas(String eventoId) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        try (PreparedStatement consulta = conexion.prepareStatement(SQL_BUSCAR_RESERVAS)) {
            consulta.setString(1, eventoId);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    reservas.add(reconstruirReserva(resultado));
                }
            }
        }
        return reservas;
    }

    private Evento reconstruirEvento(ResultSet resultado, List<Reserva> reservas) throws SQLException {
        String id = resultado.getString("ID");
        String nombre = resultado.getString("NOMBRE");
        LocalDateTime fecha = resultado.getTimestamp("FECHA").toLocalDateTime();
        String lugar = resultado.getString("LUGAR");
        int aforo = resultado.getInt("AFORO");
        int bloqueados = resultado.getInt("BLOQUEADOS");
        return new Evento(id, nombre, fecha, lugar, aforo, bloqueados, reservas);
    }

    private Reserva reconstruirReserva(ResultSet resultado) throws SQLException {
        String codigo = resultado.getString("CODIGO");
        String estudianteId = resultado.getString("ESTUDIANTE_ID");
        LocalDateTime venceEn = resultado.getTimestamp("VENCE_EN").toLocalDateTime();
        EstadoReserva estado = EstadoReserva.valueOf(resultado.getString("ESTADO"));
        Timestamp horaAsistencia = resultado.getTimestamp("HORA");
        Asistencia asistencia = horaAsistencia == null
                ? null
                : new Asistencia(horaAsistencia.toLocalDateTime());
        return Reserva.reconstruir(codigo, estudianteId, venceEn, estado, asistencia);
    }

    private void inicializarEsquema() {
        String sql = cargarScriptEsquema();
        try (Statement sentencia = conexion.createStatement()) {
            for (String bloque : sql.split(";")) {
                String instruccion = bloque.trim();
                if (!instruccion.isEmpty()) {
                    sentencia.execute(instruccion);
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException(
                    "No fue posible inicializar el esquema de la base de datos.", e);
        }
    }

    private String cargarScriptEsquema() {
        try (InputStream entrada = RepositorioEventosH2.class.getResourceAsStream(RUTA_ESQUEMA_CLASSPATH)) {
            if (entrada != null) {
                return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
            }
            Path archivo = Path.of(RUTA_ESQUEMA_DISCO);
            if (Files.isRegularFile(archivo)) {
                return Files.readString(archivo, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "No fue posible leer el script del esquema de base de datos.", e);
        }
        throw new IllegalStateException("No se encontró el script del esquema de base de datos.");
    }
}
