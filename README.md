# CampusFest

Sistema de escritorio en **Java 21 + JavaFX 21** para la gestión de cupos, reservas temporales de entradas, edición de eventos y control de ingreso mediante código QR en eventos universitarios, construido bajo **Arquitectura Limpia (Hexagonal)** y persistencia embebida en **H2**.

---

## Arquitectura y tecnologías

- **Lenguaje y compilación**: Java 21 (`maven-compiler-plugin:3.13.0`, `<release>21</release>`)
- **Interfaz gráfica**: JavaFX 21.0.5 (`javafx-controls`, `javafx-fxml`, `javafx-maven-plugin:0.0.8`)
- **Persistencia**: H2 Database 2.2.224 (`jdbc:h2:file:./datos/campusfest`, esquema `EVENTO`, `RESERVA`, `ASISTENCIA` en `src/main/resources/db/schema.sql`)
- **Pruebas automatizadas**: JUnit Jupiter 5.10.2 (`maven-surefire-plugin:3.2.5`) — **90 pruebas** en 10 clases de prueba (+ 2 dobles de prueba en `src/test/`)

### Anillos y regla de dependencia

El código fuente de `co.edu.unillanos.campusfest` está organizado en cuatro anillos concéntricos donde las dependencias apuntan estrictamente hacia el centro:

| Anillo | Paquete / Ruta | Responsabilidad |
| --- | --- | --- |
| **Entidades (`domain`)** | `co.edu.unillanos.campusfest.domain` | `Evento`, `Reserva`, `Asistencia`, `EstadoReserva`. Reglas de negocio puras (plazo de 10 minutos, cálculo de cupos disponibles, aforo mínimo, transiciones de reserva y asistencia). Sin `.now()`, sin consola y sin dependencias externas. |
| **Puertos (`application.port`)** | `co.edu.unillanos.campusfest.application.port` | Interfaces `RepositorioEventos` (`buscar`, `guardar`) y `Reloj` (`ahora`). |
| **Casos de uso (`application.usecase`)** | `co.edu.unillanos.campusfest.application.usecase` | `ConsultarCupos` (CU1), `ReservarEntrada` (CU2), `EditarEvento` (CU3) y `RegistrarIngreso` (CU4). Solo coordinan entidades y puertos; reciben `(RepositorioEventos, Reloj)`. |
| **Adaptadores (`infrastructure`)** | `co.edu.unillanos.campusfest.infrastructure.{persistence,reloj,ui}` | `RepositorioEventosH2`, `RepositorioEventosEnMemoria`, `RelojDelSistema` (única clase que invoca `LocalDateTime.now()`), y los 4 controladores JavaFX (`Pantalla*Controller`). |
| **Ensamblaje y vistas** | `co.edu.unillanos.campusfest` (`App`, `Launcher`) y `resources/.../vistas/` | `App` abre la conexión H2, instancia puertos/casos de uso/controladores y asigna cada controlador a su FXML mediante `FXMLLoader.setController(...)`. |

---

## Integrantes y roles del equipo

> Completar nombres y usuarios de GitHub antes de la entrega final.

| Integrante | Usuario GitHub | Rol en el equipo | Commits propios | PRs abiertos | Auditorías cruzadas |
| --- | --- | --- | --- | --- | --- |
| `<Nombre Integrante 1>` | `<@usuario-github-1>` | Dominio (`domain`) | 8 (`ag1-v1..ag1-v8`) | 2 (`PR #1`, `PR #2`) | 2 PRs (`PR #6`, `PR #9`) |
| `<Nombre Integrante 2>` | `<@usuario-github-2>` | Casos de uso (`application`) + Revisor Principal QA | 6 (`ag2-v1..ag2-v6`) | 2 (`PR #3`, `PR #4`) | 5 asignaciones (`v0`, `PR #1`, `PR #2`, `PR #5`, `PR #8`, `PR #11`) |
| `<Nombre Integrante 3>` | `<@usuario-github-3>` | Infraestructura (`RelojDelSistema`, Memoria, H2 / SQL) | 10 (`ag3-v1..ag3-v10`) | 3 (`PR #5`, `PR #7`, `PR #8`) | 3 actas en 2 PRs (`PR #4`, `PR #10`) |
| `<Nombre Integrante 4>` | `<@usuario-github-4>` | Líder Integrador + Interfaz JavaFX (`vistas`, controladores, `App`, `Launcher`, docs) | `v0` en `main` + 12 (`ag4-v1..ag4-v12`) | 4 (`PR #6`, `PR #9`, `PR #10`, `PR #11`) | 2 PRs (`PR #3`, `PR #7`) + integración |

