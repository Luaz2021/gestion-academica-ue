package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import model.usuarios;

/*
 * Consulta del expediente academico para el perfil de alumno.
 * Blindado con control de sesion: si no hay un alumno autenticado,
 * no permite saltarse el login.
 */
public class DashAlumno extends JFrame {

	private static final long serialVersionUID = 1L;
	private usuarios alumnoActivo;
	private JPanel contentPane;
	private JPanel panelHeader;
	private JPanel panelCardMedia;
	private JPanel panelCardModulos;
	private JPanel panelDetalle;
	private JTable tablaCalificaciones;
	private DefaultTableModel modeloTabla;
	private JTextArea txtObservacionesDetalle;
	private JLabel lblMediaValor;
	private JLabel lblModulosValor;
	private JButton btnCerrarSesion;
	private JLabel lblAlumnoInfo;

	// Si alguien intenta ejecutar esta clase directamente sin pasar por el login, lo mandamos al Login
	public static void main(String[] args) {
		try {
			FlatLightLaf.setup();
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JOptionPane.showMessageDialog(null, "Acceso no autorizado. Debes identificarte primero en la pantalla de inicio.", "Seguridad del Campus", JOptionPane.WARNING_MESSAGE);
					Login login = new Login();
					new controller.LoginController(login);
					login.setLocationRelativeTo(null);
					login.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Constructor por defecto necesario para WindowBuilder en modo edicion
	public DashAlumno() {
		this(null);
	}

	// Constructor oficial con validacion estricta de sesion
	public DashAlumno(usuarios alumno) {
		this.alumnoActivo = alumno;

		setTitle("Expediente Académico del Alumno - Universidad Europea");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 920, 620);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 250, 252));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Barra superior corporativa
		panelHeader = new JPanel();
		panelHeader.setBackground(new Color(196, 18, 48));
		panelHeader.setBounds(0, 0, 910, 65);
		contentPane.add(panelHeader);
		panelHeader.setLayout(null);

		JLabel lblLogoUE = new JLabel("UNIVERSIDAD EUROPEA");
		lblLogoUE.setForeground(Color.WHITE);
		lblLogoUE.setFont(new Font("Segoe UI", Font.BOLD, 17));
		lblLogoUE.setBounds(25, 12, 280, 24);
		panelHeader.add(lblLogoUE);

		JLabel lblSubCampus = new JLabel("Expediente de Calificaciones del Estudiante");
		lblSubCampus.setForeground(new Color(254, 202, 202));
		lblSubCampus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSubCampus.setBounds(25, 34, 280, 18);
		panelHeader.add(lblSubCampus);

		String alumnoTexto = (alumno != null) ? alumno.getNombreCompleto() + " (" + alumno.getIdentificacion() + ")" : "Alumno: Modo Diseño";
		lblAlumnoInfo = new JLabel(alumnoTexto);
		lblAlumnoInfo.setHorizontalAlignment(SwingConstants.RIGHT);
		lblAlumnoInfo.setForeground(Color.WHITE);
		lblAlumnoInfo.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblAlumnoInfo.setBounds(500, 22, 260, 20);
		panelHeader.add(lblAlumnoInfo);

		btnCerrarSesion = new JButton("Cerrar Sesión");
		btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnCerrarSesion.setForeground(new Color(196, 18, 48));
		btnCerrarSesion.setBackground(Color.WHITE);
		btnCerrarSesion.setBounds(770, 16, 120, 32);
		btnCerrarSesion.setFocusPainted(false);
		btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelHeader.add(btnCerrarSesion);

		// Tarjetas con las metricas globales
		panelCardMedia = new JPanel();
		panelCardMedia.setBackground(Color.WHITE);
		panelCardMedia.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelCardMedia.setBounds(25, 80, 415, 85);
		contentPane.add(panelCardMedia);
		panelCardMedia.setLayout(null);

		JLabel lblMediaTitulo = new JLabel("NOTA MEDIA GLOBAL");
		lblMediaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblMediaTitulo.setForeground(new Color(100, 116, 139));
		lblMediaTitulo.setBounds(20, 14, 180, 16);
		panelCardMedia.add(lblMediaTitulo);

		lblMediaValor = new JLabel("---");
		lblMediaValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
		lblMediaValor.setForeground(new Color(196, 18, 48));
		lblMediaValor.setBounds(20, 32, 120, 40);
		panelCardMedia.add(lblMediaValor);

