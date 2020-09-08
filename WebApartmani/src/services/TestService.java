package services;

import java.util.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Apartment;
import beans.Base64Image;
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
	@Path("/imageTest/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Base64Image getImage(@PathParam("id") String id, @Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		return repo.getImageRepository().simpleGetByID(id);
	}

	@DELETE
	@Path("/imageTest/{id}")
	public void deleteImage(@PathParam("id") String id, @Context ServletContext context) {
		FileRepositoryContainer repo = (FileRepositoryContainer) context.getAttribute("repo");
		repo.getImageRepository().deleteByID(id);
	}
	
	@GET
	@Path("/timeTest")
	@Produces(MediaType.APPLICATION_JSON)
	public Apartment getTime() {
		Apartment a = new Apartment();
		a.setCheckInTime(new Date(14*60*60*1000));
		return a;
	}
	
	@POST
	@Path("/timeTest")
	public void postTime(Apartment a) {
		System.out.println(a.getCheckInTime());
	}

}
