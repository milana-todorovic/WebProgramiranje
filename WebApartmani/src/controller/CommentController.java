package controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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

import auth.AuthenticatedUser;
import auth.Secured;
import beans.Apartment;
import beans.ApartmentStatus;
import beans.Comment;
import beans.Guest;
import beans.UserRole;
import custom_exception.BadRequestException;
import service.ServiceContainer;

@Path("/comments")
public class CommentController {

	@Context
	private ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response get(@QueryParam("apartment") Integer apartmentID, @Context HttpServletRequest request) {
		if (apartmentID == null)
			return getAll(request);
		else
			return getByApartmentID(apartmentID, request);
	}

	private Response getAll(HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Collection<Comment> entities = null;
			if (user == null || user.getRole().equals(UserRole.GUEST))
				entities = service.getCommentService().getAllShowingInActiveApartments();
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getCommentService().getByApartmentHostID(user.getID());
			else if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getCommentService().getAll();
			else
				entities = new ArrayList<Comment>();
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	private Response getByApartmentID(Integer apartmentID, HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Apartment apartment = service.getApartmentService().getByID(apartmentID);
			if (apartment != null) {
				if (user != null && user.getRole().equals(UserRole.HOST)
						&& !user.getID().equals(apartment.getHost().getID()))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo svojim apartmanima.").build();
				else if ((user == null || user.getRole().equals(UserRole.GUEST))
						&& !apartment.getStatus().equals(ApartmentStatus.ACTIVE))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo aktivnim apartmanima.").build();
			}
			Collection<Comment> entities = null;
			if (user == null || user.getRole().equals(UserRole.GUEST))
				entities = service.getCommentService().getShowingByApartmentID(apartmentID);
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getCommentService().getByApartmentID(apartmentID);
			else if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getCommentService().getByApartmentID(apartmentID);
			else
				entities = new ArrayList<Comment>();
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.GUEST)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Comment comment, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		if (comment != null && (comment.getGuest() == null || comment.getGuest().getID() == null)) {
			Guest guest = new Guest();
			guest.setID(user.getID());
			comment.setGuest(guest);
		}
		if (comment != null && !user.getID().equals(comment.getGuest().getID()))
			return Response.status(Status.FORBIDDEN).entity("Nije dozvoljeno dodavanje komentara za drugog gosta.").build();
		try {
			Comment entity = service.getCommentService().create(comment);
			return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/comments/" + entity.getID()))
					.entity(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.GUEST)
	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Integer id, Comment comment, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Comment current = service.getCommentService().getByID(id);
			if (current != null && !user.getID().equals(current.getGuest().getID()))
				return Response.status(Status.FORBIDDEN).entity("Dozvoljena je izmena samo svojih komentara.").build();
			Comment entity = service.getCommentService().update(id, comment);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.GUEST)
	@Path("/{id}")
	@DELETE
	public Response delete(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Comment current = service.getCommentService().getByID(id);
			if (current != null && !user.getID().equals(current.getGuest().getID()))
				return Response.status(Status.FORBIDDEN).entity("Dozvoljeno je brisanje samo svojih komentara.").build();
			service.getCommentService().delete(id);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.HOST)
	@Path("/{id}/showing")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changeStatus(@PathParam("id") Integer id, Boolean showing, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Comment current = service.getCommentService().getByID(id);
			if (current != null && !user.getID().equals(current.getApartment().getHost().getID()))
				return Response.status(Status.FORBIDDEN).entity("Dozvoljena je izmena statusa samo za komentare na svojim apartmanima.").build();
			Comment entity = service.getCommentService().changeStatus(id, showing);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
