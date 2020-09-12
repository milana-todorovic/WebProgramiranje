package controller;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.User;
import dto.PasswordChangeDTO;
import service.ServiceContainer;

@Path("/profile")
public class ProfileController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByID() {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Integer id = 0; // id ulogovanog korisnika
		User entity = service.getUserService().getByID(id);
		return Response.ok(entity).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(User user) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Integer id = 0; // id ulogovanog korisnika
		User entity = service.getUserService().update(id, user);
		return Response.ok(entity).build();
	}

	@DELETE
	public Response delete() {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Integer id = 0; // id ulogovanog korisnika
		service.getUserService().delete(id);
		return Response.noContent().build();
	}

	@Path("/password")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changePassword(PasswordChangeDTO info) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Integer id = 0; // id ulogovanog korisnika
		service.getUserService().changePassword(id, info.getOldPassword(), info.getNewPassword());
		return Response.noContent().build();
	}

}
