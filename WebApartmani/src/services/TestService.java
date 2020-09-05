package services;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import beans.Host;
import beans.ReservationStatus;
import repository.file_repos.FileRepositoryContainer;

@Path("/test")
public class TestService {

	@GET
	@Path("/testMetoda")
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		return "test";
	}
	
	@GET
	@Path("/pathTest")
	@Produces(MediaType.APPLICATION_JSON)
	public String pathTest(@Context ServletContext context) {
		return context.getRealPath("");
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
		Apartment apartment = new Apartment();
		Host host = new Host();
		host.setName("Ime");
		host.setSurname("Prezime");
		host.getApartments().add(apartment);
		apartment.setHost(host);
		return host;
	}
	
	@POST
	@Path("/ignoreTest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Iterable<Host> ignoreTestRead(Host host, @Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		repo.getHostRepository().create(host);
		return repo.getHostRepository().getAll();	
	}

}
