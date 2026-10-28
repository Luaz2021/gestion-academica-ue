package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/*
 * Entidad que mapea directamente la tabla 'usuarios' de la base de datos.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class usuarios {

	private int id;
	private String identificacion;
	private String nombre;
	private String apellido;
	private String username;
	private String password_hash;
	private String rol;
	private LocalDateTime creado_en;

	// Constructor comodo para instanciar tras consultar la base de datos
	public usuarios(int id, String identificacion, String nombre, String apellido, String username, String password_hash, String rol) {
		this.id = id;
		this.identificacion = identificacion;
		this.nombre = nombre;
		this.apellido = apellido;
		this.username = username;
		this.password_hash = password_hash;
		this.rol = rol;
		this.creado_en = LocalDateTime.now();
	}

	// Metodo para obtener el nombre completo en formato Apellidos, Nombre
	public String getNombreCompleto() {
		return apellido + ", " + nombre;
	}
}
