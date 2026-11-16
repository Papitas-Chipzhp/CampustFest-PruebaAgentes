# Guía de Trabajo y Entrega en GitHub — `CampusFest_Grupo_10`

> **Propósito**: Pasos exactos y autosuficientes para que los **4 integrantes del equipo** suban todo el código de **CampusFest** a su repositorio **`CampusFest_Grupo_10`** (`main` ya contiene `CONTRATO.md`, `pom.xml`, `.gitignore`, `README.md` inicial y las 12 pruebas en `src/test/`). Todo el código fuente de referencia está publicado en **`https://github.com/Papitas-Chipzhp/CampustFest-PruebaAgentes.git`**, así que los 4 compañeros tienen acceso directo a los mismos archivos sin depender de ZIPs locales.

---

## 1. Datos generales y reparto del equipo

| Campo | Detalle |
| --- | --- |
| **Repositorio oficial de entrega** | `https://github.com/Papitas-Chipzhp/CampusFest_Grupo_10.git` *(ya tiene la base en `main`)* |
| **Repositorio fuente de consulta** | `https://github.com/Papitas-Chipzhp/CampustFest-PruebaAgentes.git` *(código validado `90/90` + rama/release `guia-equipo` con esta guía)* |
| **Flujo desde el estado actual** | **`PR #1` a `PR #7`** (`7` Pull Requests limpios entre los 4 integrantes) |
| **Regla de fusión** | Fusionar cada PR con **`Create a merge commit`** (nunca *Squash* ni *Rebase*) |
| **Integridad de pruebas** | Los 12 archivos de `src/test/` ya están en `main` y no se modifican nunca (`90/90`) |

| Rol | Módulo que implementa (PRs) | Qué revisa en GitHub |
| --- | --- | --- |
| **Persona 1** | `domain` — **`PR #1`** (`feature/dominio`, 3 commits) | Revisa `PR #4` (vistas) y **`PR #5` (controladores + Caso 2 de 8.2)** |
| **Persona 2** | `application` — **`PR #2`** (`feature/casos-de-uso`, 4 commits + Caso 1 de 8.2) | Revisa **`PR #1`** (dominio) y **`PR #7`** (`README.md`) |
| **Persona 3** | `infrastructure` — **`PR #3`** (`feature/persistencia-y-reloj`, 3 commits) | Revisa **`PR #2`** (casos de uso) y **`PR #6` (Aspecto 6.5)** |
| **Persona 4** | `ui` + Integración — **`PR #4`**, **`PR #5`**, **`PR #6`**, **`PR #7`** | Revisa **`PR #3`** (H2) y configura protección de `main` |

---

## 2. Preparación inicial en cada PC (una sola vez antes de `PR #1`)

1. **Verificar reglas en GitHub (`CampusFest_Grupo_10`)**:
   - En **Settings → Collaborators**, confirmar que los 4 integrantes aceptaron la invitación.
   - En **Settings → General → Pull Requests**, dejar activo únicamente **Allow merge commits**.
   - En **Settings → Rules → Rulesets**, dejar el ruleset de `main` en **`Active`** exigiendo **1 aprobación** por Pull Request, **Dismiss stale pull request approvals**, y solo **`Merge`**.

2. **Clonar los dos repositorios en carpetas separadas**:
   - **`C:\Referencia\CampustFest-PruebaAgentes`** → solo para leer y copiar el código fuente (`$SRC`). **Nunca hagan `git commit` ni `git push` aquí.**
   - **`C:\Trabajo\CampusFest_Grupo_10`** → su repositorio oficial de trabajo donde crean ramas, commits, push y PRs.

   ```powershell
   mkdir C:\Referencia, C:\Trabajo -ErrorAction SilentlyContinue

   # 1. Clonar repositorio de referencia (código completo ya verificado 90/90)
   git clone https://github.com/Papitas-Chipzhp/CampustFest-PruebaAgentes.git C:\Referencia\CampustFest-PruebaAgentes
   $SRC = "C:\Referencia\CampustFest-PruebaAgentes"

   # 2. Clonar el repositorio oficial del equipo
   git clone https://github.com/Papitas-Chipzhp/CampusFest_Grupo_10.git C:\Trabajo\CampusFest_Grupo_10
   cd C:\Trabajo\CampusFest_Grupo_10

   # 3. Cada compañero configura SU nombre y correo real de su cuenta de GitHub:
   git config user.name "<Tu Nombre Real>"
   git config user.email "<tu-correo-de-github>"
   ```

