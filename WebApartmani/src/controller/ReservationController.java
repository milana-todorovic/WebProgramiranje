package controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
import auth.Secured;
import beans.Guest;
import beans.Reservation;
import beans.ReservationStatus;
import beans.UserRole;
import custom_exception.BadRequestException;
import dto.ReservationSearchDTO;
import dto.SortType;
import service.ServiceContainer;

@Path("/reservations")
public class ReservationController {

	private static final List<ReservationStatus> guestAllowed = Arrays.asList(ReservationStatus.CANCELLED);
	private static final List<ReservationStatus> hostAllowed = Arrays.asList(ReservationStatus.ACCEPTED,
			ReservationStatus.FINISHED, ReservationStatus.REJECTED);

	@Context
	ServletContext context;

	@Secured
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Collection<Reservation> entities = null;
			if (user.getRole().equals(UserRole.GUEST))
				entities = service.getReservationService().getByGuestID(user.getID());
			else if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getReservationService().getAll();
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getReservationService().getByApartmentHostID(user.getID());
			else
				entities = new ArrayList<Reservation>();
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured(UserRole.GUEST)
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Reservation reservation, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		if (reservation != null && reservation.getGuest() == null) {
			Guest guest = new Guest();
			guest.setID(user.getID());
			reservation.setGuest(guest);
		}
		if (reservation != null && !user.getID().equals(reservation.getGuest().getID()))
			return Response.status(Status.FORBIDDEN).build();
		try {
			Reservation entity = service.getReservationService().create(reservation);
			return Response
					.created(URI.create("http://localhost:8081/WebApartmani/rest/reservations/" + entity.getID()))
					.entity(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getByID(@PathParam("id") Integer id, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Reservation entity = service.getReservationService().getByID(id);
			if (entity != null) {
				if (user.getRole().equals(UserRole.GUEST) && !entity.getGuest().getID().equals(user.getID()))
					return Response.status(Status.FORBIDDEN).build();
				else if (user.getRole().equals(UserRole.HOST)
						&& !user.getID().equals(entity.getApartment().getHost().getID()))
					return Response.status(Status.FORBIDDEN).build();
			}
			return Response.ok(entity).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured({ UserRole.HOST, UserRole.GUEST })
	@Path("/{id}/status")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(@PathParam("id") Integer id, ReservationStatus status,
			@Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		if (user.getRole().equals(UserRole.GUEST) && !guestAllowed.contains(status))
			return Response.status(Status.FORBIDDEN).build();
		else if (user.getRole().equals(UserRole.HOST) && !hostAllowed.contains(status))
			return Response.status(Status.FORBIDDEN).build();
		try {
			Reservation entity = service.getReservationService().getByID(id);
			if (entity != null) {
				if (user.getRole().equals(UserRole.GUEST) && !entity.getGuest().getID().equals(user.getID()))
					return Response.status(Status.FORBIDDEN).build();
				else if (user.getRole().equals(UserRole.HOST)
						&& !user.getID().equals(entity.getApartment().getHost().getID()))
					return Response.status(Status.FORBIDDEN).build();
			}
			Reservation reservation = service.getReservationService().changeStatus(id, status);
			return Response.ok(reservation).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@Secured
	@POST
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response search(ReservationSearchDTO searchParameters, @Context HttpServletRequest request) {
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");
		try {
			Collection<Reservation> entities = null;
			if (user.getRole().equals(UserRole.GUEST))
				entities = service.getReservationService().getByGuestID(user.getID());
			else if (user.getRole().equals(UserRole.ADMIN))
				entities = service.getReservationService().getAll();
			else if (user.getRole().equals(UserRole.HOST))
				entities = service.getReservationService().getByApartmentHostID(user.getID());
			else
				entities = new ArrayList<Reservation>();

			entities = service.getReservationService().filterByGuestUsername(entities,
					searchParameters.getGuestUsername());
			entities = service.getReservationService().filterByStatus(entities, searchParameters.getStatus());
			if (searchParameters.getSort() != null)
				if (searchParameters.getSort().equals(SortType.ASCENDING))
					entities = service.getReservationService().sortByPriceAscending(entities);
				else
					entities = service.getReservationService().sortByPriceDescending(entities);
			return Response.ok(entities).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
