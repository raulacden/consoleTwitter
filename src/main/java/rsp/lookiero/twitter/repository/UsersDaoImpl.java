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

	PostgresConnection pgConnection = new PostgresConnection();

	static {
		String path = UsersDaoImpl.class.getClassLoader().getResource("logging.properties").getFile();
		System.setProperty("java.util.logging.config.file", path);
	}

	private static Logger logger = Logger.getLogger(UsersDaoImpl.class.getName());

	@Override
	public List<User> obtainCurrentUsers() {

		Connection conn = null;

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
		int userId;
		String insertUserSQL = "Insert into users (username) values (?)";
		
		try {
			conn = pgConnection.connect();
			PreparedStatement pst = conn.prepareStatement(insertUserSQL, Statement.RETURN_GENERATED_KEYS);
			
			pst.setString(1, user.getUsername());
			pst.executeUpdate();
		
			try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
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
		
		String name = "";
		
		String SQL = "SELECT username "
                + "FROM users "
                + "WHERE id = ?";

        try {
        	conn = pgConnection.connect();       
            PreparedStatement pst = conn.prepareStatement(SQL);

            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();		
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

	@Override
	public int getIdByName(String inputName) {
		
		Connection conn = null;
		
		int user_id = 0;
		
		String SQL = "SELECT id "
                + "FROM users "
                + "WHERE username = ?";

        try {
        	conn = pgConnection.connect();       
            PreparedStatement pst = conn.prepareStatement(SQL);

            pst.setString(1, inputName);
            ResultSet rs = pst.executeQuery();		
            while (rs.next()) {
            	user_id = rs.getInt(1);
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
        return user_id;
	}

}
