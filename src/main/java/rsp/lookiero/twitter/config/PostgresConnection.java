package rsp.lookiero.twitter.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class PostgresConnection {

	static {
		String path = PostgresConnection.class.getClassLoader().getResource("logging.properties").getFile();
		System.setProperty("java.util.logging.config.file", path);
	}

	private static Logger logger = Logger.getLogger(PostgresConnection.class.getName());

	String url = "jdbc:postgresql://ec2-99-80-170-190.eu-west-1.compute.amazonaws.com:5432/d7vhhcscmjq2gr";
	String user = "irpxhviajnffkm";
	String password = "4911618bc6ed758f6740bc30c355895f0f1799f47ae59f3e81a8d9da00e7e8dd";

	public Connection connect() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			logger.info("Connected to the PostgreSQL server successfully.");
		} catch (SQLException e) {
			logger.severe(e.getMessage());
		}

		return conn;
	}

}
