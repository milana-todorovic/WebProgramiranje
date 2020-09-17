package controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import auth.AuthenticatedUser;
import auth.Blocklist;
import auth.Secured;
import beans.User;
import beans.UserRole;
import custom_exception.BadRequestException;
import dto.UserSearchDTO;
import service.ServiceContainer;

@Path("/users")
public class UserController {

	@Context
	ServletContext context;

	@Secured({ UserRole.HOST, UserRole.ADMIN })
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Collection<User> entities = null;
			if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getUserService().getAll();
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getUserService().getGuestsByHostID(user.getID());
			else
				entities = new ArrayList<User>();
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.ADMIN)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(User user) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		if (user != null && user.getRole() != null && !user.getRole().equals(UserRole.HOST))
			return Response.status(Status.FORBIDDEN).build();
		try {
			User entity = service.getUserService().create(user);
			return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/users/" + entity.getID()))
					.entity(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.HOST, UserRole.ADMIN })
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByID(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			User entity = null;
			if (user.getRole().equals(UserRole.HOST))
				entity = service.getUserService().getByIDforHost(id, user.getID());
			else if (user.getRole().equals(UserRole.ADMIN))
				entity = service.getUserService().getByID(id);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.ADMIN)
	@Path("/{id}/blocked")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response blockUser(@PathParam("id") Integer id, Boolean blocked) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Blocklist blocklist = (Blocklist) context.getAttribute("blocklist");
		try {
			service.getUserService().changeBlockStatus(id, blocked);
			if (blocked)
				blocklist.add(id);
			else
				blocklist.remove(id);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.HOST, UserRole.ADMIN })
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(UserSearchDTO searchParameters, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Collection<User> entities = null;
			if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getUserService().getAll();
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getUserService().getGuestsByHostID(user.getID());
			else
				entities = new ArrayList<User>();

			entities = service.getUserService().filterByRole(entities, searchParameters.getRole());
			entities = service.getUserService().filterByUsername(entities, searchParameters.getUsername());
			if (searchParameters.getGender() != null && searchParameters.getGender().equals("drugi"))
				entities = service.getUserService().filterByGenderExclude(entities, Arrays.asList("mu\u0161ki", "\u017Eenski"));
			else
				entities = service.getUserService().filterByGenderInclude(entities, searchParameters.getGender());
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
