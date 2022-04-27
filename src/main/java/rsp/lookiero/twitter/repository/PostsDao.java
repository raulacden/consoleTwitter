package rsp.lookiero.twitter.repository;

import java.util.List;

import rsp.lookiero.twitter.model.Posts;

public interface PostsDao {
	
	public void insert(Posts posts);
	public void deleteById(Integer id);
	public List<Posts> obtainByUser(int userId);

}
