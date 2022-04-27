package rsp.lookiero.twitter.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import rsp.lookiero.twitter.config.PostgresConnection;
import rsp.lookiero.twitter.model.Posts;

public class PostsDaoImpl implements PostsDao {
	
	PostgresConnection pgConnection;
	
	static {
		String path = PostsDaoImpl.class.getClassLoader().getResource("logging.properties").getFile();
		System.setProperty("java.util.logging.config.file", path);
	}

	private static Logger logger = Logger.getLogger(PostsDaoImpl.class.getName());

	@Override
	public void insert(Posts posts) {
		Connection conn = null;
		pgConnection = new PostgresConnection();
        try {
           conn = pgConnection.connect();
            try (Statement stmt = conn.createStatement()) {                
                stmt.executeUpdate("insert into posts(user_id,text) values ("
                        + posts.getUser_id() + ",'"
                        + posts.getText() + "');");
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
	public void deleteById(Integer id) {		
		
	}

	@Override
	public List<Posts> obtainByUser(int userId) {

		Connection conn = null;
		
		pgConnection = new PostgresConnection();

		List<Posts> listPosts = new ArrayList<>();
		
		String SQL = "SELECT * "
                + "FROM posts "
                + "WHERE user_id = ?";

        try {
        	conn = pgConnection.connect();       
            PreparedStatement pstmt = conn.prepareStatement(SQL);

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();		
				
			while (rs.next()) {
				Posts posts = new Posts();
				posts.setUser_id(rs.getInt(2));
				posts.setText(rs.getString(3));
				posts.setDate(rs.getObject(4, LocalDateTime.class));
				listPosts.add(posts);
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
		
		return listPosts;
		
	}

}
