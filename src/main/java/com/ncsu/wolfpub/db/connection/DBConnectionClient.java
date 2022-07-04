package com.ncsu.wolfpub.db.connection;

import java.util.Map;

import com.ncsu.wolfpub.util.PropertiesReader;

/**
 * This class maintains the single DBConnection for the application. This is to
 * avoid multiple database connection and does the overhead to create and close
 * connection each time.
 * 
 * @author vamsi
 *
 */
public final class DBConnectionClient {

	private static final String DBCONNECTION_PROPERTIES_FILE = "dbconnection.properties";

	private static final String DB_TYPE = "dbType";
	
	private static final String MARIA_DB = "mairadb";
	
	private static final String JDBC_URL = "jdbcUrl";

	private static final String USER_NAME = "username";

	private static final String PASSWORD = "password";

	private static IDBConnection client = null;

	public static IDBConnection getDBClient() {
		if (client == null) {
			synchronized (DBConnectionClient.class) {
				if (client == null) {
					initializeDBClient();
				}
			}
		}
		return client;
	}

	private static void initializeDBClient() {
		try {
			Map<String, String> props = PropertiesReader.readProperties(DBCONNECTION_PROPERTIES_FILE);
			
			String dbType = props.get(DB_TYPE);
			
			if(MARIA_DB.equals(dbType)) {
				String jdbcURL = props.get(JDBC_URL);
				String userName = props.get(USER_NAME);
				String password = props.get(PASSWORD);
			
				client = new MariaDBConnection(userName, password, jdbcURL);
			} else {
				System.out.println("\nCurretnly this DB Type is not supported");
			}
			
		} catch (Exception e) {
			System.out.println("\nError initialising DB Client");
		}
	}

	public static void close() {
		if (client != null)
			client.close();
	}
}
