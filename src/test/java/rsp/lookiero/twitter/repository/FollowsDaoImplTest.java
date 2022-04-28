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
import rsp.lookiero.twitter.model.Follows;

@ExtendWith(MockitoExtension.class)
public class FollowsDaoImplTest {

	@Mock
	Connection conn;

	@Mock
	PreparedStatement pst;

	@Mock
	ResultSet rs;
	
	@Mock
	PostgresConnection pgConnection;
	
	@InjectMocks
	FollowsDaoImpl followsDaoImpl;
	
	@BeforeEach
	public void initEach() throws SQLException{
		Mockito.when(pgConnection.connect()).thenReturn(conn);
	}
	
	@Test
	public void obtainFollowers_WithExistingUsers() throws SQLException {
		
		Follows follows = new Follows();
		follows.setUser_id(rs.getInt(1));
		follows.setUser_id_followed(rs.getInt(2));

		Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(pst);
		Mockito.when(pst.executeQuery()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		Mockito.when(rs.getInt(1)).thenReturn(101);
		Mockito.when(rs.getInt(2)).thenReturn(201);		
		
		List<Follows> resultList = followsDaoImpl.obtainFollowers(1);
        
        assertEquals(resultList.size(), 1);
        assertEquals(resultList.get(0).getUser_id(), 101); 
        assertEquals(resultList.get(0).getUser_id_followed(), 201);
        Mockito.verify(conn, Mockito.times(1)).close();		
	}
	
	@Test
	public void insert() throws SQLException {		

		Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(pst);
        
        followsDaoImpl.insert(1,2);
        
        Mockito.verify(pst, Mockito.times(1)).executeUpdate();
        Mockito.verify(conn, Mockito.times(1)).close();		
	}
}
