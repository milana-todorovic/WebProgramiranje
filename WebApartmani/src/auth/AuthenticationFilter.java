package auth;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ServletContext context;
	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext arg0) throws IOException {
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		Blocklist blocklist = (Blocklist) context.getAttribute("user");

		if (user == null || blocklist.isBlocked(user.getID()))
			arg0.abortWith(Response.status(Status.UNAUTHORIZED).build());
	}

}