> **Regla antes de iniciar cada rama**: ejecuten siempre `git switch main && git pull origin main`.  
> **Nota sobre numeración**: si algún compañero abre un PR en paralelo antes que otro (por ejemplo Persona 4 adelantando `feature/vistas-javafx` mientras Persona 1 hace `feature/dominio`), simplemente cambian el orden numérico y al final ajustan los `#` de PR en el `README.md` del último PR.

---

## 3. Paso a paso desde `PR #1` hasta `PR #7`

### `PR #1` — `feature/dominio` (Persona 1 · 3 commits · Revisa: Persona 2)

```powershell
git switch main && git pull origin main
git switch -c feature/dominio

$DOM = "src\main\java\co\edu\unillanos\campusfest\domain"
New-Item -ItemType Directory -Force $DOM | Out-Null

# Commit 1: EstadoReserva y Asistencia
Copy-Item "$SRC\$DOM\EstadoReserva.java" "$DOM\" -Force
Copy-Item "$SRC\$DOM\Asistencia.java" "$DOM\" -Force
mvn clean compile
git add .
git commit -m "Agrega enum EstadoReserva y entidad inmutable Asistencia"

# Commit 2: Reserva
Copy-Item "$SRC\$DOM\Reserva.java" "$DOM\" -Force
mvn clean compile
git add .
git commit -m "Implementa entidad Reserva con regla de vigencia de 10 minutos"

# Commit 3: Evento
Copy-Item "$SRC\$DOM\Evento.java" "$DOM\" -Force
mvn clean compile
git add .
git commit -m "Implementa entidad Evento con cupos disponibles, reservas y cambio de aforo"

git push -u origin feature/dominio
```

- **En GitHub**: Persona 1 abre el PR (`feature/dominio → main`) y lo asigna a **Persona 2**. Persona 2 comprueba que `domain` no usa `LocalDateTime.now()`, que `Asistencia` es inmutable, que `Reserva.confirmar` no muta estado al vencer y que `Evento.getReservas()` devuelve vista inmodificable, marca **Approve** y fusionan con **Create a merge commit**.

---

### `PR #2` — `feature/casos-de-uso` (Persona 2 · 4 commits · Revisa: Persona 3 · **Caso 1 Aspecto 8.2**)

**Ronda 1 — Persona 2 sube puertos y casos de uso, incluyendo la propuesta inicial de `RegistrarIngreso.java`:**

```powershell
git switch main && git pull origin main
git switch -c feature/casos-de-uso

$PORT = "src\main\java\co\edu\unillanos\campusfest\application\port"
$UC   = "src\main\java\co\edu\unillanos\campusfest\application\usecase"
New-Item -ItemType Directory -Force $PORT, $UC | Out-Null

# Commit 1: Puertos
Copy-Item "$SRC\$PORT\*.java" "$PORT\" -Force
mvn clean compile
git add .
git commit -m "Define puertos RepositorioEventos y Reloj en application.port"

# Commit 2: ConsultarCupos y ReservarEntrada
Copy-Item "$SRC\$UC\ConsultarCupos.java" "$UC\" -Force
Copy-Item "$SRC\$UC\ReservarEntrada.java" "$UC\" -Force
mvn clean compile
git add .
git commit -m "Implementa casos de uso ConsultarCupos y ReservarEntrada"

# Commit 3: EditarEvento y primera versión de RegistrarIngreso (validación redundante — Aspecto 8.2)
Copy-Item "$SRC\$UC\EditarEvento.java" "$UC\" -Force
$codigoRI = @'
package co.edu.unillanos.campusfest.application.usecase;

import co.edu.unillanos.campusfest.application.port.Reloj;
import co.edu.unillanos.campusfest.application.port.RepositorioEventos;
import co.edu.unillanos.campusfest.domain.Asistencia;
import co.edu.unillanos.campusfest.domain.EstadoReserva;
import co.edu.unillanos.campusfest.domain.Evento;
import co.edu.unillanos.campusfest.domain.Reserva;

/**
 * Caso de uso CU4: registra el ingreso de un estudiante con reserva confirmada.
 */
public class RegistrarIngreso {

    private final RepositorioEventos repositorio;
    private final Reloj reloj;

    public RegistrarIngreso(RepositorioEventos repositorio, Reloj reloj) {
        if (repositorio == null) {
            throw new IllegalArgumentException("El repositorio de eventos es obligatorio.");
        }
        if (reloj == null) {
            throw new IllegalArgumentException("El reloj es obligatorio.");
        }
        this.repositorio = repositorio;
        this.reloj = reloj;
    }

    public Asistencia registrar(String eventoId, String codigo) {
        Evento evento = buscarEvento(eventoId);
        Reserva reserva = evento.buscarReservaPorCodigo(codigo);
        if (reserva.getEstado() != EstadoReserva.CONFIRMADA) {
            throw new IllegalStateException("Solo pueden ingresar reservas confirmadas.");
        }
        Asistencia asistencia = reserva.registrarIngreso(reloj.ahora());
        repositorio.guardar(evento);
        return asistencia;
    }

    private Evento buscarEvento(String eventoId) {
        if (eventoId == null || eventoId.isBlank()) {
            throw new IllegalArgumentException("El identificador del evento es obligatorio.");
        }
        Evento evento = repositorio.buscar(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("No existe un evento con el identificador indicado.");
        }
        return evento;
    }
}
'@
[IO.File]::WriteAllText("$PWD\$UC\RegistrarIngreso.java", $codigoRI, [Text.UTF8Encoding]::new($false))

mvn clean compile
git add .
git commit -m "Implementa casos de uso EditarEvento y RegistrarIngreso"
git push -u origin feature/casos-de-uso
```

