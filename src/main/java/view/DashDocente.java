package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import model.usuarios;

/*
 * Panel de gestion de calificaciones para el docente.
 * Blindado con control de sesion: si no hay un docente autenticado,
 * no permite saltarse el login.
 */
public class DashDocente extends JFrame {

	private static final long serialVersionUID = 1L;
	private usuarios docenteActivo;
	private JPanel contentPane;
	private JPanel panelHeader;
	private JPanel panelFiltros;
	private JPanel panelFormulario;
	private JTable tablaAlumnos;
	private DefaultTableModel modeloTabla;
	private JComboBox<String> comboCursos;
	private JComboBox<String> comboModulos;
	private JTextField txtAlumnoSeleccionado;
	private JTextField txtNota;
	private JTextField txtObservaciones;
	private JButton btnGuardarNota;
	private JButton btnCerrarSesion;
	private JLabel lblProfesorNombre;

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
	public DashDocente() {
		this(null);
	}

	// Constructor oficial con validacion estricta de sesion
	public DashDocente(usuarios docente) {
		this.docenteActivo = docente;

		setTitle("Portal del Docente - Universidad Europea");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1020, 680);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(248, 250, 252));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Barra superior corporativa
		panelHeader = new JPanel();
		panelHeader.setBackground(new Color(196, 18, 48));
		panelHeader.setBounds(0, 0, 1010, 65);
		contentPane.add(panelHeader);
		panelHeader.setLayout(null);

		JLabel lblLogoUE = new JLabel("UNIVERSIDAD EUROPEA");
		lblLogoUE.setForeground(Color.WHITE);
		lblLogoUE.setFont(new Font("Segoe UI", Font.BOLD, 17));
		lblLogoUE.setBounds(25, 12, 280, 24);
		panelHeader.add(lblLogoUE);

		JLabel lblSubCampus = new JLabel("Portal de Gestión Docente • Calificaciones");
		lblSubCampus.setForeground(new Color(254, 202, 202));
		lblSubCampus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSubCampus.setBounds(25, 34, 280, 18);
		panelHeader.add(lblSubCampus);

		String nombreDocenteTexto = (docente != null) ? "Docente: " + docente.getNombreCompleto() : "Docente: Modo Diseño";
		lblProfesorNombre = new JLabel(nombreDocenteTexto);
		lblProfesorNombre.setHorizontalAlignment(SwingConstants.RIGHT);
		lblProfesorNombre.setForeground(Color.WHITE);
		lblProfesorNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
		lblProfesorNombre.setBounds(580, 22, 260, 20);
		panelHeader.add(lblProfesorNombre);

		btnCerrarSesion = new JButton("Cerrar Sesión");
		btnCerrarSesion.setFont(new Font("Segoe UI", Font.BOLD, 11));
		btnCerrarSesion.setForeground(new Color(196, 18, 48));
		btnCerrarSesion.setBackground(Color.WHITE);
		btnCerrarSesion.setBounds(860, 16, 125, 32);
		btnCerrarSesion.setFocusPainted(false);
		btnCerrarSesion.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelHeader.add(btnCerrarSesion);

		// Panel de seleccion y filtros academicos
		panelFiltros = new JPanel();
		panelFiltros.setBackground(Color.WHITE);
		panelFiltros.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelFiltros.setBounds(25, 80, 960, 60);
		contentPane.add(panelFiltros);
		panelFiltros.setLayout(null);

		JLabel lblCurso = new JLabel("Curso Académico:");
		lblCurso.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblCurso.setForeground(new Color(71, 85, 105));
		lblCurso.setBounds(20, 20, 120, 20);
		panelFiltros.add(lblCurso);

		comboCursos = new JComboBox<>(new String[]{"1º DAM - Desarrollo de Apps Multiplataforma", "2º DAM - Desarrollo de Apps Multiplataforma"});
		comboCursos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		comboCursos.setBounds(145, 15, 310, 32);
		panelFiltros.add(comboCursos);

		JLabel lblModulo = new JLabel("Módulo:");
		lblModulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblModulo.setForeground(new Color(71, 85, 105));
		lblModulo.setBounds(480, 20, 70, 20);
		panelFiltros.add(lblModulo);

		comboModulos = new JComboBox<>();
		comboModulos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		comboModulos.setBounds(550, 15, 385, 32);
		panelFiltros.add(comboModulos);

		// Tabla de estudiantes y notas
		JLabel lblListaTitulo = new JLabel("Listado de Estudiantes y Evaluaciones");
		lblListaTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
		lblListaTitulo.setForeground(new Color(15, 23, 42));
		lblListaTitulo.setBounds(25, 150, 350, 24);
		contentPane.add(lblListaTitulo);

		String[] columnas = {"Matrícula", "DNI", "Apellidos y Nombre", "Nota Final", "Estado", "Observaciones"};
		modeloTabla = new DefaultTableModel(columnas, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaAlumnos = new JTable(modeloTabla);
		tablaAlumnos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		tablaAlumnos.setRowHeight(30);

		JScrollPane scrollTabla = new JScrollPane(tablaAlumnos);
		scrollTabla.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		scrollTabla.setBounds(25, 180, 960, 310);
		contentPane.add(scrollTabla);

		// Formulario inferior para edicion de notas
		panelFormulario = new JPanel();
		panelFormulario.setBackground(Color.WHITE);
		panelFormulario.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelFormulario.setBounds(25, 505, 960, 125);
		contentPane.add(panelFormulario);
		panelFormulario.setLayout(null);

		JLabel lblAlumnoForm = new JLabel("Estudiante Seleccionado:");
		lblAlumnoForm.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblAlumnoForm.setForeground(new Color(71, 85, 105));
		lblAlumnoForm.setBounds(20, 15, 180, 16);
		panelFormulario.add(lblAlumnoForm);

		txtAlumnoSeleccionado = new JTextField();
		txtAlumnoSeleccionado.setEditable(false);
		txtAlumnoSeleccionado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtAlumnoSeleccionado.setBackground(new Color(248, 250, 252));
		txtAlumnoSeleccionado.setBounds(20, 35, 260, 32);
		panelFormulario.add(txtAlumnoSeleccionado);
		txtAlumnoSeleccionado.setColumns(10);

		JLabel lblNotaForm = new JLabel("Calificación (0 - 10):");
		lblNotaForm.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblNotaForm.setForeground(new Color(71, 85, 105));
		lblNotaForm.setBounds(300, 15, 130, 16);
		panelFormulario.add(lblNotaForm);

		txtNota = new JTextField();
		txtNota.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtNota.setBounds(300, 35, 100, 32);
		panelFormulario.add(txtNota);
		txtNota.setColumns(10);

		JLabel lblObsForm = new JLabel("Observaciones / Feedback:");
		lblObsForm.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblObsForm.setForeground(new Color(71, 85, 105));
		lblObsForm.setBounds(420, 15, 200, 16);
		panelFormulario.add(lblObsForm);

		txtObservaciones = new JTextField();
		txtObservaciones.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtObservaciones.setBounds(420, 35, 360, 32);
		panelFormulario.add(txtObservaciones);
		txtObservaciones.setColumns(10);

		btnGuardarNota = new JButton("GUARDAR NOTA");
		btnGuardarNota.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnGuardarNota.setForeground(Color.WHITE);
		btnGuardarNota.setBackground(new Color(196, 18, 48));
		btnGuardarNota.setBounds(795, 32, 145, 38);
		btnGuardarNota.setFocusPainted(false);
		btnGuardarNota.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelFormulario.add(btnGuardarNota);

		JLabel lblAvisoForm = new JLabel("Haz clic sobre una fila de la tabla para cargar automáticamente los datos del alumno y editar su nota.");
		lblAvisoForm.setForeground(new Color(148, 163, 184));
		lblAvisoForm.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		lblAvisoForm.setBounds(20, 80, 800, 18);
		panelFormulario.add(lblAvisoForm);

		// Evento al pulsar sobre una fila para volcar los valores al formulario
		tablaAlumnos.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting() && tablaAlumnos.getSelectedRow() != -1) {
				int fila = tablaAlumnos.getSelectedRow();
				txtAlumnoSeleccionado.setText(tablaAlumnos.getValueAt(fila, 2).toString());
				txtNota.setText(tablaAlumnos.getValueAt(fila, 3).toString().replace("NP", ""));
				txtObservaciones.setText(tablaAlumnos.getValueAt(fila, 5).toString());
			}
		});
	}

	public usuarios getDocenteActivo() { return docenteActivo; }
	public JTable getTablaAlumnos() { return tablaAlumnos; }
	public DefaultTableModel getModeloTabla() { return modeloTabla; }
	public JComboBox<String> getComboCursos() { return comboCursos; }
	public JComboBox<String> getComboModulos() { return comboModulos; }
	public JTextField getTxtAlumnoSeleccionado() { return txtAlumnoSeleccionado; }
	public JTextField getTxtNota() { return txtNota; }
	public JTextField getTxtObservaciones() { return txtObservaciones; }
	public JButton getBtnGuardarNota() { return btnGuardarNota; }
	public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
	public JLabel getLblProfesorNombre() { return lblProfesorNombre; }
}
