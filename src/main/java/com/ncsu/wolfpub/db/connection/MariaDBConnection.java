package com.ncsu.wolfpub.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This is the class that connects to MariaDB.
 * @author vamsi
 *
 */
public class MariaDBConnection implements IDBConnection, AutoCloseable {

	private static final String DRIVER_CLASS = "org.mariadb.jdbc.Driver";
	
	private String jdbcURL = "";
	private String user = "";
	private String password = "";

	Connection connection = null;

	public MariaDBConnection(String user, String password, String jdbcURL) {
		this.user = user;
		this.password = password;
		this.jdbcURL = jdbcURL;
	}

	@Override
	public void init() {
		// Get a connection instance from the first driver in the
		// DriverManager list that recognizes the URL jdbcURL

		try {
			Class.forName(DRIVER_CLASS);
			connection = DriverManager.getConnection(jdbcURL, user, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Connection getConnection() {
		if (connection == null)
			init();

		return connection;
	}

	@Override
	public void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (Throwable whatever) {
				//TODO : 
			}
		}
	}
}
