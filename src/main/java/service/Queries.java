package service;

public class Queries {

	// Busca un usuario por su DNI o por su alias UEM, comprobando su clave cifrada y su rol
	public static final String BUSCAR_USUARIO_LOGIN = 
		"SELECT * FROM usuarios WHERE (identificacion = ? OR username = ?) AND password_hash = ? AND rol = ?";

	// Trae las materias que tiene asignadas un profesor para cargarlas en su desplegable
	public static final String LISTAR_MODULOS_DOCENTE = 
		"SELECT id, nombre, curso_id, profesor_id FROM modulos WHERE profesor_id = ? ORDER BY nombre ASC";

	// Consulta la lista de alumnos matriculados en una materia con sus notas actuales
	public static final String LISTAR_ALUMNOS_POR_MODULO = 
		"SELECT COALESCE(n.id, 0) AS nota_id, m.id AS matricula_id, u.id AS alumno_id, " +
		"u.identificacion, u.nombre, u.apellido, mod.id AS modulo_id, mod.nombre AS nom_modulo, " +
		"'' AS nom_docente, n.nota, n.observaciones " +
		"FROM matriculas m " +
		"JOIN usuarios u ON m.alumno_id = u.id " +
		"JOIN modulos mod ON m.modulo_id = mod.id " +
		"LEFT JOIN notas n ON n.matricula_id = m.id " +
		"WHERE mod.id = ? ORDER BY u.apellido ASC";

	// Saca todo el expediente del alumno con las notas puestas por cada profesor
	public static final String LISTAR_EXPEDIENTE_ALUMNO = 
		"SELECT COALESCE(n.id, 0) AS nota_id, m.id AS matricula_id, u.id AS alumno_id, " +
		"u.identificacion, u.nombre, u.apellido, mod.id AS modulo_id, mod.nombre AS nom_modulo, " +
		"CONCAT(prof.nombre, ' ', prof.apellido) AS nom_docente, n.nota, n.observaciones " +
		"FROM matriculas m " +
		"JOIN usuarios u ON m.alumno_id = u.id " +
		"JOIN modulos mod ON m.modulo_id = mod.id " +
		"JOIN usuarios prof ON mod.profesor_id = prof.id " +
		"LEFT JOIN notas n ON n.matricula_id = m.id " +
		"WHERE u.id = ? ORDER BY mod.id ASC";

	// Inserta la nota si no existia o la actualiza si el profesor ya le habia puesto una antes
	public static final String GUARDAR_O_ACTUALIZAR_NOTA = 
		"INSERT INTO notas (matricula_id, nota, observaciones) VALUES (?, ?, ?) " +
		"ON DUPLICATE KEY UPDATE nota = VALUES(nota), observaciones = VALUES(observaciones)";
}