---

## Requisitos, ejecución y uso de la interfaz

### Requisitos previos

- **Java**: JDK 21 o superior (compilación con `--release 21`).
- **Apache Maven**: 3.9+ (probado con Maven 3.10.0).

### Comandos principales

```bash
# Ejecutar la suite completa de 90 pruebas automatizadas (sin abrir ventanas)
mvn clean test

# Iniciar la aplicación JavaFX con persistencia local H2 (./datos/campusfest)
mvn javafx:run
```

- La clase principal configurada en `javafx-maven-plugin` es `co.edu.unillanos.campusfest.App` (también se incluye `co.edu.unillanos.campusfest.Launcher` como punto de entrada alterno).
- Al iniciar `App`, si aún no existen en la base `./datos/campusfest`, se precargan de forma idempotente dos eventos de ejemplo:
  - `ev-1`: **Concierto de bienvenida** (`Auditorio Barcelona`, aforo `100`, bloqueados `10`).
  - `ev-2`: **Feria de emprendimiento** (`Plazoleta central`, aforo `50`, bloqueados `5`).

### Guía rápida de las 4 pantallas (CU1–CU4)

La ventana principal presenta un `TabPane` con cuatro pestañas estilizadas mediante `estilos.css` (con diferenciación visual `.mensaje-exito` / `.mensaje-error`, línea de ayuda `.subtitulo-ayuda` y botón **Limpiar** con acción `#limpiar` en cada pantalla):

1. **Consultar cupos (`pantalla-cupos.fxml`) — CU1**: ingresa el ID del evento (por defecto `ev-1`) y pulsa **Consultar cupos** (`#consultarCupos`) para ver los cupos disponibles actuales (`aforo - bloqueados - reservas vigentes/confirmadas`).
2. **Reservar entrada (`pantalla-reserva.fxml`) — CU2**:
   - Ingresa evento e ID de estudiante y pulsa **Solicitar reserva** (`#solicitarReserva`) para obtener un código único y su hora de vencimiento (plazo de 10 minutos).
   - Pulsa **Copiar código QR** (`#copiarCodigo`) para copiar el código generado al portapapeles mediante `Clipboard` de JavaFX y llevarlo a la pestaña de ingreso.
   - Pulsa **Confirmar reserva** (`#confirmarReserva`) dentro del plazo vigente para consolidar la entrada.
3. **Editar evento (`pantalla-evento.fxml`) — CU3**: precarga `ev-1` automáticamente al abrir la pestaña (`@FXML initialize()` → `#cargarEvento`). Permite consultar cualquier evento con **Cargar evento** (`#cargarEvento`) y actualizar nombre, fecha (`AAAA-MM-DDTHH:MM`), lugar y aforo máximo con **Guardar cambios** (`#guardarCambios`), respetando el aforo mínimo (`bloqueados + cupos ocupados por reservas`).
4. **Ingreso QR (`pantalla-ingreso.fxml`) — CU4**: pega o escribe el código QR de una reserva confirmada junto al ID del evento y pulsa **Registrar ingreso** (`#registrarIngreso`) para registrar la asistencia con la hora del sistema.

---

## Declaración de uso de IA — Aspecto 8.1

