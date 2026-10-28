package service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import dao.CalificacionDAO;
import model.CalificacionDetalle;
import model.modulos;
import model.usuarios;


public class CalificacionService {

	private CalificacionDAO dao;

	public CalificacionService() {
		this.dao = new CalificacionDAO();
	}

	// Recibe la clave normal (como "UEMSAMA21"), la cifra y le pide al DAO que busque al usuario
	public usuarios validarLogin(String identificador, String claveEscrita, String rol) throws Exception {
		if (identificador == null || identificador.isBlank()) {
			throw new Exception("Debes indicar tu usuario o DNI para poder entrar.");
		}
		if (claveEscrita == null || claveEscrita.isBlank()) {
			throw new Exception("La contraseña no puede estar vacía.");
		}

		// Trabajo sucio: convertimos el texto plano a su equivalente SHA-256
		String hash = DigestUtils.sha256Hex(claveEscrita.trim());

		// Con el hash listo, mandamos a consultar a la base de datos
		return dao.buscarUsuarioPorCredenciales(identificador.trim(), hash, rol.trim());
	}

	// Pide al DAO las materias asignadas a un profesor para llenar su desplegable
	public List<modulos> cargarMateriasDocente(int profesorId) throws SQLException {
		return dao.obtenerModulosDeProfesor(profesorId);
	}

	// Pide al DAO los alumnos matriculados en una asignatura concreta
	public List<CalificacionDetalle> cargarAlumnosDeMateria(int moduloId) throws SQLException {
		return dao.obtenerAlumnosPorModulo(moduloId);
	}

	// Pide al DAO las asignaturas y calificaciones completas de un alumno
	public List<CalificacionDetalle> cargarExpedienteAlumno(int alumnoId) throws SQLException {
		return dao.obtenerExpedienteAlumno(alumnoId);
	}

	// Aplica las reglas universitarias antes de tocar la base de datos
	public boolean registrarNota(int matriculaId, String textoNota, String comentarios) throws Exception {
		if (textoNota == null || textoNota.isBlank()) {
			throw new Exception("Por favor introduce una calificación numérica.");
		}

		double notaNumerica;
		try {
			notaNumerica = Double.parseDouble(textoNota.trim().replace(",", "."));
		} catch (NumberFormatException e) {
			throw new Exception("La nota debe ser un número válido, por ejemplo 8.5 o 9.");
		}

		// Regla de negocio: la nota academica oficial en Espania va estrictamente de 0 a 10
		if (notaNumerica < 0.0 || notaNumerica > 10.0) {
			throw new Exception("La nota debe estar comprendida entre 0.0 y 10.0.");
		}

		String comentariosLimpios = (comentarios != null) ? comentarios.trim() : "";

		// Una vez validadas todas las reglas, le decimos al DAO que lo guarde en MariaDB
		return dao.persistirNotaEnBD(matriculaId, notaNumerica, comentariosLimpios);
	}
}
