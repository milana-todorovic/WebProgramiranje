package controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.User;
import beans.UserRole;
import custom_exception.BadRequestException;
import service.ServiceContainer;
import service.UserService;
import util.StringValidator;

@Path("/users")
public class UserController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@QueryParam("username") String username, @QueryParam("role") String role,
			@QueryParam("gender") String gender) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Collection<User> entities = service.getUserService().getAll();
			entities = searchByUsername(service.getUserService(), entities, username);
			entities = searchByRole(service.getUserService(), entities, role);
			entities = searchByGender(service.getUserService(), entities, gender);
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	private Collection<User> searchByRole(UserService service, Collection<User> users, String role) {
		if (StringValidator.isNullOrEmpty(role))
			return users;
		if (role.equals("Domaćin"))
			return service.filterByRole(users, UserRole.HOST);
		else if (role.equals("Gost"))
			return service.filterByRole(users, UserRole.GUEST);
		else if (role.equals("Administrator"))
			return service.filterByRole(users, UserRole.ADMIN);
		else
			return new ArrayList<User>();
	}

	private Collection<User> searchByUsername(UserService service, Collection<User> users, String username) {
		if (StringValidator.isNullOrEmpty(username))
			return users;
		else
			return service.filterByUsername(users, username);
	}

	private Collection<User> searchByGender(UserService service, Collection<User> users, String gender) {
		if (StringValidator.isNullOrEmpty(gender))
			return users;
		else if (gender.equals("drugi"))
			return service.filterByGenderExclude(users, Arrays.asList("muški", "ženski"));
		else
			return service.filterByGenderInclude(users, gender);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(User user) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			User entity = service.getUserService().create(user);
			return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/users/" + entity.getID()))
					.entity(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByID(@PathParam("id") Integer id) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			User entity = service.getUserService().getByID(id);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}/blocked")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response blockUser(@PathParam("id") Integer id, Boolean blocked) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			service.getUserService().changeBlockStatus(id, blocked);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