- **Ronda 1 en GitHub — Revisión de Persona 3 (`Request changes`)**:
  > *"En `RegistrarIngreso.java` se importó `EstadoReserva` y se valida `reserva.getEstado() != EstadoReserva.CONFIRMADA` antes de llamar a `reserva.registrarIngreso(reloj.ahora())`. Esa regla ya vive dentro de `Reserva.registrarIngreso`, por lo que rompe GRASP Experto, encapsulamiento y regla única. Conviene eliminar esa validación del caso de uso y delegar directo en la entidad."*

**Ronda 2 — Persona 2 corrige copiando la versión limpia desde `$SRC`:**

```powershell
Copy-Item "$SRC\$UC\RegistrarIngreso.java" "$UC\" -Force
mvn clean compile
git add .
git commit -m "Delega validación de estado confirmado en Reserva dentro de RegistrarIngreso"
git push
```

- **En GitHub**: Persona 2 solicita re-revisión. Persona 3 confirma que ya no usa `EstadoReserva` ni `getEstado()`, marca **Approve** y fusionan con **Create a merge commit**.

---

### `PR #3` — `feature/persistencia-y-reloj` (Persona 3 · 3 commits · Revisa: Persona 4)

```powershell
git switch main && git pull origin main
git switch -c feature/persistencia-y-reloj

$REL = "src\main\java\co\edu\unillanos\campusfest\infrastructure\reloj"
$PER = "src\main\java\co\edu\unillanos\campusfest\infrastructure\persistence"
$DB  = "src\main\resources\db"
New-Item -ItemType Directory -Force $REL, $PER, $DB | Out-Null

# Commit 1: RelojDelSistema y RepositorioEventosEnMemoria
Copy-Item "$SRC\$REL\RelojDelSistema.java" "$REL\" -Force
Copy-Item "$SRC\$PER\RepositorioEventosEnMemoria.java" "$PER\" -Force
mvn clean compile
git add .
git commit -m "Implementa adaptadores RelojDelSistema y RepositorioEventosEnMemoria"

# Commit 2: Esquema SQL H2
Copy-Item "$SRC\$DB\schema.sql" "$DB\" -Force
mvn clean compile
git add .
git commit -m "Agrega esquema SQL inicial (EVENTO, RESERVA y ASISTENCIA) para H2"

# Commit 3: RepositorioEventosH2 (ya pasan las 76 pruebas de !PantallasTest)
Copy-Item "$SRC\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean test "-Dtest=!PantallasTest"
git add .
git commit -m "Implementa RepositorioEventosH2 con MERGE INTO, transacciones y manejo de SQLException"

git push -u origin feature/persistencia-y-reloj
```

