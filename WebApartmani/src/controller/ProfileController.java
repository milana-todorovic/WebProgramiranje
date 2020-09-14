package controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.User;
import custom_exception.BadRequestException;
import dto.PasswordChangeDTO;
import dto.PersonalInformationDTO;
import service.ServiceContainer;

@Path("/profile")
public class ProfileController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
			return Response.status(Status.UNAUTHORIZED).build();
		Integer id = user.getID();
		try {
			User entity = service.getUserService().getByID(id);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@DELETE
	public Response delete(@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
			return Response.status(Status.UNAUTHORIZED).build();
		Integer id = user.getID();
		try {
			service.getUserService().delete(id);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/personal-info")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(PersonalInformationDTO info, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
			return Response.status(Status.UNAUTHORIZED).build();
		Integer id = user.getID();
		try {
			User entity = service.getUserService().updatePersonalInfo(id, info);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/password")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changePassword(PasswordChangeDTO info, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		User user = (User) request.getSession().getAttribute("user");
		if (user == null)
			return Response.status(Status.UNAUTHORIZED).build();
		Integer id = user.getID();
		try {
			service.getUserService().changePassword(id, info.getOldPassword(), info.getNewPassword());
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
