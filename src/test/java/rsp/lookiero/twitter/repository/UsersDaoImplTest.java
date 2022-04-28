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
import rsp.lookiero.twitter.model.User;

@ExtendWith(MockitoExtension.class)
public class UsersDaoImplTest {

	@Mock
	Connection conn;

	@Mock
	PreparedStatement pst;

	@Mock
	ResultSet rs;
	
	@Mock
	PostgresConnection pgConnection;

	@InjectMocks
	UsersDaoImpl usersDaoImpl;
	
	@BeforeEach
	public void initEach() throws SQLException{
		Mockito.when(pgConnection.connect()).thenReturn(conn);
	}

	@Test
	public void obtainCurrentUsers() throws SQLException {
		
		User user = new User();
		user.setId(1);
		user.setUsername("Raul");		

		Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(pst);
		Mockito.when(pst.executeQuery()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		Mockito.when(rs.getInt(1)).thenReturn(user.getId());
        Mockito.when(rs.getString(2)).thenReturn(user.getUsername());
        
        List<User> resultList = usersDaoImpl.obtainCurrentUsers();
        
        assertEquals(resultList.size(), 1);
        assertEquals(resultList.get(0).getUsername(), user.getUsername());        
        Mockito.verify(conn, Mockito.times(1)).close();		
	}
	
	@Test
	public void insert() throws SQLException {
		
		User user = new User();
		user.setUsername("Raul");

		Mockito.when(conn.prepareStatement(Mockito.any(String.class),Mockito.anyInt())).thenReturn(pst);

		Mockito.when(pst.getGeneratedKeys()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		Mockito.when(rs.getInt(1)).thenReturn(1);
        
        int user_id = usersDaoImpl.insert(user);
        
        assertEquals(user_id, 1);      
        Mockito.verify(conn, Mockito.times(1)).close();		
	}
	
	@Test
	public void getNameById_withExistingUser() throws SQLException {
		
		
		Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(pst);

		Mockito.when(pst.executeQuery()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		Mockito.when(rs.getString(1)).thenReturn("Raul");
        
        String user_name = usersDaoImpl.getNameById(1);
        
        assertEquals(user_name, "Raul");      
        Mockito.verify(conn, Mockito.times(1)).close();		
	}
	
	@Test
	public void getIdByName_withExistingUser() throws SQLException {
		
		
		Mockito.when(conn.prepareStatement(Mockito.any(String.class))).thenReturn(pst);

		Mockito.when(pst.executeQuery()).thenReturn(rs);
		Mockito.when(rs.next()).thenReturn(true).thenReturn(false);
		Mockito.when(rs.getInt(1)).thenReturn(101);
        
        int user_id = usersDaoImpl.getIdByName("Test");
        
        assertEquals(user_id, 101);      
        Mockito.verify(conn, Mockito.times(1)).close();		
	}

}
