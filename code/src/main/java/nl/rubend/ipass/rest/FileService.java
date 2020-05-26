package nl.rubend.ipass.rest;

import nl.rubend.ipass.domain.Media;
import nl.rubend.ipass.domain.User;
import org.apache.tika.Tika;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;

@Path("/file")
public class FileService {
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@POST
	public Response upload(@FormDataParam("file") InputStream file,@Context SecurityContext securityContext) {
		User user= (User) securityContext.getUserPrincipal();
		return Response.ok(new Media(file,user.getId(), "/")).build();
	}
	@GET
	@Path("/{fileId}")
	public Response download(@PathParam("fileId") String fileId) {
		Media media=Media.getMedia(fileId);
		return Response.ok(media.getFile()).type(media.getMime()).build();
	}

}
