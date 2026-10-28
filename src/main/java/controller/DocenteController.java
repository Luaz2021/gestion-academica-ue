package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import model.CalificacionDetalle;
import model.modulos;
import model.usuarios;
import service.CalificacionService;
import view.DashDocente;
import view.Login;

/*
 * Controlador para la pantalla del docente.
 * Escucha los clics sobre la tabla, el boton de guardar y el desplegable de asignaturas.
 * Delega en CalificacionService para validar los numeros y guardar en MariaDB.
 */
public class DocenteController implements ActionListener {

	private DashDocente vista;
	private usuarios docente;
	private CalificacionService servicio;
	private List<CalificacionDetalle> listaActual;

	public DocenteController(DashDocente vista, usuarios docente) {
		this.vista = vista;
		this.docente = docente;
		this.servicio = new CalificacionService();

		// Enganchamos las acciones de los botones y del combo de asignaturas
		this.vista.getBtnGuardarNota().addActionListener(this);
		this.vista.getBtnCerrarSesion().addActionListener(this);
		this.vista.getComboModulos().addActionListener(this);

		// Cargamos las asignaturas reales que imparte este profesor
		cargarDesplegableMaterias();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vista.getBtnGuardarNota()) {
			guardarCalificacion();
		} else if (e.getSource() == vista.getBtnCerrarSesion()) {
			cerrarSesion();
		} else if (e.getSource() == vista.getComboModulos()) {
			refrescarTablaAlumnos();
		}
	}

	// Consulta al Service las asignaturas asignadas al docente y las pone en el combo
	private void cargarDesplegableMaterias() {
		try {
			List<modulos> materias = servicio.cargarMateriasDocente(docente.getId());
			vista.getComboModulos().removeAllItems();

			for (modulos m : materias) {
				vista.getComboModulos().addItem(m.getId() + " - " + m.getNombre());
			}

			// Si tiene materias asignadas, cargamos directamente la primera
			if (vista.getComboModulos().getItemCount() > 0) {
				refrescarTablaAlumnos();
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(vista, "Error al cargar las materias: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Pide al Service la lista de alumnos de la materia elegida y los pinta en la tabla
	private void refrescarTablaAlumnos() {
		if (vista.getComboModulos().getSelectedItem() == null) return;

		String item = vista.getComboModulos().getSelectedItem().toString();
		int moduloId = Integer.parseInt(item.split(" - ")[0]);

		try {
			listaActual = servicio.cargarAlumnosDeMateria(moduloId);
			DefaultTableModel modelo = vista.getModeloTabla();
			modelo.setRowCount(0);

			for (CalificacionDetalle c : listaActual) {
				String notaTexto = (c.getNota() != null) ? String.format("%.2f", c.getNota()) : "NP";
				modelo.addRow(new Object[]{
					c.getMatriculaId(),
					c.getIdentificacionAlumno(),
					c.getAlumnoCompleto(),
					notaTexto,
					c.getCalificacionCualitativa(),
					(c.getObservaciones() != null) ? c.getObservaciones() : ""
				});
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(vista, "Error al listar alumnos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Lee lo que el profesor escribio abajo y se lo pasa al Service para que lo valide y guarde
	private void guardarCalificacion() {
		int fila = vista.getTablaAlumnos().getSelectedRow();
		if (fila == -1) {
			JOptionPane.showMessageDialog(vista, "Primero selecciona a un alumno haciendo clic sobre la tabla.", "Atención", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int matriculaId = Integer.parseInt(vista.getTablaAlumnos().getValueAt(fila, 0).toString());
		String notaEscrita = vista.getTxtNota().getText();
		String observaciones = vista.getTxtObservaciones().getText();

		try {
			// El Service valida el rango (0 a 10), el formato y le dice al DAO que lo guarde
			boolean guardado = servicio.registrarNota(matriculaId, notaEscrita, observaciones);

			if (guardado) {
				JOptionPane.showMessageDialog(vista, "Calificación guardada correctamente en MariaDB.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
				refrescarTablaAlumnos(); // Refrescamos la tabla para que se vea el cambio de inmediato
			}
		} catch (Exception ex) {
			// Si la nota no es un numero o pasa de 10, el Service lanza el error y aqui lo mostramos
			JOptionPane.showMessageDialog(vista, ex.getMessage(), "Aviso de Validación", JOptionPane.WARNING_MESSAGE);
		}
	}

	// Cierra el panel del profesor y vuelve a mostrar el login
	private void cerrarSesion() {
		vista.dispose();
		Login login = new Login();
		new LoginController(login);
		login.setLocationRelativeTo(null);
		login.setVisible(true);
	}
}
