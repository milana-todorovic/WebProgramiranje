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

import beans.Reservation;
import beans.ReservationStatus;
import service.ServiceContainer;

@Path("/reservations")
public class ReservationController {

	@Context
	ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll() {
		// TODO dodati parametre za pretragu
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Collection<Reservation> entities = service.getReservationService().getAll();
		return Response.ok(entities).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Reservation reservation) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Reservation entity = service.getReservationService().create(reservation);
		return Response.created(URI.create("http://localhost:8081/WebApartmani/rest/amenities/" + entity.getID()))
				.entity(entity).build();
	}
	
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByID(@PathParam("id") Integer id) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Reservation entity = service.getReservationService().getByID(id);
		return Response.ok(entity).build();
	}

	@Path("/{id}/status")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("id") Integer id, ReservationStatus status) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		Reservation reservation = service.getReservationService().changeStatus(id, status);
		return Response.ok(reservation).build();
	}

}
