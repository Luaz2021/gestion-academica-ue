package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.formdev.flatlaf.FlatLightLaf;

/*
 * Ventana inicial de autenticacion para docentes y alumnos.
 * Diseñada sobre una tarjeta flotante de dos paneles para garantizar
 * estetica limpia y compatibilidad completa con el editor grafico WindowBuilder.
 */
public class Login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelIzquierdo;
	private JPanel panelOverlayTexto;
	private JPanel panelDerecho;
	private JTextField txtUsuario;
	private JPasswordField txtPassword;
	private JComboBox<String> comboRol;
	private JButton btnIngresar;
	private JButton btnLimpiar;
	private JToggleButton btnOjo;
	private JLabel lblLogo;
	private JLabel lblFondoIzquierdo;
	private JLabel lblTituloUE;
	private JLabel lblTituloUE2;
	private JLabel lblSubtituloUE;
	private JLabel lblBadgeSede;
	private JLabel lblTituloForm;
	private JLabel lblDescForm;
	private JLabel lblUsuario;
	private JLabel lblPassword;
	private JLabel lblRol;
	private JLabel lblAyuda;

	// Metodo principal para arrancar y probar la vista directamente
	public static void main(String[] args) {
		try {
			FlatLightLaf.setup();
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					new controller.LoginController(frame);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Constructor de la ventana con disposicion absoluta para WindowBuilder
	public Login() {
		setTitle("Ventana de inicio");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 820, 520);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(241, 245, 249));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Panel lateral con imagen de fondo institucional
		panelIzquierdo = new JPanel();
		panelIzquierdo.setBounds(20, 20, 310, 440);
		contentPane.add(panelIzquierdo);
		panelIzquierdo.setLayout(null);

		// Tarjeta blanca semitransparente para proteger la legibilidad del texto
		panelOverlayTexto = new JPanel();
		panelOverlayTexto.setBackground(new Color(255, 255, 255, 225));
		panelOverlayTexto.setBorder(new LineBorder(new Color(255, 255, 255, 180), 1, true));
		panelOverlayTexto.setBounds(18, 120, 274, 200);
		panelIzquierdo.add(panelOverlayTexto);
		panelOverlayTexto.setLayout(null);

		lblTituloUE = new JLabel("UNIVERSIDAD");
		lblTituloUE.setForeground(new Color(15, 23, 42));
		lblTituloUE.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTituloUE.setBounds(20, 15, 234, 28);
		panelOverlayTexto.add(lblTituloUE);

		lblTituloUE2 = new JLabel("EUROPEA");
		lblTituloUE2.setForeground(new Color(196, 18, 48));
		lblTituloUE2.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblTituloUE2.setBounds(20, 42, 234, 28);
		panelOverlayTexto.add(lblTituloUE2);

		lblSubtituloUE = new JLabel("<html>Gestión Integral de Calificaciones y Seguimiento Académico 2026.</html>");
		lblSubtituloUE.setForeground(new Color(51, 65, 85));
		lblSubtituloUE.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSubtituloUE.setBounds(20, 78, 234, 45);
		panelOverlayTexto.add(lblSubtituloUE);

		lblBadgeSede = new JLabel("CAMPUS MADRID");
		lblBadgeSede.setForeground(new Color(100, 116, 139));
		lblBadgeSede.setFont(new Font("Segoe UI", Font.BOLD, 10));
		lblBadgeSede.setBounds(20, 155, 234, 20);
		panelOverlayTexto.add(lblBadgeSede);

		lblFondoIzquierdo = new JLabel("");
		lblFondoIzquierdo.setIcon(new ImageIcon(Login.class.getResource("/fondo_ue_scaled.png")));
		lblFondoIzquierdo.setBounds(0, 0, 310, 440);
		panelIzquierdo.add(lblFondoIzquierdo);

		// Panel derecho con el formulario de inicio de sesion
		panelDerecho = new JPanel();
		panelDerecho.setBackground(Color.WHITE);
		panelDerecho.setBorder(new LineBorder(new Color(226, 232, 240), 1, true));
		panelDerecho.setBounds(345, 20, 440, 440);
		contentPane.add(panelDerecho);
		panelDerecho.setLayout(null);

		lblLogo = new JLabel("");
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setIcon(new ImageIcon(Login.class.getResource("/logo_ue.png")));
		lblLogo.setBounds(45, 18, 350, 55);
		panelDerecho.add(lblLogo);

		lblTituloForm = new JLabel("Iniciar Sesión");
		lblTituloForm.setForeground(new Color(15, 23, 42));
		lblTituloForm.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTituloForm.setBounds(45, 80, 200, 24);
		panelDerecho.add(lblTituloForm);

		lblDescForm = new JLabel("Ingresa tus credenciales del campus");
		lblDescForm.setForeground(new Color(100, 116, 139));
		lblDescForm.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblDescForm.setBounds(45, 104, 300, 16);
		panelDerecho.add(lblDescForm);

		lblUsuario = new JLabel("Usuario / DNI / NIE");
		lblUsuario.setForeground(new Color(71, 85, 105));
		lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblUsuario.setBounds(45, 130, 350, 16);
		panelDerecho.add(lblUsuario);

		txtUsuario = new JTextField();
		txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtUsuario.setBounds(45, 150, 350, 34);
		txtUsuario.putClientProperty("JTextField.placeholderText", "Ej. UEM-ALUM-SAM o UEM-DOC-JN");
		panelDerecho.add(txtUsuario);
		txtUsuario.setColumns(10);

		lblPassword = new JLabel("Contraseña");
		lblPassword.setForeground(new Color(71, 85, 105));
		lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblPassword.setBounds(45, 192, 350, 16);
		panelDerecho.add(lblPassword);

		txtPassword = new JPasswordField();
		txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		txtPassword.setBounds(45, 212, 305, 34);
		panelDerecho.add(txtPassword);

		btnOjo = new JToggleButton("👁");
		btnOjo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
		btnOjo.setBounds(355, 212, 40, 34);
		btnOjo.setMargin(new Insets(0, 0, 0, 0));
		btnOjo.setFocusPainted(false);
		btnOjo.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnOjo.addActionListener(e -> {
			if (btnOjo.isSelected()) {
				txtPassword.setEchoChar((char) 0);
			} else {
				txtPassword.setEchoChar('•');
			}
		});
		panelDerecho.add(btnOjo);

		lblRol = new JLabel("Rol de Acceso");
		lblRol.setForeground(new Color(71, 85, 105));
		lblRol.setFont(new Font("Segoe UI", Font.BOLD, 12));
		lblRol.setBounds(45, 254, 350, 16);
		panelDerecho.add(lblRol);

		comboRol = new JComboBox<>(new String[]{"DOCENTE", "ALUMNO"});
		comboRol.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		comboRol.setBounds(45, 274, 350, 34);
		panelDerecho.add(comboRol);

		// Boton principal de acceso
		btnIngresar = new JButton("ENTRAR");
		btnIngresar.setFont(new Font("Segoe UI", Font.BOLD, 13));
		btnIngresar.setForeground(Color.WHITE);
		btnIngresar.setBackground(new Color(196, 18, 48));
		btnIngresar.setBounds(45, 325, 210, 42);
		btnIngresar.setFocusPainted(false);
		btnIngresar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelDerecho.add(btnIngresar);

		// Boton secundario para limpiar el formulario
		btnLimpiar = new JButton("LIMPIAR");
		btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnLimpiar.setForeground(new Color(71, 85, 105));
		btnLimpiar.setBackground(new Color(241, 245, 249));
		btnLimpiar.setBorder(new LineBorder(new Color(203, 213, 225), 1, true));
		btnLimpiar.setBounds(265, 325, 130, 42);
		btnLimpiar.setFocusPainted(false);
		btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
		panelDerecho.add(btnLimpiar);

		lblAyuda = new JLabel("¿Problemas para acceder? Contacta con soporte@universidadeuropea.es");
		lblAyuda.setHorizontalAlignment(SwingConstants.CENTER);
		lblAyuda.setForeground(new Color(148, 163, 184));
		lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 10));
		lblAyuda.setBounds(40, 395, 360, 20);
		panelDerecho.add(lblAyuda);
	}

	public JTextField getTxtUsuario() { return txtUsuario; }
	public JPasswordField getTxtPassword() { return txtPassword; }
	public JComboBox<String> getComboRol() { return comboRol; }
	public JButton getBtnIngresar() { return btnIngresar; }
	public JButton getBtnLimpiar() { return btnLimpiar; }
	public JLabel getLblLogo() { return lblLogo; }
}
