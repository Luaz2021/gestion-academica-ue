package model;

import java.time.LocalDateTime;
import lombok.*;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class notas {
	private int id;
	private int matricula_id;
	private double nota;
	private String observaciones;
	private LocalDateTime actualizado_en;
}