- **En GitHub**: Persona 3 abre el PR y lo asigna a **Persona 4**. Persona 4 verifica `schema.sql` (PK/FK entre `EVENTO`, `RESERVA`, `ASISTENCIA`), transacciones `commit`/`rollback`/`finally`, traducción de `SQLException` sin exponer mensajes JDBC y `76/76` pruebas en verde (`!PantallasTest`), marca **Approve** y fusionan con **Create a merge commit**.

---

### `PR #4` — `feature/vistas-javafx` (Persona 4 · 2 commits · Revisa: Persona 1)

*(Puede adelantarse en paralelo en cualquier momento después de que `main` tiene la base, pues FXML y CSS no dependen de clases Java).*

```powershell
git switch main && git pull origin main
git switch -c feature/vistas-javafx

$VIS = "src\main\resources\co\edu\unillanos\campusfest\vistas"
New-Item -ItemType Directory -Force $VIS | Out-Null

Copy-Item "$SRC\$VIS\estilos.css" "$VIS\" -Force
Copy-Item "$SRC\$VIS\pantalla-cupos.fxml" "$VIS\" -Force
Copy-Item "$SRC\$VIS\pantalla-reserva.fxml" "$VIS\" -Force
mvn clean compile
git add .
git commit -m "Agrega hoja de estilos JavaFX (-fx-) y vistas de cupos y reservas"

Copy-Item "$SRC\$VIS\pantalla-evento.fxml" "$VIS\" -Force
Copy-Item "$SRC\$VIS\pantalla-ingreso.fxml" "$VIS\" -Force
mvn clean compile
git add .
git commit -m "Completa vistas FXML para edición de evento e ingreso QR"

git push -u origin feature/vistas-javafx
```

- **En GitHub**: Persona 4 abre el PR y lo asigna a **Persona 1**. Persona 1 verifica prefijos `-fx-` en `estilos.css`, ausencia de `fx:controller` y presencia de `etiquetaMensaje` en los 4 FXML, marca **Approve** y fusionan con **Create a merge commit**.

---

### `PR #5` — `feature/controladores-ui` (Persona 4 · 3 commits · Revisa: Persona 1 · **Caso 2 Aspecto 8.2**)

**Ronda 1 — Persona 4 sube los 4 controladores con la versión inicial de `PantallaReservaController.java`:**

