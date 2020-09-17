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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import auth.AuthenticatedUser;
import auth.Secured;
import beans.Apartment;
import beans.ApartmentStatus;
import beans.Base64Image;
import beans.Host;
import beans.UserRole;
import custom_exception.BadRequestException;
import dto.ApartmentSearchDTO;
import dto.SortType;
import service.ServiceContainer;

@Path("/apartments")
public class ApartmentController {

	@Context
	private ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Collection<Apartment> entities = null;
			if (user == null)
				entities = service.getApartmentService().getActive();
			else if (user.getRole().equals(UserRole.GUEST))
				entities = service.getApartmentService().getActive();
			else if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getApartmentService().getAll();
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getApartmentService().getByHostID(user.getID());
			else
				entities = new ArrayList<Apartment>();
			return Response.ok(service.getApartmentService().getTitleImages(entities)).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.HOST)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Apartment apartment, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		if (apartment != null && (apartment.getHost() == null || apartment.getHost().getID() == null)) {
			Host host = new Host();
			host.setID(user.getID());
			apartment.setHost(host);
		}
		if (apartment != null && !user.getID().equals(apartment.getHost().getID()))
			return Response.status(Status.FORBIDDEN).entity("Nije dozvoljeno dodavanje apartmana za drugog domaćina.").build();
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
	public Response getByID(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Apartment entity = service.getApartmentService().getByID(id);
			if (entity != null) {
				if (user != null && user.getRole().equals(UserRole.HOST)
						&& !user.getID().equals(entity.getHost().getID()))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo svojim apartmanima.").build();
				else if ((user == null || user.getRole().equals(UserRole.GUEST))
						&& !entity.getStatus().equals(ApartmentStatus.ACTIVE))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo aktivnim apartmanima.").build();
			}
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.ADMIN, UserRole.HOST })
	@Path("/{id}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Integer id, Apartment apartment, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			if (user.getRole().equals(UserRole.HOST)) {
				Apartment entity = service.getApartmentService().getByID(id);
				if (entity != null && user != null && !user.getID().equals(entity.getHost().getID()))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo svojim apartmanima.").build();
			}
			Apartment entity = service.getApartmentService().update(id, apartment);
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.ADMIN, UserRole.HOST })
	@Path("/{id}")
	@DELETE
	public Response delete(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			if (user.getRole().equals(UserRole.HOST)) {
				Apartment entity = service.getApartmentService().getByID(id);
				if (entity != null && user != null && !user.getID().equals(entity.getHost().getID()))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo svojim apartmanima.").build();
			}
			service.getApartmentService().delete(id);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Path("/{id}/images")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getImages(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Apartment entity = service.getApartmentService().getByID(id);
			if (entity != null) {
				if (user != null && user.getRole().equals(UserRole.HOST)
						&& !user.getID().equals(entity.getHost().getID()))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo svojim apartmanima.").build();
				else if ((user == null || user.getRole().equals(UserRole.GUEST))
						&& !entity.getStatus().equals(ApartmentStatus.ACTIVE))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo aktivnim apartmanima.").build();
			}
			Collection<Base64Image> entities = service.getApartmentService().getImagesByApartmentID(id);
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.ADMIN, UserRole.HOST })
	@Path("/{id}/images")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addImage(@PathParam("id") Integer id, String imageData, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			if (user.getRole().equals(UserRole.HOST)) {
				Apartment entity = service.getApartmentService().getByID(id);
				if (entity != null && user != null && !user.getID().equals(entity.getHost().getID()))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo svojim apartmanima.").build();
			}
			Base64Image entity = service.getApartmentService().addImage(id, new Base64Image(imageData));
			return Response
					.created(URI.create(
							"http://localhost:8081/WebApartmani/rest/apartments/" + id + "/images/" + entity.getID()))
					.entity(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.ADMIN, UserRole.HOST })
	@Path("/{id}/images/{image-id}")
	@DELETE
	public Response deleteImage(@PathParam("id") Integer apartmentID, @PathParam("image-id") String imageID,
			@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			if (user.getRole().equals(UserRole.HOST)) {
				Apartment entity = service.getApartmentService().getByID(apartmentID);
				if (entity != null && user != null && !user.getID().equals(entity.getHost().getID()))
					return Response.status(Status.FORBIDDEN).entity("Dozvoljen je pristup samo svojim apartmanima.").build();
			}
			service.getApartmentService().deleteImage(apartmentID, imageID);
			return Response.noContent().build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(ApartmentSearchDTO searchParameters, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Collection<Apartment> entities = null;
			if (user == null)
				entities = service.getApartmentService().getActive();
			else if (user.getRole().equals(UserRole.GUEST))
				entities = service.getApartmentService().getActive();
			else if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getApartmentService().getAll();
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getApartmentService().getActive();
			else
				entities = new ArrayList<Apartment>();
			entities = service.getApartmentService().filterByAvailableDates(entities, searchParameters.getStartDate(),
					searchParameters.getEndDate());
			entities = service.getApartmentService().filterByCity(entities, searchParameters.getCity());
			entities = service.getApartmentService().filterByCountry(entities, searchParameters.getCountry());
			entities = service.getApartmentService().filterByNumberOfGuests(entities,
					searchParameters.getMinimumNumberOfGuests(), searchParameters.getMaximumNumberOfGuests());
			entities = service.getApartmentService().filterByNumberOfRooms(entities,
					searchParameters.getMinimumNumberOfRooms(), searchParameters.getMaximumNumberOfRooms());
			entities = service.getApartmentService().filterByPrice(entities, searchParameters.getMinimumPrice(),
					searchParameters.getMaximumPrice());

			entities = service.getApartmentService().filterByAmenities(entities, searchParameters.getAmenities());
			entities = service.getApartmentService().filterByType(entities, searchParameters.getTypes());
			entities = service.getApartmentService().filterByStatus(entities, searchParameters.getStatus());

			if (searchParameters.getSort() != null)
				if (searchParameters.getSort().equals(SortType.ASCENDING))
					entities = service.getApartmentService().sortByPriceAscending(entities);
				else
					entities = service.getApartmentService().sortByPriceDescending(entities);

			return Response.ok(service.getApartmentService().getTitleImages(entities)).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
