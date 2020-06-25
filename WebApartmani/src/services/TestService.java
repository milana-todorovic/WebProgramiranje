package services;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import beans.Host;
import beans.ReservationStatus;

@Path("/test")
public class TestService {

	@GET
	@Path("/testMetoda")
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		return "test";
	}
	
	@GET
	@Path("/enumTest")
	@Produces(MediaType.APPLICATION_JSON)
	public ReservationStatus enumTestWrite() {
		return ReservationStatus.FINISHED;
	}
	
	@POST
	@Path("/enumTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void enumTestRead(ReservationStatus rs) {
		System.out.println(rs);
		System.out.println(rs.equals(ReservationStatus.FINISHED));
	}
	
	@GET
	@Path("/ignoreTest")
	@Produces(MediaType.APPLICATION_JSON)
	public Host ignoreTestWrite() {
		Host host = new Host();
		host.setName("Ime");
		host.setSurname("Prezime");
		host.getApartments().add(new Apartment());
		host.getApartments().add(new Apartment());
		return host;
	}
	
	@POST
	@Path("/ignoreTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void ignoreTestRead(Host host) {
		System.out.println(host.getApartments().size());
	}

}
