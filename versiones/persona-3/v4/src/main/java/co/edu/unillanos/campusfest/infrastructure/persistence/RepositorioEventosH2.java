package co.edu.unillanos.campusfest.infrastructure.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Evento;

/**
 * Adaptador de persistencia H2 del puerto RepositorioEventos.
 */
public class RepositorioEventosH2 implements RepositorioEventos {

    private static final String RUTA_ESQUEMA_CLASSPATH = "/db/schema.sql";
    private static final String RUTA_ESQUEMA_DISCO = "src/main/resources/db/schema.sql";

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

    @Override
    public Evento buscar(String eventoId) {
        return null;
    }

    @Override
    public void guardar(Evento evento) {
        throw new UnsupportedOperationException("Pendiente v6");
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
