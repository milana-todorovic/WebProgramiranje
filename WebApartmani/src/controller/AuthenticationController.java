package controller;

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

import beans.User;
import custom_exception.BadRequestException;
import dto.LoginDTO;
import service.ServiceContainer;

@Path("/auth")
public class AuthenticationController {

	@Context
	ServletContext context;

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginDTO info, @Context HttpServletRequest request) {
		// TODO stvarno implementiraj logovanje
		// za sad je samo da bi se testirao /profile resurs
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			User user = service.getUserService().getByUsernameAndPassword(info.getUsername(), info.getPassword());
			if (user == null || user.getBlocked())
				return Response.status(Status.UNAUTHORIZED).build();
			request.getSession().setAttribute("user", user);
			return Response.ok().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@Path("/logout")
	@POST
	public Response logout(@Context HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		return Response.ok().build();
	}

}
