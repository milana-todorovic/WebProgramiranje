package images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("image")
public class ImageTestService {

	public ImageTestService() {
		// TODO Auto-generated constructor stub
	}

	@GET
	@Path("/{key}")
	@Produces({ "image/png", "image/jpg" })
	public Response getFullImage(@PathParam("key") String key) {
		String rootPath = "C:\\Users\\Lana\\Desktop\\imgs";
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(rootPath + File.separator + key));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (image != null)
			return Response.ok(image).build();
		else
			return Response.noContent().build();
	}

	@GET
	@Path("/2/{key}")
	@Produces("image/jpg")
	public Response getImage(@PathParam("key") String key) {
		String rootPath = "C:\\Users\\Lana\\Desktop\\imgs";
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(rootPath + File.separator + key));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "jpg", baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] imageData = baos.toByteArray();

		// uncomment line below to send non-streamed
		return Response.ok(imageData).build();

		// uncomment line below to send streamed
		// return Response.ok(new ByteArrayInputStream(imageData)).build();
	}

	@POST
	@Path("base64")
	@Consumes(MediaType.APPLICATION_JSON)
	public void base64upload(String image, @Context ServletContext context) {
		if (context.getAttribute("base64test") == null) {
			context.setAttribute("base64test", image);
			System.out.println("here");
		}

	}

	@GET
	@Path("base64")
	@Produces(MediaType.APPLICATION_JSON)
	public String base64download(@Context ServletContext context) {
		return (String) context.getAttribute("base64test");
	}

	/*
	 * @POST
	 * 
	 * @Path("/image")
	 * 
	 * @Consumes(MediaType.MULTIPART_FORM_DATA)
	 * 
	 * @Produces(MediaType.APPLICATION_JSON) public Response uploadImage(
	 * 
	 * @FormDataParam("file") InputStream uploadedInputStream,
	 * 
	 * @FormDataParam("file") FormDataContentDisposition fileDetails) {
	 * 
	 * System.out.println(fileDetails.getFileName());
	 * 
	 * String uploadedFileLocation = "/Users/temp/" + fileDetails.getFileName();
	 * 
	 * // save it writeToFile(uploadedInputStream, uploadedFileLocation);
	 * 
	 * String output = "File uploaded to : " + uploadedFileLocation;
	 * 
	 * return Response.ok().build(); }
	 */

	/*
	 * private void writeToFile(InputStream uploadedInputStream, String
	 * uploadedFileLocation) { try { OutputStream out = new FileOutputStream(new
	 * File(uploadedFileLocation)); int read = 0; byte[] bytes = new byte[1024];
	 * 
	 * out = new FileOutputStream(new File(uploadedFileLocation)); while ((read =
	 * uploadedInputStream.read(bytes)) != -1) { out.write(bytes, 0, read); }
	 * out.flush(); out.close(); } catch (IOException e) { e.printStackTrace(); } }
	 */

}
