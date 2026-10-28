# Gestion Academica - Universidad Europea (Desarrollo de Interfaces)

Aplicacion de escritorio Java Swing desarrollada bajo arquitectura en capas moderna (MVC Empresarial) para la gestion de calificaciones, alumnos y docentes de la Universidad Europea. Integra conexion a base de datos MariaDB (Laragon), autenticacion con cifrado SHA-256, diseño visual con FlatLaf y diseñador WindowBuilder en Eclipse IDE.

---

## Compatibilidad del Entorno y Versiones

| Componente | Version Oficial Utilizada | Notas de Configuracion |
| :--- | :--- | :--- |
| **IDE** | **Eclipse IDE 2026-12** (o superior) | Enterprise Java and Web / Java Developers |
| **Plugin Visual** | **WindowBuilder Pro v1.13+** | Instalar desde *Eclipse Marketplace* |
| **JDK** | **Java 21 LTS** | `maven.compiler.source=21`, `maven.compiler.target=21` |
| **Gestor** | **Apache Maven 3.9+** | Gestion declarativa en `pom.xml` |
| **Base de Datos** | **MariaDB 10.4+ / MySQL 8.0+** | Servidor local (ej. Laragon / XAMPP) |

---

## Dependencias Maven Utilizadas

Declaradas en el archivo `pom.xml`:

- `org.mariadb.jdbc:mariadb-java-client:3.3.3`: Driver oficial JDBC de alto rendimiento para MariaDB/MySQL.
- `com.formdev:flatlaf:3.4.1`: Libreria moderna de Look & Feel para Java Swing (soporte temas claros y oscuros).
- `org.projectlombok:lombok:1.18.32`: Generacion automatica de Getters, Setters y constructores en clases de modelo.
- `commons-codec:commons-codec:1.16.1`: Cifrado y hashing seguro de contraseñas mediante algoritmo SHA-256.

---

## Configuracion de la Base de Datos

La aplicacion conecta mediante la clase singleton `database.DatabaseConexion.java`:

- **Servidor:** `localhost:3306`
- **Base de Datos:** `universidad_europea`
- **Usuario:** `root`
- **Contraseña:** *(vacia por defecto para Laragon)*

### Scripts de Inicializacion (en `src/main/java/database/`)

1. **`ddl.sql`**: Define el esquema relacional con integridad referencial completa:
   - `usuarios`: Administradores, docentes y alumnos con roles y contraseñas cifradas.
   - `cursos`: Grados universitarios (DAM, DAW, ASIR, etc.).
   - `modulos`: Asignaturas vinculadas a cada curso.
   - `matriculas`: Relacion entre alumnos y cursos activos.
   - `notas`: Calificaciones desglosadas (evaluacion continua, ordinaria y extraordinaria).
2. **`dml.sql`**: Semilla oficial con docentes, alumnos reales de la Universidad Europea y sus notas registradas.

---

## Credenciales de Acceso para Pruebas

Documentadas en el reporte oficial `src/main/resources/Credenciales_Acceso_Campus_UE.pdf`.

### 1. Perfil Docente
- **Usuario:** `profesor`
- **Contraseña:** `docente2026`
- **Capacidades:** Visualizar listado de alumnos matriculados en sus asignaturas, filtrar por modulo y modificar calificaciones en tiempo real con guardado directo en base de datos.

### 2. Perfil Alumno
- **Usuario:** `alumno`
- **Contraseña:** `alumno2026`
- **Capacidades:** Consulta individual de notas por modulo, calculo de media y desglose de convocatorias ordinarias y extraordinarias en modo solo lectura.

---

## Instalacion y Puesta en Marcha

### Opcion Rapida (Instaladores Automaticos)
- **En Windows:** Haz doble clic sobre `instalar_eclipse.bat`. Detectara tu carpeta `eclipse-workspace`, sincronizara el proyecto y validara Java y Maven en tu sistema.
- **En Linux / macOS:** Ejecuta en la terminal:
  ```bash
  chmod +x instalar_eclipse.sh && ./instalar_eclipse.sh
  ```

### Importacion Manual en Eclipse
1. Abre Eclipse IDE (2026-12 o superior).
2. Ve al menu: `File` -> `Import...`
3. Selecciona `Maven` -> `Existing Maven Projects` y pulsa `Next`.
4. En `Root Directory`, pulsa `Browse...` y elige la carpeta de este repositorio (`gestion-academica-ue`).
5. Pulsa `Finish`. Maven resolvera y descargara todas las dependencias automaticamente.

### Como Abrir las Vistas con WindowBuilder
1. En el Explorador de Proyectos de Eclipse, navega hasta `src/main/java/view/`.
2. Haz clic derecho sobre el archivo visual deseado (`Login.java`, `DashDocente.java` o `DashAlumno.java`).
3. Selecciona **Open With** -> **WindowBuilder Editor**.
4. En la parte inferior del editor, alterna entre la pestaña **Source** (codigo Java) y **Design** (diseñador visual de Swing).

### Ejecucion de la Aplicacion
Para iniciar el sistema de forma segura respetando el flujo de autenticacion:
1. Haz clic derecho sobre `src/main/java/view/Login.java`.
2. Selecciona **Run As** -> **Java Application**.

---

## Estructura de Arquitectura en Capas

```text
src/main/java/
├── controller/         -> Intermediarios de eventos entre la vista y la logica
│   ├── LoginController.java
│   ├── DocenteController.java
│   └── AlumnoController.java
│
├── service/            -> Reglas de negocio y consultas centralizadas
│   ├── Queries.java             <- Frases SQL centralizadas y auditables
│   └── CalificacionService.java <- Logica de calculo, promedios y validaciones
│
├── dao/                -> Data Access Object (Persistencia e interaccion JDBC pura)
│   └── CalificacionDAO.java
│
├── database/           -> Singleton de conexion y scripts de despliegue SQL
│   ├── DatabaseConexion.java
│   ├── ddl.sql
│   └── dml.sql
│
├── model/              -> Clases entidad (DTO)
│   ├── CalificacionDetalle.java
│   ├── usuarios.java
│   ├── cursos.java
│   ├── modulos.java
│   ├── matriculas.java
│   └── notas.java
│
└── view/               -> Formularios visuales Swing diseñados con WindowBuilder
    ├── Login.java
    ├── DashDocente.java
    └── DashAlumno.java
```