| Elemento | Detalle documentado |
| --- | --- |
| **Herramientas y modelos utilizados** | **Antigravity (`Gemini 4 Hydrogen`, razonamiento alto)** para la implementación asistida por capas y auditorías cruzadas registradas en los `COMMITS.md` y `REVISIONES.md` de los 4 agentes; **Claude** únicamente como apoyo externo en planeación inicial y diseño de prompts (sin generar código fuente). |
| **Tareas en las que apoyó la IA** | Inicialización Maven (`pom.xml`, `.gitignore`, `README.md`), estructuración de entidades (`domain`), interfaces de puertos y casos de uso (`application`), esquema SQL y adaptador transaccional H2 (`infrastructure`), diseño de vistas FXML/CSS, controladores JavaFX, ensamblaje `App`/`Launcher` y redacción de listas de verificación para auditorías cruzadas. |
| **Qué verificó el equipo manualmente y con pruebas** | 1) **Suite automatizada `90/90`**: `ReservaTest` (`16/16`), `EventoTest` (`14/14`), `ConsultarCuposTest` (`5/5`), `ReservarEntradaTest` (`7/7`), `EditarEventoTest` (`6/6`), `RegistrarIngresoTest` (`5/5`), `RepositorioEventosEnMemoriaTest` (`4/4`), `RepositorioEventosH2Test` (`16/16`), `PantallasTest` (`14/14`) y `ReglaDeDependenciaTest` (`3/3`).<br>2) **Revisión cruzada obligatoria** en `REVISIONES.md` antes de promover cada PR a `proyecto/`.<br>3) **Integridad de pruebas**: verificación SHA-256 (`Get-FileHash`) de los 12 archivos de `src/test/` frente a `base/src/test/`. |
| **Controles de calidad aplicados** | Prohibición de modificar `base/src/test/`; prohibición de `pom-local.xml` en entregas finales; verificación de cero `.now()` fuera de `RelojDelSistema`, cero `System.out`/`System.err`/`printStackTrace`, cero `getEstado()` fuera de `Reserva`/`RepositorioEventosH2`, cero `plusMinutes`/`isBefore`/`isAfter` fuera de `Reserva`, cero setters públicos en `Reserva`/`Asistencia`, instanciación concreta únicamente en `App.java` y comparación SHA-256 entre `v10` y `v11` (Aspecto 6.5). |

---

## Casos reales donde una propuesta de IA violó una regla y fue rechazada y corregida — Aspecto 8.2

Durante las auditorías cruzadas se detectaron, rechazaron (`CAMBIOS PEDIDOS`) y corrigieron con commits dedicados **3 casos reales** en distintas capas del sistema:

### Caso 1 (`PR #4`, capa `application`): `RegistrarIngreso.java` (`ag2-v5` → `ag2-v6`, auditoría del Agente 3)

- **Propuesta inicial de la IA y commit original (`ag2-v5`)**:
  - Commit: `Implementa el caso de uso RegistrarIngreso (CU4) con validación inicial de estado en aplicación`
  - En `RegistrarIngreso.java:6` importó `co.edu.unillanos.campusfest.domain.EstadoReserva` y en `RegistrarIngreso.java:35-37` insertó `if (reserva.getEstado() != EstadoReserva.CONFIRMADA) { throw new IllegalStateException("Solo pueden ingresar reservas confirmadas."); }` antes de invocar `reserva.registrarIngreso(reloj.ahora())`.
- **Regla / criterio violado**:
  - **GRASP Experto en información** y encapsulamiento del dominio (uso de `getEstado()` fuera de `Reserva` y `RepositorioEventosH2`).
  - **GRASP Controlador**: un caso de uso coordina el flujo sin asumir validaciones de estado de la entidad.
  - **Regla única / No duplicación**: `Reserva.registrarIngreso(LocalDateTime ahora)` (`Reserva.java:83-85`) ya validaba `estado != EstadoReserva.CONFIRMADA`.
- **Cómo se detectó**:
  - Aunque la suite parcial `56/56` pasaba, el **Agente 3 (`infrastructure`)** identificó la fuga de regla de negocio durante la revisión cruzada (`agentes/agente-2-application/REVISIONES.md`) y dictaminó **`CAMBIOS PEDIDOS`** citando `RegistrarIngreso.java:6, 35-37`.