```powershell
git switch main && git pull origin main
git switch -c feature/controladores-ui

$UI = "src\main\java\co\edu\unillanos\campusfest\infrastructure\ui"
New-Item -ItemType Directory -Force $UI | Out-Null

# Commit 1: PantallaCuposController y PantallaEventoController
Copy-Item "$SRC\$UI\PantallaCuposController.java" "$UI\" -Force
Copy-Item "$SRC\$UI\PantallaEventoController.java" "$UI\" -Force
mvn clean compile
git add .
git commit -m "Implementa controladores para consulta de cupos y edición de evento"

# Commit 2: PantallaIngresoController y versión inicial de PantallaReservaController (Caso 2 — Aspecto 8.2)
Copy-Item "$SRC\$UI\PantallaIngresoController.java" "$UI\" -Force
$codigoPR = @'
package co.edu.unillanos.campusfest.infrastructure.ui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.edu.unillanos.campusfest.application.usecase.ReservarEntrada;
import co.edu.unillanos.campusfest.domain.Reserva;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

/**
 * Controlador de la pantalla CU2 para solicitar y confirmar reservas temporales de entradas.
 */
public class PantallaReservaController {

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ReservarEntrada reservarEntrada;

    @FXML
    private TextField campoEventoId;

    @FXML
    private TextField campoEstudianteId;

    @FXML
    private TextField campoCodigoReserva;

    @FXML
    private Label etiquetaMensaje;

    public PantallaReservaController(ReservarEntrada reservarEntrada) {
        if (reservarEntrada == null) {
            throw new IllegalArgumentException("El caso de uso ReservarEntrada es obligatorio.");
        }
        this.reservarEntrada = reservarEntrada;
    }

    @FXML
    private void solicitarReserva() {
        try {
            String eventoId = campoEventoId.getText().trim();
            String estudianteId = campoEstudianteId.getText().trim();
            Reserva reserva = reservarEntrada.solicitarReserva(eventoId, estudianteId);
            campoCodigoReserva.setText(reserva.getCodigo());
            LocalDateTime venceEn = LocalDateTime.now().plusMinutes(10);
            mostrarExito("Reserva pendiente creada. Código: " + reserva.getCodigo()
                    + ". Vence: " + venceEn.format(FORMATO_HORA) + ".");
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void confirmarReserva() {
        try {
            String eventoId = campoEventoId.getText().trim();
            String codigoReserva = campoCodigoReserva.getText().trim();
            Reserva reserva = reservarEntrada.confirmarReserva(eventoId, codigoReserva);
            mostrarExito("Reserva confirmada para " + reserva.getEstudianteId()
                    + ". Código QR: " + reserva.getCodigo());
        } catch (IllegalArgumentException | IllegalStateException e) {
            mostrarError(e.getMessage());
        }
    }

    @FXML
    private void copiarCodigo() {
        String codigo = campoCodigoReserva.getText().trim();
        if (codigo.isBlank()) {
            mostrarError("No hay ningún código de reserva para copiar.");
            return;
        }
        ClipboardContent contenido = new ClipboardContent();
        contenido.putString(codigo);
        Clipboard.getSystemClipboard().setContent(contenido);
        mostrarExito("Código QR copiado al portapapeles: " + codigo);
    }

    @FXML
    private void limpiar() {
        campoEventoId.clear();
        campoEstudianteId.clear();
        campoCodigoReserva.clear();
        limpiarMensaje();
    }

    private void mostrarExito(String mensaje) {
        etiquetaMensaje.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        etiquetaMensaje.getStyleClass().add("mensaje-exito");
        etiquetaMensaje.setText(mensaje);
    }

    private void mostrarError(String mensaje) {
        etiquetaMensaje.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        etiquetaMensaje.getStyleClass().add("mensaje-error");
        etiquetaMensaje.setText(mensaje);
    }

    private void limpiarMensaje() {
        etiquetaMensaje.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        etiquetaMensaje.setText("");
    }
}
'@
[IO.File]::WriteAllText("$PWD\$UI\PantallaReservaController.java", $codigoPR, [Text.UTF8Encoding]::new($false))

mvn clean test "-Dtest=PantallasTest"   # Fallan 2 pruebas estáticas por LocalDateTime.now().plusMinutes(10) (+ App faltante)
git add .
git commit -m "Implementa controladores de ingreso QR y reserva de entrada"
git push -u origin feature/controladores-ui
```

- **Ronda 1 en GitHub — Revisión de Persona 1 (`Request changes`)**:
  > *"En `PantallaReservaController.java` se calculó el vencimiento con `LocalDateTime.now().plusMinutes(10)`. El plazo de 10 minutos pertenece a `Reserva` y la hora viene de `RelojDelSistema`; además fallan dos pruebas de `PantallasTest`. Cambia esa línea para usar directamente `reserva.getVenceEn().format(FORMATO_HORA)`."*

**Ronda 2 — Persona 4 corrige copiando la versión limpia desde `$SRC`:**

```powershell
Copy-Item "$SRC\$UI\PantallaReservaController.java" "$UI\" -Force
mvn clean test "-Dtest=PantallasTest"   # Ahora pasa 13/14 (solo falta App.java)
git add .
git commit -m "Usa reserva.getVenceEn() en PantallaReservaController para evitar lógica de negocio en UI"
git push
```

- **En GitHub**: Persona 4 solicita re-revisión. Persona 1 confirma `13/14` en `PantallasTest`, marca **Approve** y fusionan con **Create a merge commit**.

---

### `PR #6` — `feature/app-ensamblaje-h2` (Persona 4 · 2 commits · Revisa: Persona 3 · **Aspecto 6.5**)

```powershell
git switch main && git pull origin main
git switch -c feature/app-ensamblaje-h2

$RAIZ = "src\main\java\co\edu\unillanos\campusfest"
New-Item -ItemType Directory -Force $RAIZ | Out-Null

# Commit 1: Launcher + App con RepositorioEventosEnMemoria (89/90 en mvn clean test)
Copy-Item "$SRC\$RAIZ\Launcher.java" "$RAIZ\" -Force
Copy-Item "$SRC\$RAIZ\App.java" "$RAIZ\" -Force

$appMem = (Get-Content "$RAIZ\App.java" -Raw -Encoding UTF8)
$appMem = $appMem `
  -replace 'import java\.sql\..*?\r?\n', '' `
  -replace 'import co\.edu\.unillanos\.campusfest\.infrastructure\.persistence\.RepositorioEventosH2;', 'import co.edu.unillanos.campusfest.infrastructure.persistence.RepositorioEventosEnMemoria;' `
  -replace 'private Connection conexion;\r?\n\r?\n', '' `
  -replace 'conexion = abrirConexion\(\);\r?\n\s*RepositorioEventos repositorio = new RepositorioEventosH2\(conexion\);', 'RepositorioEventos repositorio = new RepositorioEventosEnMemoria();' `
  -replace '(?s)@Override\s+public void stop\(\).*?private Connection abrirConexion\(\).*?\r?\n    \}', ''