		JLabel lblEstadoGlobal = new JLabel("EXPEDIENTE EN CURSO");
		lblEstadoGlobal.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblEstadoGlobal.setForeground(new Color(22, 163, 74));
		lblEstadoGlobal.setBounds(250, 35, 155, 25);
		panelCardMedia.add(lblEstadoGlobal);

		panelCardModulos = new JPanel();
		panelCardModulos.setBackground(Color.WHITE);
		panelCardModulos.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelCardModulos.setBounds(465, 80, 425, 85);
		contentPane.add(panelCardModulos);
		panelCardModulos.setLayout(null);

		JLabel lblModulosTitulo = new JLabel("MÓDULOS EVALUADOS");
		lblModulosTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblModulosTitulo.setForeground(new Color(100, 116, 139));
		lblModulosTitulo.setBounds(20, 14, 180, 16);
		panelCardModulos.add(lblModulosTitulo);

		lblModulosValor = new JLabel("0 / 0");
		lblModulosValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
		lblModulosValor.setForeground(new Color(15, 23, 42));
		lblModulosValor.setBounds(20, 32, 120, 40);
		panelCardModulos.add(lblModulosValor);

		JLabel lblProgresoTxt = new JLabel("Evaluación Continua");
		lblProgresoTxt.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblProgresoTxt.setForeground(new Color(71, 85, 105));
		lblProgresoTxt.setBounds(280, 35, 135, 25);
		panelCardModulos.add(lblProgresoTxt);

		// Desglose de asignaturas
		JLabel lblTablaExp = new JLabel("Desglose de Calificaciones por Módulo");
		lblTablaExp.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblTablaExp.setForeground(new Color(15, 23, 42));
		lblTablaExp.setBounds(25, 180, 350, 24);
		contentPane.add(lblTablaExp);

		String[] columnas = {"Módulo Formativo", "Docente", "Calificación", "Calificación Cualitativa", "Feedback"};
		modeloTabla = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaCalificaciones = new JTable(modeloTabla);
		tablaCalificaciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tablaCalificaciones.setRowHeight(30);

		JScrollPane scrollTabla = new JScrollPane(tablaCalificaciones);
		scrollTabla.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		scrollTabla.setBounds(25, 210, 865, 220);
		contentPane.add(scrollTabla);

		// Visor de observaciones del docente
		panelDetalle = new JPanel();
		panelDetalle.setBackground(Color.WHITE);
		panelDetalle.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelDetalle.setBounds(25, 445, 865, 120);
		contentPane.add(panelDetalle);
		panelDetalle.setLayout(null);

		JLabel lblDetalleTitulo = new JLabel("Observaciones del Profesor del Módulo Seleccionado:");
		lblDetalleTitulo.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblDetalleTitulo.setForeground(new Color(71, 85, 105));
		lblDetalleTitulo.setBounds(20, 12, 350, 16);
		panelDetalle.add(lblDetalleTitulo);

		txtObservacionesDetalle = new JTextArea("Selecciona una fila en la tabla de arriba para leer el comentario completo del profesor.");
		txtObservacionesDetalle.setEditable(false);
		txtObservacionesDetalle.setLineWrap(true);
		txtObservacionesDetalle.setWrapStyleWord(true);
		txtObservacionesDetalle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtObservacionesDetalle.setBackground(new Color(248, 250, 252));
		txtObservacionesDetalle.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		txtObservacionesDetalle.setBounds(20, 32, 825, 72);
		panelDetalle.add(txtObservacionesDetalle);

		// Evento al seleccionar una fila para desplegar las observaciones completas
		tablaCalificaciones.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tablaCalificaciones.getSelectedRow() != -1) {
				int fila = tablaCalificaciones.getSelectedRow();
				String obs = tablaCalificaciones.getValueAt(fila, 4).toString();
				String modulo = tablaCalificaciones.getValueAt(fila, 0).toString();
				txtObservacionesDetalle.setText("[" + modulo + "]: " + obs);
			}
		});
	}

	public usuarios getAlumnoActivo() { return alumnoActivo; }
	public JTable getTablaCalificaciones() { return tablaCalificaciones; }
	public DefaultTableModel getModeloTabla() { return modeloTabla; }
	public JLabel getLblMediaValor() { return lblMediaValor; }
	public JLabel getLblModulosValor() { return lblModulosValor; }
	public JTextArea getTxtObservacionesDetalle() { return txtObservacionesDetalle; }
	public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
	public JLabel getLblAlumnoInfo() { return lblAlumnoInfo; }
}