- **Cómo se corrigió y commit de corrección (`ag2-v6`)**:
  - Commit: `Elimina validación redundante de estado en RegistrarIngreso tras auditoría del Agente 3 (Caso IA #1)`
  - Se eliminaron `import EstadoReserva` y el condicional redundante, delegando por completo en `reserva.registrarIngreso(reloj.ahora())` y estandarizando `buscarEvento(eventoId)` en los cuatro casos de uso.

### Caso 2 (`PR #8`, capa `infrastructure`): `RepositorioEventosH2.java` (`ag3-v8` → `ag3-v9`, auditoría del Agente 2)

- **Propuesta inicial de la IA y commit original (`ag3-v8`)**:
  - Commit: `Agrega manejo transaccional explícito (setAutoCommit, commit, rollback, finally) en guardar(evento) con concatenación de e.getMessage() JDBC`
  - En los cuatro bloques `catch (SQLException e)` de `RepositorioEventosH2.java` (`:95-96` en `buscar`, `:128-129` en `guardar`, `:153-154` en `restaurarAutoCommit` y `:237-238` en `inicializarEsquema`), la propuesta concatenó `+ e.getMessage()` en la `IllegalStateException`.
- **Regla / criterio violado**:
  - **Aspecto 6.4** y convención de errores (`base/CONTRATO.md`): los adaptadores H2 deben traducir `SQLException` a `IllegalStateException` sin exponer mensajes técnicos de JDBC/SQL (códigos de H2, sentencias o detalles de conexión) que luego los controladores mostrarían en `etiquetaMensaje`.
- **Cómo se detectó**:
  - Las pruebas automatizadas `76/76` pasaban porque validan la clase de excepción lanzada; el **Agente 2 (`application` / Revisor QA)** detectó la concatenación al auditar línea por línea el código de `v8` (`agentes/agente-3-infrastructure/REVISIONES.md`) y emitió **`CAMBIOS PEDIDOS`**.
- **Cómo se corrigió y commit de corrección (`ag3-v9`)**:
  - Commit: `Elimina concatenación de e.getMessage() JDBC en RepositorioEventosH2 dejando mensajes claros en español (Aspecto 6.4 / Caso IA #2)`
  - Se retiró `+ e.getMessage()` en las cuatro ubicaciones, dejando mensajes comprensibles en español para el usuario final y conservando `e` únicamente como causa interna (`new IllegalStateException("...", e)`).

### Caso 3 (`PR #9`, capa `ui`): `PantallaReservaController.java` (`ag4-v8` → `ag4-v9`, auditoría del Agente 1)

- **Propuesta inicial de la IA y commit original (`ag4-v8`)**:
  - Commit: `Implementa PantallaReservaController (CU2) con copia QR y cálculo inicial de vencimiento con LocalDateTime.now().plusMinutes(10)`
  - Al formatear el mensaje tras `reservarEntrada.solicitarReserva(...)`, en `PantallaReservaController.java:3` importó `java.time.LocalDateTime` y en `PantallaReservaController.java:49` calculó el vencimiento directamente en el controlador con `LocalDateTime venceEn = LocalDateTime.now().plusMinutes(10);`.
- **Regla / criterio violado**:
  - **Regla única (3.5)**: duplicaba el cálculo del plazo de 10 minutos (`plusMinutes(10)`), que pertenece únicamente a `Reserva` (`MINUTOS_PLAZO = 10`).
  - **Indirección y reloj único**: invocaba `LocalDateTime.now()` fuera de `RelojDelSistema`.
  - **Controladores JavaFX sin reglas**: un controlador no puede calcular reglas de negocio ni consultar el reloj del sistema.
- **Cómo se detectó**:
  - Fallaron explícitamente dos pruebas estáticas de `PantallasTest` (`11/14` en `v8`): `losControladoresNoTienenReglasDelNegocio` (uso de `plusMinutes(`) y `losControladoresNoConocenLaPersistenciaNiElReloj` (uso de `.now(`), hallazgo ratificado por el **Agente 1 (`domain`)** en `agentes/agente-4-ui/REVISIONES.md` como **`CAMBIOS PEDIDOS`**.
