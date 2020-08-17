/**
 * 
 */
package dbUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class for simpler and easier connection to our MySQL database.
 * @author Eva Rubio
 *
 */
public class DBConnector {

	private static final String USERNAME = "root";
	private static final String PASSWORD = "Sudafrica1$";

	// To Connect to the database. (our connection)
	private static final String CONN = "jdbc:mysql://localhost:3306/volunteer?serverTimezone=UTC";


	public static Connection getConnection() throws SQLException {

		try {
			
			return DriverManager.getConnection();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.out.println(e);
			StackTraceElement l = new Exception().getStackTrace()[0];
			System.out.println(l.getClassName()+"/"+l.getMethodName()+":"+l.getLineNumber());
		}

	}






}
