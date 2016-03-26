package com.epsilon.LeakHawk.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class DBConnector {

	public static Logger LOGGER = Logger.getLogger("DBConnector");

	public static final String DB_USERNAME = "epsilontest";

	public static final String DB_PASSWORD = "epsilontest";

	public static final String DATABASE = "jdbc:mysql://localhost:3306/epsilondb";

	public static Connection getConnection() throws SQLException {

		return DriverManager.getConnection(DATABASE, DB_USERNAME, DB_PASSWORD);
	}

	public static void closeConnection(Connection con) throws SQLException {
		if (con != null) {
			con.close();
		}
	}

	public static void closeResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			rs.close();
		}
	}

	public static void closeStatement(PreparedStatement st) throws SQLException {
		if (st != null) {
			st.close();
		}
	}

	public static void closeStatement(Statement st) throws SQLException {
		if (st != null) {
			st.close();
		}
	}

}
