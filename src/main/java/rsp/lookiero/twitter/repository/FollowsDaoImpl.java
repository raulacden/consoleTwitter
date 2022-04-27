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
import rsp.lookiero.twitter.model.Follows;

public class FollowsDaoImpl implements FollowsDao {
	
PostgresConnection pgConnection;
	
	static {
		String path = FollowsDaoImpl.class.getClassLoader().getResource("logging.properties").getFile();
		System.setProperty("java.util.logging.config.file", path);
	}

	private static Logger logger = Logger.getLogger(FollowsDaoImpl.class.getName());

	@Override
	public void insert(int follower, int followed) {
		
		Connection conn = null;
		pgConnection = new PostgresConnection();
        try {
        	conn = pgConnection.connect();
	        try (Statement stmt = conn.createStatement()) {                
	            stmt.executeUpdate("insert into follows(user_id,user_id_followed) values ("
	                    + follower + ","
	                    + followed + ");");
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

	}

	@Override
	public List<Follows> obtainFollowers(int follower_id) {
		
		Connection conn = null;
		pgConnection = new PostgresConnection();

		List<Follows> listFollows = new ArrayList<>();

		String SQL = "SELECT * "
                + "FROM follows "
                + "WHERE user_id = ?";

        try {
        	conn = pgConnection.connect();       
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setInt(1, follower_id);
            ResultSet rs = pstmt.executeQuery();		
				
			while (rs.next()) {
				Follows follows = new Follows();
				follows.setUser_id(rs.getInt(1));
				follows.setUser_id_followed(rs.getInt(2));
				listFollows.add(follows);
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
        
        return listFollows;
	}

}
