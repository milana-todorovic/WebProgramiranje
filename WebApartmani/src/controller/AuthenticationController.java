package controller;

import java.net.URI;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import auth.AuthenticatedUser;
import beans.User;
import custom_exception.BadRequestException;
import dto.LoginDTO;
import service.ServiceContainer;

@Path("/auth")
public class AuthenticationController {

	@Context
	private ServletContext context;

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginDTO info, @Context HttpServletRequest request) {
		// TODO preci na JWT ili ne?
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			User user = service.getUserService().getByUsernameAndPassword(info.getUsername(), info.getPassword());
			if (user != null && user.getBlocked())
				return Response.status(Status.FORBIDDEN).entity("Blokrani ste od strane administratora.").build();
			if (user == null)
				return Response.status(Status.UNAUTHORIZED).entity("Pogre\u0161no korisničko ime ili lozinka.").build();
			request.getSession().setAttribute("user", new AuthenticatedUser(user.getID(), user.getRole()));
			return Response.ok().build();
		} catch (BadRequestException e) {
			return Response.status(Status.UNAUTHORIZED).entity(e.getMessage()).build();
		}
	}

	@Path("/logout")
	@POST
	public Response logout(@Context HttpServletRequest request) {
		if (request.getSession().getAttribute("user") != null)
			request.getSession().removeAttribute("user");
		return Response.ok().build();
	}

	@Path("/register")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response register(User user) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			User entity = service.getUserService().create(user);
			return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/users/" + entity.getID()))
					.entity(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
