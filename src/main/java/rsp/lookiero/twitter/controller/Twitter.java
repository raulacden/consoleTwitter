package rsp.lookiero.twitter.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import rsp.lookiero.twitter.model.Follows;
import rsp.lookiero.twitter.model.Posts;
import rsp.lookiero.twitter.model.User;
import rsp.lookiero.twitter.repository.FollowsDaoImpl;
import rsp.lookiero.twitter.repository.PostsDaoImpl;
import rsp.lookiero.twitter.repository.UsersDaoImpl;
import rsp.lookiero.twitter.utils.ConsoleIO;
import rsp.lookiero.twitter.utils.Utils;

public class Twitter {
	
	ConsoleIO console = new ConsoleIO();
	UsersDaoImpl usersDaoImpl = new UsersDaoImpl();
	PostsDaoImpl postsDaoImpl = new PostsDaoImpl();
	FollowsDaoImpl followsDaoImpl = new FollowsDaoImpl();	
	Utils utils = new Utils();
	
	public void start() {

        String inputText = console.readLine();  
        
        if("Close".equals(inputText)) {
        	console.printLine("BYE!");
        	console.closeLine();
        }else {      
	
	        //Post
	        if(inputText.contains("->")) {
	
	        	String[] splittedText = inputText.split("->");
	        	newPosts(newUser(splittedText[0].strip()),splittedText[1].strip());
	
	        //Follow	
	        } else if(inputText.contains("follows")) {
	
	        	String[] splittedText = inputText.split("follows");
	
	        	int followerId = isAlreadyUser(splittedText[0].strip());
	        	int followedId = isAlreadyUser(splittedText[1].strip());
	
	    		if(followerId == 0) {
	            	console.printLine("This follower ("+splittedText[0].strip()+") does not exist");
	        	}else if(followedId == 0) {
	            	console.printLine("Can't follow "+splittedText[1].strip()+", does not exist");
	        	}else if(isAlreadyFollowed(followerId,followedId)) {
	    			console.printLine(splittedText[0].strip() + " is already following "+splittedText[1].strip());
	    		}else {
	        		followUsers(followerId,followedId);
	        	}
	
	        //Wall
	        } else if(inputText.contains("wall")) {
	        	
	        	String[] splittedText = inputText.split("wall");
	        	
	        	int userId = isAlreadyUser(splittedText[0].strip());
	        	
	        	if(userId == 0) {
	        		console.printLine("This user ("+splittedText[0].strip()+") does not exist");
	        	}else {
	        		printWall(userId);
	        	}
	
	        //Read	
	        }else {
	
	        	int userId = isAlreadyUser(inputText.strip());
	
	        	if(userId == 0) {
	        		console.printLine(inputText.strip()+ " is not a registered user name");
	        	}else {
	        		readPosts(userId, inputText.strip());
	        	}
	        }
			start(); 
        }
	}

	/**
	 * Create new posts
	 * 
	 * @param userId
	 * @param text
	 */
	public void newPosts(int userId,String text) {
    	Posts newPost = new Posts();        	
    	newPost.setUser_id(userId);
    	newPost.setText(text);
    	
    	postsDaoImpl.insert(newPost);
    	
    	console.printLine("Saved!");
	}
	
	/**
	 * Set the followers
	 * 
	 * @param followerId
	 * @param followedId
	 */
	public void followUsers(int followerId, int followedId) {
		followsDaoImpl.insert(followerId, followedId);
		
    	console.printLine("Followed!");
	}
	
	/**
	 * Show all the posts from the user and the follows
	 * 
	 * @param userId
	 */
	public void printWall(int userId) {		

		List<Posts> listFollowerPosts = followsDaoImpl.obtainFollowers(userId)
				.stream()
				.flatMap(follower -> postsDaoImpl.obtainByUser(follower.getUser_id_followed()).stream())
				.collect(Collectors.toList());		
		List<Posts> listUserPosts = postsDaoImpl.obtainByUser(userId);		
		listUserPosts.addAll(listFollowerPosts);
		
		listUserPosts.stream().sorted(Comparator.comparing(Posts::getDate).reversed()).forEach(posts -> System.out.println(usersDaoImpl.getNameById(posts.getUser_id()) + " -> " + posts.getText() + utils.dateComparator(posts.getDate())));
		
		console.printLine("Nothing else...");
	}
	
	/**
	 * Show all the posts from the user
	 * 
	 * @param userId
	 * @param name
	 */
	public void readPosts(int userId, String name) {		
		List<Posts> listPosts = postsDaoImpl.obtainByUser(userId);
				
		listPosts.sort(Comparator.comparing(Posts::getDate).reversed());

		listPosts.forEach(posts -> System.out.println(name + " -> " + posts.getText() + utils.dateComparator(posts.getDate())));
		
		console.printLine("Nothing else...");
	}
	
	/**
	 * Create a new user if not exists
	 * 
	 * @param name
	 * @return
	 */
	public int newUser(String name) {		
		int dbUser = 0;
		dbUser = isAlreadyUser(name);
		
		if(dbUser == 0) {
			User newUser = new User();
			newUser.setUsername(name);
			dbUser = usersDaoImpl.insert(newUser);
		}
		
		return dbUser;
	}
	
	/**
	 * Check if the user already exists in the DB
	 * 
	 * @param name
	 * @return
	 */
	public int isAlreadyUser(String name){		
		List<User> listUsers = new ArrayList<>();
		int dbUserId = 0;
		
		listUsers = usersDaoImpl.obtainCurrentUsers();
		Optional<User> dbUser = listUsers.stream().filter(user -> name.equals(user.getUsername())).findFirst();
		if (dbUser.isPresent()) {
			dbUserId = dbUser.get().getId();
		}

		return dbUserId;
	}
	
	public boolean isAlreadyFollowed(int followerId, int followedId) {
		List<Follows> listFollows = followsDaoImpl.obtainFollowers(followerId);
				
		return listFollows.stream()
				  .filter(follower -> followedId == follower.getUser_id_followed())
				  .findFirst()
				  .isPresent();
		
	}

}
