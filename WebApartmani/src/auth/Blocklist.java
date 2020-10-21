package auth;

import java.util.concurrent.ConcurrentHashMap;

import beans.User;
import service.UserService;

public class Blocklist {

	private ConcurrentHashMap<Integer, Integer> blockedUsers;

	public Blocklist(UserService userService) {
		this.blockedUsers = new ConcurrentHashMap<>();
		for (User user : userService.getAll()) {
			if (user.getBlocked())
				blockedUsers.put(user.getID(), user.getID());
		}
	}

	public void add(Integer userID) {
		blockedUsers.put(userID, userID);
	}

	public void remove(Integer userID) {
		if (blockedUsers.containsKey(userID))
			blockedUsers.remove(userID);
	}

	public Boolean isBlocked(Integer userID) {
		return blockedUsers.containsKey(userID);
	}

}
