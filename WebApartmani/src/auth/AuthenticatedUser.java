package auth;

import beans.UserRole;

public class AuthenticatedUser {

	private Integer id;
	private UserRole role;

	public AuthenticatedUser() {
		super();
	}

	public AuthenticatedUser(Integer id, UserRole role) {
		super();
		this.id = id;
		this.role = role;
	}

	public Integer getID() {
		return id;
	}

	public void setID(Integer id) {
		this.id = id;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

}
