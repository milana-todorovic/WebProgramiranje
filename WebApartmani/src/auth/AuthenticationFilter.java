package auth;

import java.io.IOException;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.Provider;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext arg0) throws IOException {
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");		

		if (user == null)
			arg0.abortWith(Response.status(Status.UNAUTHORIZED)
					.entity("Morate biti ulogovani kako bi pristupili traženom resursu.").build());
	}

}
