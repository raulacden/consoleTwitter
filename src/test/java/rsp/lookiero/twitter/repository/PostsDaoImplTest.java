package rsp.lookiero.twitter.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import rsp.lookiero.twitter.config.PostgresConnection;
import rsp.lookiero.twitter.model.Posts;

@ExtendWith(MockitoExtension.class)
public class PostsDaoImplTest {
	
	@Mock
	Connection conn;

	@Mock
	PreparedStatement pst;

	@Mock
	ResultSet rs;
	
	@Mock
	PostgresConnection pgConnection;
	
	@InjectMocks
	PostsDaoImpl postsDaoImpl;
	
	@BeforeEach
	public void initEach() throws SQLException{
		Mockito.when(pgConnection.connect()).thenReturn(conn);
	}
	
	@Test
	public void obtainByUser_WithExistingUsers() throws SQLException {
		
		
		Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(pst);
		Mockito.when(pst.executeQuery()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		Mockito.when(rs.getInt(2)).thenReturn(101);
		Mockito.when(rs.getString(3)).thenReturn("Test");
		
		List<Posts> resultList = postsDaoImpl.obtainByUser(1);
        
        assertEquals(resultList.size(), 1);
        assertEquals(resultList.get(0).getUser_id(), 101); 
        assertEquals(resultList.get(0).getText(), "Test"); 
        Mockito.verify(conn, Mockito.times(1)).close();		
	}
	
	@Test
	public void insert() throws SQLException {
		
		Posts mockPost = new Posts();
		mockPost.setUser_id(1);
		mockPost.setText("Test String");

		Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(pst);
        
		postsDaoImpl.insert(mockPost);
        
        Mockito.verify(pst, Mockito.times(1)).executeUpdate();
        Mockito.verify(conn, Mockito.times(1)).close();		
	}

}
