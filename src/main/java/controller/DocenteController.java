package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JOptionPane;

import model.CalificacionDetalle;
import model.cursos;
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
	private List<modulos> materiasDocente = List.of();
	private boolean cargandoCombos;

	public DocenteController(DashDocente vista, usuarios docente) {
		this.vista = vista;
		this.docente = docente;
		this.servicio = new CalificacionService();

		// Enganchamos las acciones de los botones y los combos de curso y asignaturas
		this.vista.getBtnGuardarNota().addActionListener(this);
		this.vista.getBtnCerrarSesion().addActionListener(this);
		this.vista.getComboCursos().addActionListener(this);
		this.vista.getComboModulos().addActionListener(this);

		// Cargamos las asignaturas reales que imparte este profesor
		cargarDesplegableMaterias();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == vista.getBtnGuardarNota()) {
			guardarCalificacion();
			return;
		}
		if (src == vista.getBtnCerrarSesion()) {
			cerrarSesion();
			return;
		}
		if (cargandoCombos) return;
		if (src == vista.getComboCursos()) {
			filtrarModulosPorCurso();
			return;
		}
		if (src == vista.getComboModulos()) {
			refrescarTablaAlumnos();
		}
	}

	// Consulta cursos y asignaturas reales del docente y filtra el desplegable por el curso activo
	private void cargarDesplegableMaterias() {
		try {
			cargandoCombos = true;
			try {
				vista.getComboCursos().removeAllItems();
				servicio.cargarCursos().forEach(vista.getComboCursos()::addItem);
			} finally {
				cargandoCombos = false;
			}
			materiasDocente = servicio.cargarMateriasDocente(docente.getId());
			seleccionarPrimerCursoConMaterias();
			filtrarModulosPorCurso();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(vista, "Error al cargar las materias: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Evita abrir en blanco si el docente solo imparte docencia en otro curso (ej. 2º DAM)
	private void seleccionarPrimerCursoConMaterias() {
		cursos actual = (cursos) vista.getComboCursos().getSelectedItem();
		if (actual == null || materiasDocente.isEmpty()) return;

		boolean tieneEnActual = materiasDocente.stream().anyMatch(m -> m.getCurso_id() == actual.getId());
		if (tieneEnActual) return;

		int primerCursoId = materiasDocente.get(0).getCurso_id();
		cargandoCombos = true;
		try {
			for (int i = 0; i < vista.getComboCursos().getItemCount(); i++) {
				if (vista.getComboCursos().getItemAt(i).getId() == primerCursoId) {
					vista.getComboCursos().setSelectedIndex(i);
					return;
				}
			}
		} finally {
			cargandoCombos = false;
		}
	}

	private void filtrarModulosPorCurso() {
		cursos cursoSeleccionado = (cursos) vista.getComboCursos().getSelectedItem();
		if (cursoSeleccionado == null) {
			limpiarTablaYFormulario();
			return;
		}

		// Bloquea eventos intermedios de removeAllItems/addItem para no disparar consultas parciales
		cargandoCombos = true;
		try {
			vista.getComboModulos().removeAllItems();
			materiasDocente.stream()
				.filter(m -> m.getCurso_id() == cursoSeleccionado.getId())
				.forEach(vista.getComboModulos()::addItem);
		} finally {
			cargandoCombos = false;
		}

		if (vista.getComboModulos().getItemCount() == 0) {
			limpiarTablaYFormulario();
			return;
		}
		vista.getComboModulos().setSelectedIndex(0);
		refrescarTablaAlumnos();
	}

	// Pide al Service la lista de alumnos de la materia elegida y los pinta en la tabla
	private void refrescarTablaAlumnos() {
		modulos moduloSeleccionado = (modulos) vista.getComboModulos().getSelectedItem();
		if (moduloSeleccionado == null) {
			limpiarTablaYFormulario();
			return;
		}

		try {
			listaActual = servicio.cargarAlumnosDeMateria(moduloSeleccionado.getId());
			vista.getModeloTabla().setRowCount(0);
			limpiarFormulario();

			for (CalificacionDetalle c : listaActual) {
				String notaTexto = (c.getNota() != null) ? String.format("%.2f", c.getNota()) : "NP";
				vista.getModeloTabla().addRow(new Object[]{
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

	private void limpiarTablaYFormulario() {
		listaActual = List.of();
		vista.getModeloTabla().setRowCount(0);
		limpiarFormulario();
	}

	private void limpiarFormulario() {
		vista.getTablaAlumnos().clearSelection();
		vista.getTxtAlumnoSeleccionado().setText("");
		vista.getTxtNota().setText("");
		vista.getTxtObservaciones().setText("");
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
