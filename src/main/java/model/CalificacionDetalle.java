package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalificacionDetalle {

	private int notaId;
	private int matriculaId;
	private int alumnoId;
	private String identificacionAlumno;
	private String nombreAlumno;
	private String apellidoAlumno;
	private int moduloId;
	private String nombreModulo;
	private String nombreDocente;
	private Double nota;
	private String observaciones;

	// Formato de nombre completo para visualizar en la tabla del profesor
	public String getAlumnoCompleto() {
		return apellidoAlumno + ", " + nombreAlumno;
	}

	// Conversion automatica de la nota numerica al baremo cualitativo oficial
	public String getCalificacionCualitativa() {
		if (nota == null) return "NO EVALUADO";
		if (nota < 5.0) return "SUSPENSO";
		if (nota < 7.0) return "APROBADO";
		if (nota < 9.0) return "NOTABLE";
		return "SOBRESALIENTE";
	}
}
