package rsp.lookiero.twitter.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import rsp.lookiero.twitter.config.PostgresConnection;
import rsp.lookiero.twitter.model.User;

public class UsersDaoImpl implements UsersDao {

	PostgresConnection pgConnection;

	static {
		String path = UsersDaoImpl.class.getClassLoader().getResource("logging.properties").getFile();
		System.setProperty("java.util.logging.config.file", path);
	}

	private static Logger logger = Logger.getLogger(UsersDaoImpl.class.getName());

	@Override
	public List<User> obtainCurrentUsers() {

		Connection conn = null;
		pgConnection = new PostgresConnection();

		List<User> listUsers = new ArrayList<>();

		try {
			conn = pgConnection.connect();
			PreparedStatement pst = conn.prepareStatement("SELECT * FROM users");
			ResultSet rs = pst.executeQuery();

			while (rs.next()) {
				User user = new User();
				user.setId(rs.getInt(1));
				user.setUsername(rs.getString(2));
				listUsers.add(user);
			}

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return listUsers;
	}

	@Override
	public int insert(User user) {	

		Connection conn = null;
		pgConnection = new PostgresConnection();
		int userId;
		String insertUserSQL = "Insert into users (username) values (?)";
		
		try {
			conn = pgConnection.connect();
			PreparedStatement prepStmt = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS);
			prepStmt.setString(1, user.getUsername());
		    prepStmt.executeUpdate();
		
			try (ResultSet generatedKeys = prepStmt.getGeneratedKeys()) {
			      if (generatedKeys.next()) {
			        userId = generatedKeys.getInt(1);
			      }
			      else {
			    	  conn.rollback();
			    	  throw new SQLException("Ha habido un problema en el registro del usuario");
			      }
			    }
		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return userId;		
				
	}

	@Override
	public String getNameById(int userId) {

		Connection conn = null;
		pgConnection = new PostgresConnection();
		
		String name = "";
		
		String SQL = "SELECT username "
                + "FROM users "
                + "WHERE id = ?";

        try {
        	conn = pgConnection.connect();       
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();		
            while (rs.next()) {
            	name = rs.getString(1);
            }

		} catch (SQLException e) {
			logger.severe(e.getMessage());
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
        return name;
	}

}
