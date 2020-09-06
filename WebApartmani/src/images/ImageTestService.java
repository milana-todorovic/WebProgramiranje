package images;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
	    //return Response.ok(new ByteArrayInputStream(imageData)).build();
	}

}
