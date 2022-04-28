package rsp.lookiero.twitter.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import rsp.lookiero.twitter.config.PostgresConnection;
import rsp.lookiero.twitter.model.Posts;

public class PostsDaoImpl implements PostsDao {
	
	PostgresConnection pgConnection = new PostgresConnection();
	
	static {
		String path = PostsDaoImpl.class.getClassLoader().getResource("logging.properties").getFile();
		System.setProperty("java.util.logging.config.file", path);
	}

	private static Logger logger = Logger.getLogger(PostsDaoImpl.class.getName());

	@Override
	public void insert(Posts posts) {
		
		Connection conn = null;
		String insertPostsSQL = "Insert into posts (user_id,text) values (?,?)";
		
        try {
        	conn = pgConnection.connect();
        	PreparedStatement pst = conn.prepareStatement(insertPostsSQL);
        	
        	pst.setInt(1,  posts.getUser_id());
        	pst.setString(2, posts.getText());
        	
			pst.executeUpdate();
        	
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
	public List<Posts> obtainByUser(int userId) {

		List<Posts> listPosts = new ArrayList<>();

		Connection conn = null;		
		String obtainPostsSQL = "SELECT * "
                + "FROM posts "
                + "WHERE user_id = ?";

        try {
        	conn = pgConnection.connect();       
            PreparedStatement pst = conn.prepareStatement(obtainPostsSQL);

            pst.setInt(1, userId);
            
            ResultSet rs = pst.executeQuery();		
				
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
