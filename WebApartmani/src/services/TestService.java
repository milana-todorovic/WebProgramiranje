package services;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import beans.Base64Image;
import beans.Host;
import beans.Reservation;
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
		host.setID(1);
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

	@POST
	@Path("/imageTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createImage(String base64, @Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		Base64Image image = new Base64Image();
		image.setData(base64);
		repo.getImageRepository().create(image);
	}

	@GET
	@Path("/imageTest")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Base64Image getImage(String id, @Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		return repo.getImageRepository().simpleGetByID(id);
	}

	@DELETE
	@Path("/imageTest")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteImage(String id, @Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		repo.getImageRepository().deleteByID(id);
	}

}
