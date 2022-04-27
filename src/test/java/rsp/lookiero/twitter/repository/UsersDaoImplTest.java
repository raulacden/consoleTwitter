package rsp.lookiero.twitter.repository;


import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import rsp.lookiero.twitter.model.User;

public class UsersDaoImplTest {
	
	UsersDaoImpl usersDaoImpl = new UsersDaoImpl();
	
	@Test
	public void obtainCurrentUsers_withNonData() {
		
		List<User> listUsers = usersDaoImpl.obtainCurrentUsers();
				
		Assertions.assertEquals(0, listUsers.size());
	}

}