[IO.File]::WriteAllText("$PWD\$RAIZ\App.java", $appMem, [Text.UTF8Encoding]::new($false))

mvn clean test   # 89/90 (falla solo appEnsamblaH2ElRelojYLasCuatroPantallas)
git add .
git commit -m "Ensambla aplicación JavaFX con repositorio en memoria"

# Commit 2: Restaura ÚNICAMENTE App.java final con RepositorioEventosH2 (90/90 en verde!)
Copy-Item "$SRC\$RAIZ\App.java" "$RAIZ\" -Force
git diff --stat  # Debe imprimir únicamente App.java
mvn clean test   # 90/90 BUILD SUCCESS
git add .
git commit -m "Conecta App a RepositorioEventosH2 sin modificar dominio ni casos de uso"

git push -u origin feature/app-ensamblaje-h2
```

- **En GitHub**: Persona 4 abre el PR y lo asigna a **Persona 3**. Persona 3 comprueba en la pestaña *Commits* que el segundo commit **solo tocó `App.java`** (Aspecto 6.5) y que `mvn clean test` da **`90/90`**, marca **Approve** y fusionan con **Create a merge commit**.

---

### `PR #7` — `docs/readme-final` (Persona 4 · 1 commit · Revisa: Persona 2)

```powershell
git switch main && git pull origin main
git switch -c docs/readme-final

Copy-Item "$SRC\README.md" "README.md" -Force
```

> **¡Antes del commit!** Abran `README.md`, reemplacen `<Nombre Integrante 1..4>` y `<@usuario-github-1..4>` por sus nombres y usuarios reales de GitHub, y confirmen que los `#` de PR (`PR #2`, `PR #5`, `PR #6`) coincidan con los números reales que quedaron en `CampusFest_Grupo_10`.

```powershell
mvn clean test
git add README.md
git commit -m "Completa README final con integrantes, guía de ejecución y uso de IA"
git push -u origin docs/readme-final
```

- **En GitHub**: Persona 4 abre el PR y lo asigna a **Persona 2**. Persona 2 verifica nombres reales, coherencia de los casos 8.1/8.2 y `mvn clean test` en **`90/90`**, marca **Approve** y fusionan con **Create a merge commit**.

---

## 4. Verificación final antes de entregar el enlace en Moodle

```powershell
git clone https://github.com/Papitas-Chipzhp/CampusFest_Grupo_10.git C:\Trabajo\verificacion-final
cd C:\Trabajo\verificacion-final

mvn clean test                        # Obligatorio: 90/90 BUILD SUCCESS
git shortlog -sn --no-merges HEAD     # Deben aparecer los 4 integrantes con commits de código
git log --stat -n 6                   # Verificar cambio exclusivo de App.java en el commit de H2
mvn javafx:run                        # Abrir la app y probar las 4 pestañas con H2
```

| Pestaña | Prueba rápida en `mvn javafx:run` | Resultado esperado |
| --- | --- | --- |
| **Editar evento** | Al abrir precarga `ev-1`; intentar guardar aforo `0` | Mensaje verde al cargar; mensaje rojo completo sin texto JDBC al rechazar aforo |
| **Consultar cupos** | Consultar `ev-1` y `ev-2`; pulsar **Limpiar** | Muestra cupos disponibles en verde; **Limpiar** vacía campo y mensaje |
| **Reservar entrada** | Solicitar reserva en `ev-1`, **Copiar código QR** y **Confirmar reserva** | Genera código y vencimiento (+10 min); copia al portapapeles; confirma en verde |
| **Ingreso QR** | Pegar QR (`Ctrl+V`), registrar ingreso, reintentar y reiniciar app | Primer intento en verde; segundo rechazado en rojo; persiste tras reiniciar |
