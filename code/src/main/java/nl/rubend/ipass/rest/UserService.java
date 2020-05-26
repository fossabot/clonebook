package nl.rubend.ipass.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import nl.rubend.ipass.domain.*;
import nl.rubend.ipass.domain.NotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.io.StringReader;
import java.util.AbstractMap;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newPasswordMail(@FormParam("email") String email) {
		User user;
		try {
			user=User.getUserByEmail(email);
		} catch (NotFoundException e) {
			user=new User(email);
		}
		user.sendPasswordForgottenUrl();
		return Response.ok(new HashMap<String,String>()).build();
	}
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Path("/newPassword")
	public Response use(@FormParam("code") String code, @FormParam("password") String password) {
		User user=NewPassword.use(code);
		user.setPassword(password);
		return Response.ok().build();
	}
	@POST
	@Path("/settings")
	@RolesAllowed("user")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response setProfile(@FormParam("name") String name, @FormParam("email") String email, @Context SecurityContext securityContext) {
		User user = (User) securityContext.getUserPrincipal();
		if (!user.getEmail().equals(email)) user.setEmail(email);
		if (!user.getName().equals(name)) user.setName(name);
		return Response.ok().build();
	}
	@GET
	@RolesAllowed("user")
	@Path("/{userId}")
	public Response publicUserProfile(@Context SecurityContext securityContext, @PathParam("userId") String userId) {
		User user= (User) securityContext.getUserPrincipal();
		User requested=User.getUserById(userId);
		if(requested.getPrivatePage().isLid(user)) return Response.ok(requested).build();
		else throw new ForbiddenException("Niet eigen profiel");
	}
}
