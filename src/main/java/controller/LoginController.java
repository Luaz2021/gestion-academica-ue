package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import model.usuarios;
import service.CalificacionService;
import view.DashAlumno;
import view.DashDocente;
import view.Login;

/*
 * Controlador de la pantalla de Login.
 * Su unico trabajo es escuchar cuando el usuario pulsa Entrar o Limpiar,
 * pasarle los textos al Service para que haga la comprobacion y abrir la ventana que toque.
 */
public class LoginController implements ActionListener {

	private Login vista;
	private CalificacionService servicio;

	public LoginController(Login vista) {
		this.vista = vista;
		this.servicio = new CalificacionService();

		// Enganchamos los botones de la pantalla con este controlador
		this.vista.getBtnIngresar().addActionListener(this);
		this.vista.getBtnLimpiar().addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == vista.getBtnIngresar()) {
			procesarIngreso();
		} else if (e.getSource() == vista.getBtnLimpiar()) {
			vaciarFormulario();
		}
	}

	// Deja en blanco las cajas de texto y regresa la seleccion del rol al primero
	private void vaciarFormulario() {
		vista.getTxtUsuario().setText("");
		vista.getTxtPassword().setText("");
		vista.getComboRol().setSelectedIndex(0);
		vista.getTxtUsuario().requestFocus();
	}

	// Toma los datos que escribio el usuario y se los entrega al Service
	private void procesarIngreso() {
		String identificador = vista.getTxtUsuario().getText().trim();
		String password = new String(vista.getTxtPassword().getPassword()).trim();
		String rolSeleccionado = vista.getComboRol().getSelectedItem().toString();

		// En la base de datos el docente esta registrado como PROFESOR
		String rolBD = "DOCENTE".equalsIgnoreCase(rolSeleccionado) ? "PROFESOR" : "ALUMNO";

		try {
			// El Service hace el trabajo sucio: valida campos, saca el hash SHA-256 y consulta
			usuarios usuarioConectado = servicio.validarLogin(identificador, password, rolBD);

			if (usuarioConectado == null) {
				JOptionPane.showMessageDialog(vista, "Datos incorrectos", "Error de Acceso", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Mensaje de bienvenida oficial que exige el enunciado de la tarea
			JOptionPane.showMessageDialog(vista, "Bienvenido " + usuarioConectado.getNombre() + " " + usuarioConectado.getApellido(), "Acceso Concedido", JOptionPane.INFORMATION_MESSAGE);

			// Abrimos la ventana correspondiente segun si es profesor o alumno pasando el usuario autenticado
			if ("PROFESOR".equalsIgnoreCase(usuarioConectado.getRol())) {
				DashDocente dash = new DashDocente(usuarioConectado);
				new DocenteController(dash, usuarioConectado);
				dash.setLocationRelativeTo(null);
				dash.setVisible(true);
			} else {
				DashAlumno dash = new DashAlumno(usuarioConectado);
				new AlumnoController(dash, usuarioConectado);
				dash.setLocationRelativeTo(null);
				dash.setVisible(true);
			}

			// Cerramos la pantalla de login para que no se quede abierta detras
			vista.dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(vista, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
		}
	}
}
