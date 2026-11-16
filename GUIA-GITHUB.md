# Guía de Trabajo y Entrega en GitHub — `CampusFest_Grupo_10` (Plan Original · 11 PRs)

> **Estado actual del repositorio `CampusFest_Grupo_10`**:
> - `main` ya contiene `CONTRATO.md`, `pom.xml`, `.gitignore`, `README.md` inicial y los **12 archivos originales de `src/test/`**.
> - **`PR #1` (`feature/vistas-javafx`) ya fue subida por Persona 4** y espera la aprobación (**Approve + Create a merge commit**) de **Persona 1**.
> - Todo el código fuente (versión final `90/90` y cada paso intermedio en `versiones/`) está publicado en **`https://github.com/Papitas-Chipzhp/CampustFest-PruebaAgentes.git`** para que los 4 compañeros tengan acceso directo a los mismos archivos sin depender de carpetas locales.

---

## 1. Datos generales y reparto equitativo del equipo

| Campo | Detalle |
| --- | --- |
| **Repositorio oficial de entrega** | `https://github.com/Papitas-Chipzhp/CampusFest_Grupo_10.git` |
| **Repositorio fuente de consulta** | `https://github.com/Papitas-Chipzhp/CampustFest-PruebaAgentes.git` *(código `90/90` + carpeta `versiones/` + `guia/`)* |
| **Flujo completo del proyecto** | **`PR #1` a `PR #11`** (`11` Pull Requests · `34` commits entre los 4 integrantes · `3` casos reales de Aspecto 8.2) |
| **Regla de fusión** | Fusionar cada PR con **`Create a merge commit`** (nunca *Squash* ni *Rebase*) |
| **Integridad de pruebas** | Los 12 archivos de `src/test/` ya están en `main` y no se modifican nunca (`90/90`) |

