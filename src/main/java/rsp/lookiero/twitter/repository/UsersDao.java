package rsp.lookiero.twitter.repository;

import java.util.List;

import rsp.lookiero.twitter.model.User;

public interface UsersDao {
	
	public List<User> obtainCurrentUsers();
	public int insert(User user);
	public String getNameById(int userId);

}
