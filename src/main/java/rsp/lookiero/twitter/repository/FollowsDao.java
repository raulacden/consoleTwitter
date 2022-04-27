package rsp.lookiero.twitter.repository;

import java.util.List;

import rsp.lookiero.twitter.model.Follows;

public interface FollowsDao {
	public void insert(int follower,int followed);
	public List<Follows> obtainFollowers(int follower_id);
}
