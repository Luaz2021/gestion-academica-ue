# 🎓 Sistema de Gestión Académica — Universidad Europea

![Java](https://img.shields.io/badge/Java-21%20LTS-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.x-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Swing & FlatLaf](https://img.shields.io/badge/UI-Swing%20%2B%20FlatLaf%203.4.1-2F6FED?style=for-the-badge)
![MariaDB](https://img.shields.io/badge/MariaDB-Laragon%203306-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Eclipse](https://img.shields.io/badge/Eclipse%20IDE-2026--09%2B-2C2255?style=for-the-badge&logo=eclipseide&logoColor=white)

Aplicación de escritorio en **Java 21 (Swing + FlatLaf)** bajo arquitectura **MVC + DAO + Service** para la gestión integral de matrículas, cursos, módulos y calificaciones académicas conectada a **MariaDB**.

---

## ✨ Características principales

- 🔐 **Autenticación por roles** (`view.Login` / `LoginController`): acceso diferenciado para **Docente** y **Alumno** con hashes SHA-256 (`commons-codec`).
- 👨‍🏫 **Panel Docente** (`view.DashDocente` / `DocenteController`):
  - Selección automática del primer curso activo asignado al profesor.
  - Filtrado dinámico **Curso → Módulos** y carga de matrículas asociadas.
  - Alta, edición y validación numérica/nula de calificaciones a través de `CalificacionService` y `CalificacionDAO`.
- 👩‍🎓 **Panel Alumno** (`view.DashAlumno` / `AlumnoController`): consulta limpia del expediente, módulos matriculados y notas obtenidas.
- 🎨 **Interfaz moderna**: tema **FlatLightLaf** (`flatlaf 3.4.1`) con recursos gráficos corporativos en `src/main/resources`.

---

## 🗂️ Estructura del proyecto

```text
gestion-academica-ue/
├── Ejecutar_Gestion_UE.launch      # Lanzador portable para Eclipse (M2E + Java 21)
├── ejecutar.bat                    # Compilador y lanzador directo en Windows (sin abrir Eclipse)
├── instalar_eclipse.bat            # Vincula/copia el repo al workspace de Eclipse (Windows)
├── instalar_eclipse.sh             # Vincula el repo al workspace de Eclipse (Linux / macOS)
├── pom.xml                         # Dependencias y plugins Maven (Compiler + Lombok + Exec)
└── src/main/
    ├── java/
    │   ├── controller/             # LoginController, DocenteController, AlumnoController
    │   ├── dao/                    # CalificacionDAO
    │   ├── database/               # DatabaseConexion, ddl.sql, dml.sql
    │   ├── model/                  # usuarios, cursos, modulos, matriculas, notas, CalificacionDetalle
    │   ├── service/                # CalificacionService, Queries
    │   └── view/                   # Login (main), DashDocente, DashAlumno
    └── resources/                  # logo_ue.png, fondo_ue.jpg, fondo_ue_scaled.png
```

---

## 📦 Dependencias y plugins (`pom.xml`)

| Artefacto | Grupo | Versión | Uso |
| :--- | :--- | :---: | :--- |
| `mariadb-java-client` | `org.mariadb.jdbc` | `3.3.3` | Driver JDBC oficial para MariaDB / MySQL (Laragon) |
| `flatlaf` | `com.formdev` | `3.4.1` | Look & Feel moderno para Java Swing |
| `lombok` | `org.projectlombok` | `1.18.38` | Generación en compilación (`provided` + `annotationProcessorPaths`) |
| `commons-codec` | `commons-codec` | `1.16.1` | Utilidades criptográficas (SHA-256) |
| `slf4j-nop` | `org.slf4j` | `2.0.16` | Implementación silenciosa de SLF4J en consola |
| `maven-compiler-plugin` | `org.apache.maven.plugins` | `3.13.0` | Compilación Java 21 UTF-8 con procesador de Lombok |
| `exec-maven-plugin` | `org.codehaus.mojo` | `3.5.0` | Ejecución directa de `view.Login` vía Maven |

---

## 🗄️ Base de datos (MariaDB / Laragon)

La aplicación conecta por defecto mediante `database.DatabaseConexion` a:

- **Host / Puerto:** `localhost:3306`
- **Base de datos:** `universidad_europea`
- **Usuario / Contraseña:** `root` / *(vacía, configuración estándar de Laragon)*

### Inicialización rápida del esquema y datos

1. Inicia **Laragon** (o tu servidor MariaDB/MySQL) en el puerto `3306`.
2. Ejecuta en orden sobre MariaDB:
   - `src/main/java/database/ddl.sql` *(estructura de tablas y claves foráneas)*
   - `src/main/java/database/dml.sql` *(datos semilla de cursos, módulos, usuarios y notas)*

---

## 🚀 Cómo ejecutar la aplicación

### Opción 1 — Desde Eclipse IDE (`2026-09` o superior)

1. *(Opcional)* Ejecuta `instalar_eclipse.bat` (Windows) o `./instalar_eclipse.sh` (Linux/macOS) para enlazar el repositorio con tu `eclipse-workspace`.
2. Abre Eclipse y ve a **File → Import... → Maven → Existing Maven Projects** seleccionando la carpeta del proyecto.
3. Si es la primera vez tras clonar o actualizar `pom.xml`, haz **clic derecho en el proyecto → Maven → Update Project...** (`Alt + F5`).
4. Para arrancar la aplicación:
   - Haz **clic derecho sobre `Ejecutar_Gestion_UE.launch` → Run As → Ejecutar_Gestion_UE**, **o** ejecuta `src/main/java/view/Login.java` con **Run As → Java Application**.

> **Nota para diseño visual:** haz clic derecho sobre cualquier vista (`Login.java`, `DashDocente.java`, `DashAlumno.java`) → **Open With → WindowBuilder Editor**.

### Opción 2 — Sin abrir Eclipse (Windows, 1 clic)

Si quieres saltarte las manías de Eclipse tras compilar dependencias una vez:

```bat
ejecutar.bat
```

El script comprueba `javac` (JDK 21+), localiza los `.jar` en `%USERPROFILE%\.m2\repository`, compila `src\main\java` con `-encoding UTF-8`, copia `src\main\resources` a `target\classes` y arranca `view.Login`.

### Opción 3 — Con Maven CLI

```bash
mvn clean compile exec:java
```

---

## 🛠️ Solución rápida de problemas en Eclipse

- **Lombok marca errores en getters/setters o constructores:**
  Verifica que `eclipse.ini` incluya `-javaagent:<ruta>\lombok.jar` (instalador oficial de `lombok-1.18.38.jar`) y ejecuta **Project → Clean...** seguido de **Maven → Update Project**.
- **Faltan JARs al usar `ejecutar.bat`:**
  Ejecuta **Maven → Update Project** dentro de Eclipse una sola vez para poblar `%USERPROFILE%\.m2\repository` (o `mvn dependency:resolve` si tienes Maven en el PATH).
- **Error de conexión al iniciar sesión:**
  Confirma que el servicio MariaDB/MySQL está iniciado en `localhost:3306` y que ejecutaste `ddl.sql` y `dml.sql`.

---

## 🔑 Credenciales de acceso (`dml.sql`)

Ejemplo de cuentas incluidas en `src/main/java/database/dml.sql` (consulta ese script o `src/main/resources/Credenciales_Acceso_Campus_UE.pdf` para el listado completo):

| Rol | Usuario | Contraseña |
| :--- | :--- | :--- |
| 👨‍🏫 **Docente** | `UEM-DOC-BM` | `UEMBOMA00` |
| 👩‍🎓 **Alumno** | `UEM-ALUM-SAM` | `UEMSAMA21` |
