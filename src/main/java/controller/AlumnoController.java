package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.CalificacionDetalle;
import model.usuarios;
import service.CalificacionService;
import view.DashAlumno;
import view.Login;

/*
 * Controlador para la pantalla del alumno.
 * Pide al Service todas las asignaturas matriculadas, saca el promedio general
 * y rellena la tabla de calificaciones con el feedback de cada profesor.
 */
public class AlumnoController implements ActionListener {

	private DashAlumno vista;
	private usuarios alumno;
	private CalificacionService servicio;

	public AlumnoController(DashAlumno vista, usuarios alumno) {
		this.vista = vista;
		this.alumno = alumno;
		this.servicio = new CalificacionService();

		// Escuchamos el boton de cerrar sesion
		this.vista.getBtnCerrarSesion().addActionListener(this);

		// Cargamos el expediente del estudiante
		mostrarExpediente();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vista.getBtnCerrarSesion()) {
			cerrarSesion();
		}
	}

	// Pide las calificaciones al Service, calcula la media y rellena la tabla
	private void mostrarExpediente() {
		try {
			List<CalificacionDetalle> asignaturas = servicio.cargarExpedienteAlumno(alumno.getId());
			DefaultTableModel modelo = vista.getModeloTabla();
			modelo.setRowCount(0);

			double sumaTotal = 0.0;
			int cantidadEvaluadas = 0;

			for (CalificacionDetalle c : asignaturas) {
				String textoNota = (c.getNota() != null) ? String.format("%.2f", c.getNota()) : "NP";

				if (c.getNota() != null) {
					sumaTotal += c.getNota();
					cantidadEvaluadas++;
				}

				String nombreProfe = (c.getNombreDocente() != null && !c.getNombreDocente().isBlank())
						? c.getNombreDocente()
						: "Profesor Asignado";

				String comentarios = (c.getObservaciones() != null && !c.getObservaciones().isBlank())
						? c.getObservaciones()
						: "Sin observaciones registradas";

				modelo.addRow(new Object[]{
					c.getNombreModulo(),
					nombreProfe,
					textoNota,
					c.getCalificacionCualitativa(),
					comentarios
				});
			}

			// Actualizamos las tarjetas de resumen de arriba
			if (cantidadEvaluadas > 0) {
				double promedio = sumaTotal / cantidadEvaluadas;
				vista.getLblMediaValor().setText(String.format("%.2f", promedio));
			} else {
				vista.getLblMediaValor().setText("---");
			}

			vista.getLblModulosValor().setText(cantidadEvaluadas + " / " + asignaturas.size());

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(vista, "Error al cargar tu expediente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Cierra el panel y regresa a la ventana de login
	private void cerrarSesion() {
		vista.dispose();
		Login login = new Login();
		new LoginController(login);
		login.setLocationRelativeTo(null);
		login.setVisible(true);
	}
}
