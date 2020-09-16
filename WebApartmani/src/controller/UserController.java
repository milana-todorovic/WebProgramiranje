package controller;

import java.net.URI;
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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import beans.User;
import custom_exception.BadRequestException;
import dto.UserSearchDTO;
import service.ServiceContainer;

@Path("/users")
public class UserController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Collection<User> entities = service.getUserService().getAll();
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
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

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(UserSearchDTO searchParameters) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Collection<User> entities = service.getUserService().getAll();
			entities = service.getUserService().filterByRole(entities, searchParameters.getRole());
			entities = service.getUserService().filterByUsername(entities, searchParameters.getUsername());
			if (searchParameters.getGender() != null && searchParameters.getGender().equals("drugi"))
				entities = service.getUserService().filterByGenderExclude(entities, Arrays.asList("muški", "ženski"));
			else
				entities = service.getUserService().filterByGenderInclude(entities, searchParameters.getGender());
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
