package controller;

import java.net.URI;
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

import beans.User;
import service.ServiceContainer;

@Path("/users")
public class UserController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		// TODO dodati parametre za pretragu
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Collection<User> entities = service.getUserService().getAll();
		return Response.ok(entities).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(User user) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		User entity = service.getUserService().create(user);
		return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/users/" + entity.getID()))
				.entity(entity).build();
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByID(@PathParam("id") Integer id) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		User entity = service.getUserService().getByID(id);
		return Response.ok(entity).build();
	}

	@Path("/{id}/blocked")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response blockUser(@PathParam("id") Integer id, Boolean blocked) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		service.getUserService().changeBlockStatus(id, blocked);
		return Response.noContent().build();
	}

}
