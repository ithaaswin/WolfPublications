package com.ncsu.wolfpub.contents;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ncsu.wolfpub.db.connection.DBConnectionClient;
import com.ncsu.wolfpub.db.connection.IDBConnection;

/**
 * This is the main class to create the tables in the database as mentioned in
 * <b> wolf_schema.txt</b> and populates the data from <b> wolf_data.txt</b>.
 * 
 * @author vamsi
 *
 */
public class CreateContents {

	private static final String DELIMETER = "<EOQ>";

	private static final String TABLE_TAG = "<TABLE>";
	private static final String TABLE_END_TAG = "</TABLE>";

	private static final String SCHEMA_FILE_NAME = "/wolf_schema.txt";
	private static final String DATA_FILE_NAME = "/demoData.txt";

	private static Pattern p = Pattern.compile(TABLE_TAG + "(.+?)" + TABLE_END_TAG, Pattern.DOTALL);

	public static void main(String[] args) {

		try {
			executeDBCommands(SCHEMA_FILE_NAME);
			executeDBCommands(DATA_FILE_NAME);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			terminate();
		}

	}

	private static void executeDBCommands(String fileName) throws URISyntaxException {

		Statement statement = null;

		IDBConnection client = DBConnectionClient.getDBClient();
		Connection connection = client.getConnection();
		// Create a statement instance that will be sending
		// your SQL statements to the DBMS
		try {
			statement = connection.createStatement();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		StringBuilder command = new StringBuilder();
		// java.io.InputStream
		String line = "";
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(CreateContents.class.getResourceAsStream(fileName)))) {
			
			while ((line = br.readLine()) != null) {
				if (line.contains(DELIMETER)) {
					statement.executeUpdate(command.toString());
					command.setLength(0);
				} else if (line.startsWith(TABLE_TAG)) {
					String tableName = getTableName(line);
					String isTableExistsCommand = isTableExistsCommand(tableName);

					ResultSet result = statement.executeQuery(isTableExistsCommand);
					if (result.next()) {
						String dropTableCommand = getDropTableCommand(tableName);
						statement.executeUpdate(dropTableCommand);
					}
				} else {
					command.append(line);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(command.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println(command.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			close(statement);
		}
	}

	private static String isTableExistsCommand(String tableName) {
		return "show tables like \'" + tableName + "\'";
	}

	private static String getTableName(String line) {
		Matcher m = p.matcher(line);
		m.find();
		return m.group(1);
	}

	private static String getDropTableCommand(String tableName) {
		StringBuilder dropTableCommand = new StringBuilder();
		dropTableCommand.append("DROP TABLE ").append(tableName);
		return dropTableCommand.toString();
	}

	private static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (Throwable whatever) {
			}
		}
	}

	private static void terminate() {
		DBConnectionClient.close();
	}
}
