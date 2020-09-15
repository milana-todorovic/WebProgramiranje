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

import auth.AuthenticatedUser;
import auth.Secured;
import beans.User;
import beans.UserRole;
import custom_exception.BadRequestException;
import dto.PasswordChangeDTO;
import dto.PersonalInformationDTO;
import service.ServiceContainer;

@Path("/profile")
public class ProfileController {

	@Context
	private ServletContext context;

	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			User entity = service.getUserService().getByID(user.getID());
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.GUEST, UserRole.HOST })
	@DELETE
	public Response delete(@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			service.getUserService().delete(user.getID());
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured
	@Path("/personal-info")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(PersonalInformationDTO info, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			User entity = service.getUserService().updatePersonalInfo(user.getID(), info);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured
	@Path("/password")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response changePassword(PasswordChangeDTO info, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			service.getUserService().changePassword(user.getID(), info.getOldPassword(), info.getNewPassword());
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
