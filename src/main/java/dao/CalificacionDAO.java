package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConexion;
import model.CalificacionDetalle;
import model.modulos;
import model.usuarios;
import service.Queries;

public class CalificacionDAO {

	// Lanza la consulta en la tabla usuarios para ver si existe alguien con esa clave cifrada
	public usuarios buscarUsuarioPorCredenciales(String identificador, String hashPassword, String rol) throws SQLException {
		Connection conn = DatabaseConexion.getConexion();
		if (conn == null) {
			throw new SQLException("No hay conexion abierta con la base de datos.");
		}

		try (PreparedStatement ps = conn.prepareStatement(Queries.BUSCAR_USUARIO_LOGIN)) {
			ps.setString(1, identificador);
			ps.setString(2, identificador);
			ps.setString(3, hashPassword);
			ps.setString(4, rol);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new usuarios(
						rs.getInt("id"),
						rs.getString("identificacion"),
						rs.getString("nombre"),
						rs.getString("apellido"),
						rs.getString("username"),
						rs.getString("password_hash"),
						rs.getString("rol")
					);
				}
			}
		}
		return null;
	}

	// Va a la tabla modulos y me trae solo las materias de este profesor
	public List<modulos> obtenerModulosDeProfesor(int profesorId) throws SQLException {
		List<modulos> lista = new ArrayList<>();
		Connection conn = DatabaseConexion.getConexion();

		try (PreparedStatement ps = conn.prepareStatement(Queries.LISTAR_MODULOS_DOCENTE)) {
			ps.setInt(1, profesorId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					lista.add(new modulos(
						rs.getInt("id"),
						rs.getString("nombre"),
						rs.getInt("curso_id"),
						rs.getInt("profesor_id")
					));
				}
			}
		}
		return lista;
	}

	// Junta matriculas, alumnos y notas para armar la lista de la clase que ve el profesor
	public List<CalificacionDetalle> obtenerAlumnosPorModulo(int moduloId) throws SQLException {
		List<CalificacionDetalle> lista = new ArrayList<>();
		Connection conn = DatabaseConexion.getConexion();

		try (PreparedStatement ps = conn.prepareStatement(Queries.LISTAR_ALUMNOS_POR_MODULO)) {
			ps.setInt(1, moduloId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Double nota = rs.getObject("nota") != null ? rs.getDouble("nota") : null;
					lista.add(new CalificacionDetalle(
						rs.getInt("nota_id"),
						rs.getInt("matricula_id"),
						rs.getInt("alumno_id"),
						rs.getString("identificacion"),
						rs.getString("nombre"),
						rs.getString("apellido"),
						rs.getInt("modulo_id"),
						rs.getString("nom_modulo"),
						rs.getString("nom_docente"),
						nota,
						rs.getString("observaciones")
					));
				}
			}
		}
		return lista;
	}

	// Busca todas las asignaturas donde esta matriculado el alumno y recopila sus notas
	public List<CalificacionDetalle> obtenerExpedienteAlumno(int alumnoId) throws SQLException {
		List<CalificacionDetalle> lista = new ArrayList<>();
		Connection conn = DatabaseConexion.getConexion();

		try (PreparedStatement ps = conn.prepareStatement(Queries.LISTAR_EXPEDIENTE_ALUMNO)) {
			ps.setInt(1, alumnoId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Double nota = rs.getObject("nota") != null ? rs.getDouble("nota") : null;
					lista.add(new CalificacionDetalle(
						rs.getInt("nota_id"),
						rs.getInt("matricula_id"),
						rs.getInt("alumno_id"),
						rs.getString("identificacion"),
						rs.getString("nombre"),
						rs.getString("apellido"),
						rs.getInt("modulo_id"),
						rs.getString("nom_modulo"),
						rs.getString("nom_docente"),
						nota,
						rs.getString("observaciones")
					));
				}
			}
		}
		return lista;
	}

	// Guarda o modifica la nota final en la tabla notas
	public boolean persistirNotaEnBD(int matriculaId, double nota, String observaciones) throws SQLException {
		Connection conn = DatabaseConexion.getConexion();
		try (PreparedStatement ps = conn.prepareStatement(Queries.GUARDAR_O_ACTUALIZAR_NOTA)) {
			ps.setInt(1, matriculaId);
			ps.setDouble(2, nota);
			ps.setString(3, observaciones);
			return ps.executeUpdate() > 0;
		}
	}
}
