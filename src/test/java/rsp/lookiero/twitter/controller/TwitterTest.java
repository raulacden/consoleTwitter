package rsp.lookiero.twitter.controller;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import rsp.lookiero.twitter.model.Follows;
import rsp.lookiero.twitter.model.Posts;
import rsp.lookiero.twitter.model.User;
import rsp.lookiero.twitter.repository.FollowsDaoImpl;
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
	
	@Mock
	FollowsDaoImpl followsDaoImpl;

	@InjectMocks
	Twitter twitter;
	
	
	@Test
	public void submitPosts_withExistingUser() {
		
		User mockUser = new User();
		mockUser.setId(1);
		mockUser.setUsername("Raul");		
		Posts mockPosts = new Posts();
		mockPosts.setUser_id(mockUser.getId());
		mockPosts.setText("Test");
		
		Mockito.when(consoleIO.readLine()).thenReturn("Raul -> Test").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(mockUser.getId());
		
		twitter.start();
		
		Mockito.verify(usersDaoImpl, Mockito.times(0)).insert(Mockito.any());
		Mockito.verify(postsDaoImpl, Mockito.times(1)).insert(Mockito.any());		
		Mockito.verify(consoleIO, Mockito.times(1)).printLine("Saved!");		
		
	}
	
	@Test
	public void submitPosts_withNonExistingUser() {
		
		
		Mockito.when(consoleIO.readLine()).thenReturn("Raul -> Test").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(0);
		
		twitter.start();
		
		Mockito.verify(usersDaoImpl, Mockito.times(1)).insert(Mockito.any());		
		Mockito.verify(consoleIO, Mockito.times(1)).printLine("Saved!");
		
	}
	
	@Test
	public void followUsers_withExistingUsers() {

		User mockUser = new User();
		mockUser.setId(1);
		mockUser.setUsername("Raul");
		User mockUser2 = new User();
		mockUser2.setId(2);
		mockUser2.setUsername("Paula");

		Mockito.when(consoleIO.readLine()).thenReturn("Raul follows Paula").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(mockUser.getId()).thenReturn(mockUser2.getId());

		twitter.start();

		Mockito.verify(followsDaoImpl, Mockito.times(1)).insert(Mockito.anyInt(),Mockito.anyInt());

	}
	
	@Test
	public void followUsers_withAlreadyUsersFollowers() {

		User mockUser = new User();
		mockUser.setId(1);
		mockUser.setUsername("Raul");
		User mockUser2 = new User();
		mockUser2.setId(2);
		mockUser2.setUsername("Paula");
		
		Follows follows = new Follows();
		follows.setUser_id(1);
		follows.setUser_id_followed(2);

		Mockito.when(consoleIO.readLine()).thenReturn("Raul follows Paula").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(mockUser.getId()).thenReturn(mockUser2.getId());
		Mockito.when(followsDaoImpl.obtainFollowers(Mockito.anyInt())).thenReturn(Arrays.asList(follows));

		twitter.start();
		
		Mockito.verify(consoleIO, Mockito.times(1)).printLine(mockUser.getUsername() + " is already following " + mockUser2.getUsername());

	}
	
	@Test
	public void followUsers_withNonExistingFollower() {		

		Mockito.when(consoleIO.readLine()).thenReturn("Invent follows Paula").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(0);
		
		twitter.start();
		
		Mockito.verify(consoleIO, Mockito.times(1)).printLine("This follower (Invent) does not exist");

	}
	
	@Test
	public void followUsers_withNonExistingFollowed() {		

		Mockito.when(consoleIO.readLine()).thenReturn("Raul follows Invent").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(1).thenReturn(0);
		
		twitter.start();
		
		Mockito.verify(consoleIO, Mockito.times(1)).printLine("Can't follow Invent, does not exist");

	}
	
	@Test
	public void showWall_withExistingUsers() {		

		final PrintStream standardOut = System.out;
		final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();		
		System.setOut(new PrintStream(outputStreamCaptor));

		User mockFollower = new User();
		mockFollower.setId(1);
		mockFollower.setUsername("Raul");		
		Posts postFollower = new Posts();
		postFollower.setId(1);
		postFollower.setUser_id(mockFollower.getId());
		postFollower.setText("Posts Follower");
		postFollower.setDate(LocalDateTime.now().minusMinutes(1));	
		List<Posts> listFollower = new ArrayList<>();
		listFollower.add(postFollower);

		User mockFollowed = new User();
		mockFollowed.setId(2);
		mockFollowed.setUsername("Paula");
		Posts postFollowed = new Posts();
		postFollowed.setId(2);
		postFollowed.setUser_id(mockFollowed.getId());
		postFollowed.setText("Posts Followed");
		postFollowed.setDate(LocalDateTime.now());		

		Follows follows = new Follows();
		follows.setUser_id(1);
		follows.setUser_id_followed(2);
		
		Mockito.when(consoleIO.readLine()).thenReturn("Raul wall").thenReturn("Close");
		
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(mockFollower.getId());
		
		Mockito.when(postsDaoImpl.obtainByUser(Mockito.anyInt())).thenReturn(listFollower).thenReturn(Arrays.asList(postFollowed));
		Mockito.when(followsDaoImpl.obtainFollowers(Mockito.anyInt())).thenReturn(Arrays.asList(follows));
		Mockito.when(usersDaoImpl.getNameById(Mockito.anyInt())).thenReturn(mockFollowed.getUsername()).thenReturn(mockFollower.getUsername());		
				
		twitter.start();
		
		Assertions.assertEquals("Paula -> Posts Followed (0 second ago)\r\nRaul -> Posts Follower (1 minute ago)", outputStreamCaptor.toString().trim());
		
		System.setOut(standardOut);
		
	}
	
	@Test
	public void showWall_withNonExistingUser() {
		
		Mockito.when(consoleIO.readLine()).thenReturn("InventUser wall").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(0);
		
		twitter.start();
				
		Mockito.verify(consoleIO, Mockito.times(1)).printLine("This user (InventUser) does not exist");
		
	}
	
	@Test
	public void readPosts_withNonExistingUser() {
		Mockito.when(consoleIO.readLine()).thenReturn("Raul").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(1);
		
		twitter.start();
		
		
	}
	
	@Test
	public void readPosts_withExistingUser() {
		
		final PrintStream standardOut = System.out;
		final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();		
		System.setOut(new PrintStream(outputStreamCaptor));
		
		User mockUser = new User();
		mockUser.setId(1);
		mockUser.setUsername("Raul");		
		Posts post1 = new Posts();
		post1.setId(1);
		post1.setUser_id(mockUser.getId());
		post1.setText("My first Post");
		post1.setDate(LocalDateTime.now().minusMinutes(1));
		Posts post2 = new Posts();
		post2.setId(1);
		post2.setUser_id(mockUser.getId());
		post2.setText("My second Post");
		post2.setDate(LocalDateTime.now());		
		List<Posts> listPosts = new ArrayList<>();
		listPosts.add(post1);
		listPosts.add(post2);
		
		
		Mockito.when(consoleIO.readLine()).thenReturn("InventUser").thenReturn("Close");
		Mockito.when(usersDaoImpl.getIdByName(Mockito.anyString())).thenReturn(mockUser.getId());
		
		Mockito.when(postsDaoImpl.obtainByUser(Mockito.anyInt())).thenReturn(listPosts);
		
		twitter.start();
			
		Assertions.assertEquals("My second Post (0 second ago)\r\nMy first Post (1 minute ago)", outputStreamCaptor.toString().trim());

		System.setOut(standardOut);
	}

}
