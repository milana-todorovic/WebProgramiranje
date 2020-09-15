package controller;

import java.util.Collection;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import custom_exception.BadRequestException;
import service.ServiceContainer;

@Path("/holidays")
public class HolidayController {
	
	@Context ServletContext context;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAll(){
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Collection<Date> holidays = service.getHolidayService().getAll();
			return Response.ok(holidays).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(Collection<Date> newHolidays){
		ServiceContainer service = (ServiceContainer) context.getAttribute("service");
		try {
			Collection<Date> holidays = service.getHolidayService().update(newHolidays);
			return Response.ok(holidays).build();
		} catch (BadRequestException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
