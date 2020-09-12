package controller;

import java.net.URI;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import beans.Comment;
import service.ServiceContainer;

@Path("/comments")
public class CommentController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		// TODO dodati parametre za pretragu
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Collection<Comment> entities = service.getCommentService().getAll();
		return Response.ok(entities).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Comment comment) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Comment entity = service.getCommentService().create(comment);
		return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/amenities/" + entity.getID()))
				.entity(entity).build();
	}

	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByID(@PathParam("id") Integer id) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Comment entity = service.getCommentService().getByID(id);
		return Response.ok(entity).build();
	}

	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Integer id, Comment comment) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Comment entity = service.getCommentService().update(id, comment);
		return Response.ok(entity).build();
	}

	@Path("/{id}")
	@DELETE
	public Response delete(@PathParam("id") Integer id) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		service.getCommentService().delete(id);
		return Response.noContent().build();
	}

}