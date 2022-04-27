package rsp.lookiero.twitter.controller;


import java.io.InputStream;
import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import rsp.lookiero.twitter.model.Posts;
import rsp.lookiero.twitter.model.User;
import rsp.lookiero.twitter.repository.PostsDaoImpl;
import rsp.lookiero.twitter.repository.UsersDaoImpl;
import rsp.lookiero.twitter.utils.ConsoleIO;

@ExtendWith(MockitoExtension.class)
public class TwitterTest {

	
	@Mock
	ConsoleIO consoleIO;
	
	@Mock
	UsersDaoImpl usersDaoImpl;
	
	@Mock
	PostsDaoImpl postsDaoImpl;

	@InjectMocks
	Twitter twitter;
	

    private final InputStream systemIn = System.in;
    
    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }
	
	@Test
	public void submitPosts_withExistingUser() {
		
		User mockUser = new User();
		mockUser.setId(1);
		mockUser.setUsername("Raul");		
		Posts mockPosts = new Posts();
		mockPosts.setUser_id(mockUser.getId());
		mockPosts.setText("Test");
		
		Mockito.when(consoleIO.readLine()).thenReturn("Raul -> Test","Close");
		Mockito.when(usersDaoImpl.obtainCurrentUsers()).thenReturn(Arrays.asList(mockUser));
		
		twitter.start();
		
		Mockito.verify(consoleIO, Mockito.times(1)).printLine("Saved!");		
		
	}

}
