package model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class modulos {
	private int id;
	private String nombre;
	private int curso_id;
	private int profesor_id;

	@Override
	public String toString() {
		return nombre;
	}
}
