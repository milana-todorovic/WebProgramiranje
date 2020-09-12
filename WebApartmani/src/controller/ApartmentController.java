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
import javax.ws.rs.core.Response.Status;

import beans.Apartment;
import beans.Base64Image;
import custom_exception.BadRequestException;
import service.ServiceContainer;

@Path("/apartments")
public class ApartmentController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		// TODO dodati parametre za pretragu
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Collection<Apartment> entities = service.getApartmentService().getAll();
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Apartment apartment) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Apartment entity = service.getApartmentService().create(apartment);
			return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/apartments/" + entity.getID()))
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
			Apartment entity = service.getApartmentService().getByID(id);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Integer id, Apartment apartment) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Apartment entity = service.getApartmentService().update(id, apartment);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}")
	@DELETE
	public Response delete(@PathParam("id") Integer id) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			service.getApartmentService().delete(id);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}/images")
	@GET
	public Response getImages(@PathParam("id") Integer id) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Collection<Base64Image> entities = service.getApartmentService().getImagesByApartmentID(id);
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}/images")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addImage(@PathParam("id") Integer id, String imageData) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Base64Image entity = service.getApartmentService().addImage(id, new Base64Image(imageData));
			return Response
					.created(URI.create(
							"http://localhost:8081/WebApartmani/rest/apartments/" + id + "/images/" + entity.getID()))
					.entity(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}/images/{image-id}")
	@DELETE
	public Response deleteImage(@PathParam("id") Integer apartmentID, @PathParam("image-id") String imageID) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			service.getApartmentService().deleteImage(apartmentID, imageID);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