| Rol | PRs que implementa | Commits | Qué revisa en GitHub |
| --- | --- | --- | --- |
| **Persona 1** (`domain`) | **`PR #2`** (`feature/reserva`) y **`PR #3`** (`feature/evento`) | **8** (`5 + 3`) | Revisa **`PR #1`** *(ya abierta)* y **`PR #9`** (controladores + Caso IA #3) |
| **Persona 2** (`application`) | **`PR #4`** (`feature/puertos-y-cupos`) y **`PR #5`** (`feature/casos-uso-reservas-evento-ingreso` + Caso IA #1) | **6** (`2 + 4`) | Revisa **`PR #2`**, **`PR #3`**, **`PR #6`**, **`PR #8`** (+ Caso IA #2) y **`PR #11`** |
| **Persona 3** (`infrastructure`) | **`PR #6`** (`feature/reloj-y-memoria`), **`PR #7`** (`feature/h2-esquema-y-busqueda`) y **`PR #8`** (`feature/h2-guardar-transaccional` + Caso IA #2) | **10** (`2 + 3 + 5`) | Revisa **`PR #5`** (+ Caso IA #1) y **`PR #10`** (Aspecto 6.5) |
| **Persona 4** (`ui` + Integración) | **`PR #1`** *(ya abierta)*, **`PR #9`** (`feature/controladores-ui` + Caso IA #3), **`PR #10`** (`feature/app-ensamblaje-y-h2`) y **`PR #11`** (`docs/readme-final`) | **10** (`2 + 5 + 2 + 1`) | Revisa **`PR #4`** (puertos/CU1) y **`PR #7`** (H2 búsqueda) |

### Orden de dependencias (qué puede ir en paralelo)

1. **Ahora mismo**: **Persona 1** revisa/aprueba **`PR #1`** (`feature/vistas-javafx`) y al mismo tiempo inicia **`PR #2`** (`feature/reserva`).
2. Tras fusionar `PR #2` → **Persona 1** hace **`PR #3`** (`feature/evento`).
3. Tras fusionar `PR #3` → **Persona 2** hace **`PR #4`** (`feature/puertos-y-cupos`).
4. Tras fusionar `PR #4`:
   - **Persona 2** continúa con **`PR #5`** (`feature/casos-uso-reservas-evento-ingreso`).
   - En paralelo, **Persona 3** inicia **`PR #6`** (`feature/reloj-y-memoria`).
5. Tras fusionar `PR #6` (y `PR #4`) → **Persona 3** hace **`PR #7`** (`feature/h2-esquema-y-busqueda`) y luego **`PR #8`** (`feature/h2-guardar-transaccional`).
6. Tras fusionar `PR #1` y `PR #5` → **Persona 4** hace **`PR #9`** (`feature/controladores-ui`).
7. Tras fusionar `PR #8` y `PR #9` → **Persona 4** hace **`PR #10`** (`feature/app-ensamblaje-y-h2`) y al final **`PR #11`** (`docs/readme-final`).

> **Nota sobre numeración en GitHub**: si dos personas abren sus PRs en paralelo (por ejemplo `PR #5` y `PR #6`), el orden numérico puede intercambiarse entre sí; simplemente confirmen que los números citados en `README.md` (`PR #11`) coincidan con los números reales de `CampusFest_Grupo_10`.

---

## 2. Preparación inicial en cada PC (una sola vez antes de empezar)

1. **Verificar configuración en GitHub (`CampusFest_Grupo_10`)**:
   - **Settings → Collaborators**: confirmar que los 4 integrantes ya aceptaron la invitación.
   - **Settings → General → Pull Requests**: dejar activo únicamente **Allow merge commits**.
   - **Settings → Rules → Rulesets**: confirmar que la regla de `main` está en **Active**, pidiendo **1 aprobación**, **Dismiss stale pull request approvals**, y solo **Merge**.

2. **Clonar los dos repositorios en carpetas separadas**:
   - **`C:\Referencia\CampustFest-PruebaAgentes`** → solo lectura para copiar archivos (`$SRC`). **Nunca hagan commits ni push ahí.**
   - **`C:\Trabajo\CampusFest_Grupo_10`** *(o su carpeta local equivalente)* → donde cada integrante crea sus ramas, commits y PRs.

   ```powershell
   mkdir C:\Referencia, C:\Trabajo -ErrorAction SilentlyContinue

   # 1. Clonar repositorio de referencia (código completo 90/90 + versiones paso a paso)
   git clone https://github.com/Papitas-Chipzhp/CampustFest-PruebaAgentes.git C:\Referencia\CampustFest-PruebaAgentes
   $SRC = "C:\Referencia\CampustFest-PruebaAgentes"

   # 2. Clonar el repositorio oficial del equipo
   git clone https://github.com/Papitas-Chipzhp/CampusFest_Grupo_10.git C:\Trabajo\CampusFest_Grupo_10
   cd C:\Trabajo\CampusFest_Grupo_10

   # 3. Cada compañero configura SU nombre y correo real de su cuenta de GitHub:
   git config user.name "<Tu Nombre Real>"
   git config user.email "<tu-correo-de-github>"
   ```

> **Regla antes de iniciar cada rama**: ejecuten siempre `git switch main && git pull origin main` (y definan `$SRC = "C:\Referencia\CampustFest-PruebaAgentes"` si abren una nueva terminal PowerShell).

---

## 3. Paso a paso desde `PR #1` hasta `PR #11`

### `PR #1` — `feature/vistas-javafx` *(Persona 4 · 2 commits · Ya abierta — Revisa: Persona 1)*

Esta rama ya fue subida a `CampusFest_Grupo_10` por **Persona 4** con los commits:
1. `Agrega hoja de estilos JavaFX (-fx-) y vistas de cupos y reservas` (`estilos.css`, `pantalla-cupos.fxml`, `pantalla-reserva.fxml`)
2. `Completa vistas FXML para edición de evento e ingreso QR` (`pantalla-evento.fxml`, `pantalla-ingreso.fxml`)

> **Revisión en GitHub — Persona 1 (`Approve`)**: entra a `PR #1` en `CampusFest_Grupo_10`, confirma en **Files changed** los prefijos `-fx-` en `estilos.css`, ausencia de `fx:controller` y existencia de `etiquetaMensaje` en los 4 FXML. Aprueba (**Approve**) y fusiona con **Create a merge commit**.

---

### `PR #2` — `feature/reserva` *(Persona 1 · 5 commits · Revisa: Persona 2)*

```powershell
git switch main && git pull origin main
git switch -c feature/reserva

$DOM = "src\main\java\co\edu\unillanos\campusfest\domain"
New-Item -ItemType Directory -Force $DOM | Out-Null

# Commit 1 (v1): EstadoReserva
Copy-Item "$SRC\versiones\persona-1\v1\$DOM\EstadoReserva.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Crea el enum EstadoReserva con los estados PENDIENTE, CONFIRMADA, VENCIDA y CANCELADA"

# Commit 2 (v2): Asistencia
Copy-Item "$SRC\versiones\persona-1\v2\$DOM\Asistencia.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Crea la entidad Asistencia con hora inmutable y validación de hora no nula"

# Commit 3 (v3): Base de Reserva
Copy-Item "$SRC\versiones\persona-1\v3\$DOM\Reserva.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Crea base de entidad Reserva con MINUTOS_PLAZO = 10, fábricas pendiente y reconstruir, y getters"

# Commit 4 (v4): Vigencia y ocupación de cupo en Reserva
Copy-Item "$SRC\versiones\persona-1\v4\$DOM\Reserva.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Agrega métodos estaVigente(ahora) y ocupaCupo(ahora) en Reserva"

# Commit 5 (v5): Confirmación sin mutar al vencer, cancelación e ingreso
Copy-Item "$SRC\versiones\persona-1\v5\$DOM\Reserva.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Agrega confirmar(ahora) sin mutar estado al vencer, cancelar() y registrarIngreso(ahora) en Reserva"

git push -u origin feature/reserva
```

> **Revisión en GitHub — Persona 2 (`Approve`)**: verifica que `domain` no importa paquetes externos ni llama a `LocalDateTime.now()`, `Asistencia` es inmutable y `Reserva.confirmar(ahora)` no cambia el estado si venció. Fusionar con **Create a merge commit**.

---

### `PR #3` — `feature/evento` *(Persona 1 · 3 commits · Revisa: Persona 2)*

*(Requiere `PR #2` fusionada en `main`.)*

```powershell
git switch main && git pull origin main
git switch -c feature/evento

$DOM = "src\main\java\co\edu\unillanos\campusfest\domain"

# Commit 1 (v6): Base de Evento
Copy-Item "$SRC\versiones\persona-1\v6\$DOM\Evento.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Crea base de entidad Evento con constructores, validaciones de datos/aforo y getters inmodificables"

# Commit 2 (v7): Cupos, reservas y búsqueda por código
Copy-Item "$SRC\versiones\persona-1\v7\$DOM\Evento.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Agrega contarOcupados, cuposDisponibles, reservar y buscarReservaPorCodigo en Evento"

# Commit 3 (v8): Edición de datos y aforo mínimo
Copy-Item "$SRC\versiones\persona-1\v8\$DOM\Evento.java" "$DOM\" -Force
mvn clean compile && git add .
git commit -m "Agrega cambiarDatos y cambiarAforo con validación de bloqueados más cupos ocupados"

git push -u origin feature/evento
```

> **Revisión en GitHub — Persona 2 (`Approve`)**: verifica `Evento.getReservas()` inmodificable (`List.copyOf`), cálculo de `cuposDisponibles` apoyado en `Reserva.ocupaCupo(ahora)` (GRASP Experto) y validación de aforo mínimo. Fusionar con **Create a merge commit**.

---

### `PR #4` — `feature/puertos-y-cupos` *(Persona 2 · 2 commits · Revisa: Persona 4)*

*(Requiere `PR #3` fusionada en `main`.)*

```powershell
git switch main && git pull origin main
git switch -c feature/puertos-y-cupos

$PORT = "src\main\java\co\edu\unillanos\campusfest\application\port"
$UC   = "src\main\java\co\edu\unillanos\campusfest\application\usecase"
New-Item -ItemType Directory -Force $PORT, $UC | Out-Null

# Commit 1 (v1): Puertos de salida
Copy-Item "$SRC\versiones\persona-2\v1\$PORT\*.java" "$PORT\" -Force
mvn clean compile && git add .
git commit -m "Crea los puertos de salida RepositorioEventos y Reloj en application.port"

# Commit 2 (v2): Caso de uso CU1
Copy-Item "$SRC\versiones\persona-2\v2\$UC\ConsultarCupos.java" "$UC\" -Force
mvn clean compile && git add .
git commit -m "Implementa caso de uso ConsultarCupos (CU1) con buscarEvento privado"

git push -u origin feature/puertos-y-cupos
```

> **Revisión en GitHub — Persona 4 (`Approve`)**: confirma que los puertos están en `application.port` y que `ConsultarCupos` solo coordina repositorio, reloj y entidad sin reglas repetidas. Fusionar con **Create a merge commit**.

---

### `PR #5` — `feature/casos-uso-reservas-evento-ingreso` *(Persona 2 · 4 commits · Revisa: Persona 3 · Caso IA #1 Aspecto 8.2)*

*(Requiere `PR #4` fusionada en `main`.)*

**Ronda 1**: Persona 2 sube CU2, CU3 y la primera propuesta de `RegistrarIngreso.java` (`v5`, con validación duplicada de estado):

```powershell
git switch main && git pull origin main
git switch -c feature/casos-uso-reservas-evento-ingreso

$UC = "src\main\java\co\edu\unillanos\campusfest\application\usecase"

# Commit 1 (v3): ReservarEntrada (CU2)
Copy-Item "$SRC\versiones\persona-2\v3\$UC\ReservarEntrada.java" "$UC\" -Force
mvn clean compile && git add .
git commit -m "Implementa caso de uso ReservarEntrada (CU2) con solicitarReserva y confirmarReserva"

# Commit 2 (v4): EditarEvento (CU3)
Copy-Item "$SRC\versiones\persona-2\v4\$UC\EditarEvento.java" "$UC\" -Force
mvn clean compile && git add .
git commit -m "Implementa caso de uso EditarEvento (CU3) con obtener y editarEvento"

# Commit 3 (v5): RegistrarIngreso con validación inicial de estado (Caso IA #1 — Aspecto 8.2)
Copy-Item "$SRC\versiones\persona-2\v5\$UC\RegistrarIngreso.java" "$UC\" -Force
mvn clean compile && git add .
git commit -m "Implementa RegistrarIngreso (CU4) con verificación previa de estado en aplicación"

git push -u origin feature/casos-uso-reservas-evento-ingreso
```

> **Ronda 1 — Revisión Persona 3 (`Request changes`)**: *«En `RegistrarIngreso.java` se importó `EstadoReserva` y se valida `reserva.getEstado() != EstadoReserva.CONFIRMADA` antes de llamar a `reserva.registrarIngreso(reloj.ahora())`. Esa regla ya vive dentro de `Reserva.registrarIngreso`, por lo que rompe GRASP Experto, encapsulamiento y regla única. Conviene eliminar esa validación del caso de uso y delegar directo en la entidad.»*

**Ronda 2**: Persona 2 copia la versión corregida (`v6`) y pide re-revisión:

```powershell
Copy-Item "$SRC\versiones\persona-2\v6\$UC\RegistrarIngreso.java" "$UC\" -Force
mvn clean compile && git add .
git commit -m "Delega validación de estado confirmado en Reserva dentro de RegistrarIngreso (Caso IA #1)"
git push
```

> **Ronda 2 — Revisión Persona 3 (`Approve`)**: confirma que ya no importa `EstadoReserva` ni consulta `getEstado()`, delegando en `Reserva.registrarIngreso(reloj.ahora())`. Fusionar con **Create a merge commit**.

---

### `PR #6` — `feature/reloj-y-memoria` *(Persona 3 · 2 commits · Revisa: Persona 2)*

*(Puede trabajarse en paralelo apenas se fusione `PR #4`.)*

```powershell
git switch main && git pull origin main
git switch -c feature/reloj-y-memoria

$REL = "src\main\java\co\edu\unillanos\campusfest\infrastructure\reloj"
$PER = "src\main\java\co\edu\unillanos\campusfest\infrastructure\persistence"
New-Item -ItemType Directory -Force $REL, $PER | Out-Null

# Commit 1 (v1): RelojDelSistema
Copy-Item "$SRC\versiones\persona-3\v1\$REL\RelojDelSistema.java" "$REL\" -Force
mvn clean compile && git add .
git commit -m "Implementa adaptador RelojDelSistema en infrastructure.reloj retornando LocalDateTime.now()"

# Commit 2 (v2): RepositorioEventosEnMemoria
Copy-Item "$SRC\versiones\persona-3\v2\$PER\RepositorioEventosEnMemoria.java" "$PER\" -Force
mvn clean compile && git add .
git commit -m "Implementa adaptador RepositorioEventosEnMemoria en infrastructure.persistence"

git push -u origin feature/reloj-y-memoria
```

> **Revisión en GitHub — Persona 2 (`Approve`)**: confirma que `RelojDelSistema` implementa `Reloj` y `RepositorioEventosEnMemoria` implementa `RepositorioEventos` con manejo limpio de nulos. Fusionar con **Create a merge commit**.

---

### `PR #7` — `feature/h2-esquema-y-busqueda` *(Persona 3 · 3 commits · Revisa: Persona 4)*

*(Requiere `PR #6` fusionada en `main`.)*

```powershell
git switch main && git pull origin main
git switch -c feature/h2-esquema-y-busqueda

$PER = "src\main\java\co\edu\unillanos\campusfest\infrastructure\persistence"
$DB  = "src\main\resources\db"
New-Item -ItemType Directory -Force $PER, $DB | Out-Null

# Commit 1 (v3): Script schema.sql
Copy-Item "$SRC\versiones\persona-3\v3\$DB\schema.sql" "$DB\" -Force
mvn clean compile && git add .
git commit -m "Crea script SQL de esquema H2 en db/schema.sql con tablas EVENTO, RESERVA y ASISTENCIA"

# Commit 2 (v4): Constructor e inicialización idempotente de RepositorioEventosH2
Copy-Item "$SRC\versiones\persona-3\v4\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean compile && git add .
git commit -m "Crea RepositorioEventosH2 con inicialización idempotente de esquema desde schema.sql"

# Commit 3 (v5): Método buscar(eventoId) con LEFT JOIN ASISTENCIA
Copy-Item "$SRC\versiones\persona-3\v5\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean compile && git add .
git commit -m "Implementa buscar(eventoId) en RepositorioEventosH2 reconstruyendo reservas y asistencias"

git push -u origin feature/h2-esquema-y-busqueda
```

> **Revisión en GitHub — Persona 4 (`Approve`)**: verifica PK/FK entre `EVENTO`, `RESERVA` y `ASISTENCIA` en `schema.sql`, reconstrucción con `Evento.reconstruir` / `Reserva.reconstruir` y lectura segura. Fusionar con **Create a merge commit**.

---

### `PR #8` — `feature/h2-guardar-transaccional` *(Persona 3 · 5 commits · Revisa: Persona 2 · Caso IA #2 Aspecto 8.2)*

*(Requiere `PR #5` y `PR #7` fusionadas en `main`, para poder correr las `76` pruebas no-UI.)*

**Ronda 1**: Persona 3 sube `MERGE INTO` y el manejo transaccional con concatenación inicial de `e.getMessage()` JDBC (`v8`):

```powershell
git switch main && git pull origin main
git switch -c feature/h2-guardar-transaccional

$PER = "src\main\java\co\edu\unillanos\campusfest\infrastructure\persistence"

# Commit 1 (v6): guardarEvento y guardarReserva con MERGE INTO
Copy-Item "$SRC\versiones\persona-3\v6\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean compile && git add .
git commit -m "Implementa guardarEvento y guardarReserva en RepositorioEventosH2 usando MERGE INTO"

# Commit 2 (v7): guardarAsistencia con MERGE INTO
Copy-Item "$SRC\versiones\persona-3\v7\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean compile && git add .
git commit -m "Implementa guardarAsistencia en RepositorioEventosH2 usando MERGE INTO KEY"

# Commit 3 (v8): Transacciones commit/rollback concatenando detalle JDBC (Caso IA #2 — Aspecto 8.2)
Copy-Item "$SRC\versiones\persona-3\v8\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean test "-Dtest=!PantallasTest"   # 76/76 pruebas pasan, pero expone texto JDBC en catch
git add .
git commit -m "Agrega transacciones commit/rollback en guardar(evento) de RepositorioEventosH2"

git push -u origin feature/h2-guardar-transaccional
```

> **Ronda 1 — Revisión Persona 2 (`Request changes`)**: *«En `RepositorioEventosH2.java`, los bloques `catch (SQLException e)` concatenan `+ e.getMessage()` al construir `IllegalStateException`. Eso filtra detalles internos de SQL/JDBC hacia la aplicación y la pantalla, violando el Aspecto 6.4. Conserva `e` únicamente como causa y muestra mensajes claros en español.»*

**Ronda 2**: Persona 3 aplica la corrección (`v9` + refinamiento `v10`, pasando `76/76` en `!PantallasTest`):

```powershell
# Commit 4 (v9): Elimina e.getMessage() JDBC de los catch
Copy-Item "$SRC\versiones\persona-3\v9\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean compile && git add .
git commit -m "Elimina exposición de mensajes JDBC en RepositorioEventosH2 (Caso IA #2 / Aspecto 6.4)"

# Commit 5 (v10): Refinamiento final y validación 76/76
Copy-Item "$SRC\versiones\persona-3\v10\$PER\RepositorioEventosH2.java" "$PER\" -Force
mvn clean test "-Dtest=!PantallasTest"   # Obligatorio: 76/76 BUILD SUCCESS
git add .
git commit -m "Consolida validaciones de nulidad y lectura de schema.sql en RepositorioEventosH2"

git push
```

> **Ronda 2 — Revisión Persona 2 (`Approve`)**: confirma ausencia de `e.getMessage()` SQL hacia el usuario, `commit`/`rollback`/`finally` correctos y **`76/76`** pruebas en verde. Fusionar con **Create a merge commit**.

---

### `PR #9` — `feature/controladores-ui` *(Persona 4 · 5 commits · Revisa: Persona 1 · Caso IA #3 Aspecto 8.2)*

*(Requiere `PR #1` y `PR #5` fusionadas en `main`.)*

**Ronda 1**: Persona 4 sube los 4 controladores con el cálculo inicial `LocalDateTime.now().plusMinutes(10)` en `PantallaReservaController.java` (`v8`, haciendo fallar `2` pruebas estáticas de `PantallasTest`):

```powershell
git switch main && git pull origin main
git switch -c feature/controladores-ui

$UI = "src\main\java\co\edu\unillanos\campusfest\infrastructure\ui"
New-Item -ItemType Directory -Force $UI | Out-Null

# Commit 1: PantallaCuposController (CU1)
Copy-Item "$SRC\src\main\java\co\edu\unillanos\campusfest\infrastructure\ui\PantallaCuposController.java" "$UI\" -Force
mvn clean compile && git add .
git commit -m "Implementa PantallaCuposController (CU1) con alternancia éxito/error y limpieza rápida"

# Commit 2: PantallaEventoController (CU3)
Copy-Item "$SRC\src\main\java\co\edu\unillanos\campusfest\infrastructure\ui\PantallaEventoController.java" "$UI\" -Force
mvn clean compile && git add .
git commit -m "Implementa PantallaEventoController (CU3) con precarga de ev-1 en initialize()"

# Commit 3: PantallaIngresoController (CU4)
Copy-Item "$SRC\src\main\java\co\edu\unillanos\campusfest\infrastructure\ui\PantallaIngresoController.java" "$UI\" -Force
mvn clean compile && git add .
git commit -m "Implementa PantallaIngresoController (CU4) para registro de ingreso QR"

# Commit 4 (v8): PantallaReservaController calculando vencimiento en UI (Caso IA #3 — Aspecto 8.2)
Copy-Item "$SRC\versiones\persona-4\v8\$UI\PantallaReservaController.java" "$UI\" -Force
mvn clean test "-Dtest=PantallasTest"   # Esperado: 11/14 (fallan 2 reglas estáticas en PantallaReservaController + App faltante)
git add .
git commit -m "Implementa PantallaReservaController (CU2) con copia QR y cálculo inicial de vencimiento"

git push -u origin feature/controladores-ui
```

> **Ronda 1 — Revisión Persona 1 (`Request changes`)**: *«En `PantallaReservaController.java` se calculó el vencimiento con `LocalDateTime.now().plusMinutes(10)`. El plazo de 10 minutos pertenece a `Reserva` y la hora viene de `RelojDelSistema`; además fallan dos pruebas estáticas de `PantallasTest`. Cambia esa línea para leer directamente `reserva.getVenceEn().format(FORMATO_HORA)`.»*

**Ronda 2**: Persona 4 copia el controlador corregido desde `$SRC\src\...` (`PantallasTest` sube a `13/14`, restando solo `App.java`):

```powershell
Copy-Item "$SRC\src\main\java\co\edu\unillanos\campusfest\infrastructure\ui\PantallaReservaController.java" "$UI\" -Force
mvn clean test "-Dtest=PantallasTest"   # 13/14 en verde (solo falta App.java)
git add .
git commit -m "Usa reserva.getVenceEn() en PantallaReservaController para evitar lógica de negocio en UI (Caso IA #3)"
git push
```

> **Ronda 2 — Revisión Persona 1 (`Approve`)**: confirma la lectura de `reserva.getVenceEn()` sin importar `LocalDateTime`, y `13/14` en `PantallasTest`. Fusionar con **Create a merge commit**.

---

### `PR #10` — `feature/app-ensamblaje-y-h2` *(Persona 4 · 2 commits · Revisa: Persona 3 · Aspecto 6.5)*

*(Requiere `PR #8` y `PR #9` fusionadas en `main`.)*

**Obligatorio por el Aspecto 6.5**: el primer commit ensambla con `RepositorioEventosEnMemoria` (`89/90`) y el segundo cambia **únicamente `App.java`** a `RepositorioEventosH2` (`90/90`).

```powershell
git switch main && git pull origin main
git switch -c feature/app-ensamblaje-y-h2

$RAIZ = "src\main\java\co\edu\unillanos\campusfest"

# Commit 1 (v10): Launcher + App ensamblado con RepositorioEventosEnMemoria (89/90 en mvn clean test)
Copy-Item "$SRC\$RAIZ\Launcher.java" "$RAIZ\" -Force
Copy-Item "$SRC\versiones\persona-4\v10\$RAIZ\App.java" "$RAIZ\" -Force
mvn clean test   # Esperado: 89/90 (solo falla appEnsamblaH2ElRelojYLasCuatroPantallas)
git add .
git commit -m "Crea Launcher y ensamblaje inicial de App en memoria con precarga de ev-1 y ev-2"

# Commit 2 (v11): Reemplaza ÚNICAMENTE App.java por la versión final con H2 (90/90 en verde!)
Copy-Item "$SRC\$RAIZ\App.java" "$RAIZ\" -Force
git diff --stat   # Debe mostrar únicamente App.java
mvn clean test    # Obligatorio: 90/90 BUILD SUCCESS
git add .
git commit -m "Conecta App con RepositorioEventosH2 (Aspecto 6.5) sin modificar dominio ni casos de uso"

git push -u origin feature/app-ensamblaje-y-h2
```

> **Revisión en GitHub — Persona 3 (`Approve`)**: verifica en la pestaña **Commits** del PR que el segundo commit modificó únicamente `App.java` (Aspecto 6.5) y que `mvn clean test` pasa **`90/90`**. Fusionar con **Create a merge commit**.

---

### `PR #11` — `docs/readme-final` *(Persona 4 · 1 commit · Revisa: Persona 2)*

```powershell
git switch main && git pull origin main
git switch -c docs/readme-final

Copy-Item "$SRC\README.md" "README.md" -Force
# IMPORTANTE: abrir README.md, poner los 4 nombres y usuarios reales de GitHub
# y confirmar que los # de PR en Aspectos 8.2 y 6.5 coinciden con los reales de CampusFest_Grupo_10.
mvn clean test
git add README.md
git commit -m "Completa README.md final con integrantes, guía de ejecución y declaración de IA (8.1 y 8.2)"
git push -u origin docs/readme-final
```

> **¡Antes del commit!** Abran `README.md`, reemplacen `<Nombre Integrante 1..4>` y `<@usuario-github-1..4>` por sus datos reales, y confirmen que los números de PR citados (`PR #5`, `PR #8`, `PR #9`, `PR #10`) coinciden con los PRs reales de `CampusFest_Grupo_10`.

> **Revisión en GitHub — Persona 2 (`Approve`)**: confirma nombres reales, coherencia de los casos `8.1`/`8.2` y `mvn clean test` en **`90/90`**. Fusionar con **Create a merge commit**.

---

## 4. Verificación final antes de entregar el enlace en Moodle

```powershell
git clone https://github.com/Papitas-Chipzhp/CampusFest_Grupo_10.git C:\Trabajo\verificacion-final
cd C:\Trabajo\verificacion-final

mvn clean test                         # Obligatorio: 90/90 BUILD SUCCESS
git shortlog -sn --no-merges HEAD      # Deben aparecer los 4 integrantes con commits de código
git log --stat -n 6                    # Verificar cambio exclusivo de App.java en PR #10 y 34 commits de autores
mvn javafx:run                         # Abrir la app y probar las 4 pestañas con H2
```

| Pestaña | Prueba rápida en `mvn javafx:run` | Resultado esperado |
| --- | --- | --- |
| **Editar evento** | Al abrir precarga `ev-1`; intentar guardar aforo `0` | Mensaje verde al cargar; mensaje rojo completo sin texto JDBC al rechazar aforo |
| **Consultar cupos** | Consultar `ev-1` y `ev-2`; pulsar **Limpiar** | Muestra cupos disponibles en verde; **Limpiar** vacía campo y mensaje |
| **Reservar entrada** | Solicitar reserva en `ev-1`, **Copiar código QR** y **Confirmar reserva** | Genera código y vencimiento (+10 min); copia al portapapeles; confirma en verde |
| **Ingreso QR** | Pegar QR (`Ctrl+V`), registrar ingreso, reintentar y reiniciar app | Primer intento en verde; segundo rechazado en rojo; persiste tras reiniciar |
