package controller;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dto.LoginDTO;

@Path("/auth")
public class AuthenticationController {

	@Context
	ServletContext context;

	@Path("/login")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(LoginDTO info) {
		// TODO odlučiti kako se ovo radi, JWT ili sesija
		// i koji kod ovde? created nema smisla 
		return Response.ok().build();
	}

}
