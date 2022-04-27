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

	String url = "jdbc:postgresql://ec2-52-30-159-47.eu-west-1.compute.amazonaws.com:5432/ddno89e61ojo1l";
	String user = "vgrrtwupgtqbjp";
	String password = "86aec189e179ad24ccbc9cc1f9c963c3c9303eb22db4186b2728019bf796907a";

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
