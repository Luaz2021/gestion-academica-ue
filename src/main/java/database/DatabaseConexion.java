package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Gestion centralizada de la conexion JDBC a MariaDB en Laragon.
 * Aplico el patron Singleton para mantener una unica instancia activa
 * y evitar saturar el pool de conexiones del servidor local.
 */
public class DatabaseConexion {

	// Parametros de configuracion para el servidor local de Laragon
	private static final String URL = "jdbc:mariadb://localhost:3306/universidad_europea";
	private static final String USER = "root";
	private static final String PASSWORD = "";

	// Instancia compartida de la conexion
	private static Connection conexion;

	// Constructor privado para impedir instanciaciones externas
	private DatabaseConexion() {}

	// Obtencion de la conexion activa con reconexion automatica si se cerro
	public static synchronized Connection getConexion() {
		try {
			if (conexion == null || conexion.isClosed()) {
				Class.forName("org.mariadb.jdbc.Driver");
				conexion = DriverManager.getConnection(URL, USER, PASSWORD);
				System.out.println("Conexion activa con universidad_europea.");
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Fallo al conectar con la BD: " + e.getMessage());
		}
		return conexion;
	}

	// Metodo seguro para liberar los recursos al cerrar la aplicacion
	public static void desconectar() {
		try {
			if (conexion != null && !conexion.isClosed()) {
				conexion.close();
				System.out.println("Conexion cerrada.");
			}
		} catch (SQLException e) {
			System.err.println("Error al cerrar: " + e.getMessage());
		}
	}
}
