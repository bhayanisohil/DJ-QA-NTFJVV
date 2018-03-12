import java.sql.*;

public class DatabaseQueries {

	public static int getLatestIdinForm(String formName) throws ClassNotFoundException, SQLException {
		int id = 0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost/" + Constants.DB_NAME,
					Constants.DB_USER_NAME, Constants.DB_PASSWORD);
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM " + formName + " ORDER BY id DESC LIMIT 1");
			while (rs.next())
				System.out.println("latest id in table " + formName + " is " + rs.getInt(1));
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return id;
	}

	public static Connection getDBConnection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost/" + Constants.DB_NAME,
				Constants.DB_USER_NAME, Constants.DB_PASSWORD);
		return con;
	}

	public static int getLatestIdFromTable(Connection con, String formName) throws SQLException {
		int id = 0;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM " + formName + " ORDER BY id DESC LIMIT 1");
			while (rs.next()) {
				System.out.println("latest id in table " + formName + " is " + rs.getInt(1));
				id = rs.getInt(1);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (con != null) {
				con.close();
			}
		}

		return id;
	}

}