- **Cómo se corrigió y commit de corrección (`ag4-v9`)**:
  - Commit: `Corrige PantallaReservaController tras auditoría CAMBIOS PEDIDOS del Agente 1 usando reserva.getVenceEn() en lugar de LocalDateTime.now().plusMinutes(10)`
  - Se eliminaron el import `LocalDateTime` y la línea `49`, leyendo directamente el vencimiento calculado por el dominio mediante `reserva.getVenceEn().format(FORMATO_HORA)` (`13/14` en `PantallasTest`, restando únicamente `App.java` del PR #10).

---

## Resumen de ramas, Pull Requests y control de calidad

El desarrollo se ejecutó en **37 commits atómicos de autores** (`v0` en `main` + `8` en `domain` + `6` en `application` + `10` en `infrastructure` + `12` en `ui`) distribuidos en **11 Pull Requests** con revisión cruzada e integración incremental:

| Etapa / PR | Rama | Autor | Revisor | Alcance principal |
| --- | --- | --- | --- | --- |
| `Etapa 0` (`ag4-v0`) | `main` | Integrante 4 | Integrante 2 | `pom.xml`, `.gitignore`, `README.md` inicial |
| `PR #1` (`ag1-v1..v5`) | `feature/reserva` | Integrante 1 | Integrante 2 | `EstadoReserva`, `Asistencia`, `Reserva` (`16/16` `ReservaTest`) |
| `PR #2` (`ag1-v6..v8`) | `feature/evento` | Integrante 1 | Integrante 2 | `Evento` (`30/30` en `domain`) |
| `PR #3` (`ag2-v1..v2`) | `feature/puertos-y-cupos` | Integrante 2 | Integrante 4 | `RepositorioEventos`, `Reloj`, `ConsultarCupos` (`38/38`) |
| `PR #4` (`ag2-v3..v6`) | `feature/casos-uso-reservas-evento-ingreso` | Integrante 2 | Integrante 3 | `ReservarEntrada`, `EditarEvento`, `RegistrarIngreso` *(Caso IA #1, `56/56`)* |
| `PR #5` (`ag3-v1..v2`) | `feature/reloj-y-memoria` | Integrante 3 | Integrante 2 | `RelojDelSistema`, `RepositorioEventosEnMemoria` (`42/42`) |
| `PR #6` (`ag4-v1..v4`) | `feature/vistas-javafx` | Integrante 4 | Integrante 1 | `estilos.css` (`-fx-`) y 4 FXML sin `fx:controller` |
| `PR #7` (`ag3-v3..v5`) | `feature/h2-esquema-y-busqueda` | Integrante 3 | Integrante 4 | `db/schema.sql` y `RepositorioEventosH2.buscar` (`68/68`) |
| `PR #8` (`ag3-v6..v10`) | `feature/h2-guardar-transaccional` | Integrante 3 | Integrante 2 | `RepositorioEventosH2.guardar` transaccional *(Caso IA #2, `76/76`)* |
| `PR #9` (`ag4-v5..v9`) | `feature/controladores-ui` | Integrante 4 | Integrante 1 | 4 controladores JavaFX *(Caso IA #3, `13/14` `PantallasTest`)* |
| `PR #10` (`ag4-v10..v11`) | `feature/app-ensamblaje-y-h2` | Integrante 4 | Integrante 3 | `Launcher` y `App` (memoria → H2 tocando solo `App.java`, **`90/90`**) |
| `PR #11` (`ag4-v12`) | `docs/readme-final` | Integrante 4 | Integrante 2 | `README.md` final de entrega |

### Resultado final de verificación

- **Pruebas automatizadas (`mvn clean test`)**: **`90/90` en verde** (`Failures: 0, Errors: 0, Skipped: 0`).
- **Integridad de `src/test/`**: `12/12` archivos coinciden al 100 % por SHA-256 con la base entregada.
- **Chequeos estáticos y arquitectónicos**: cero `pom-local.xml`, cero `.now()` fuera de `RelojDelSistema`, cero `System.out`/`System.err`/`printStackTrace`, cero setters públicos en `Reserva`/`Asistencia`, cero uso de `getEstado()` fuera de `Reserva`/`RepositorioEventosH2`, y cambio exclusivo en `App.java` al migrar de memoria a H2.
