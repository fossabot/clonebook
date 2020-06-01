package nl.rubend.ipass.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import nl.rubend.ipass.domain.User;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {


	@Context
	private HttpServletRequest httpServletRequest;
	public static boolean isThisMyIpAddress(InetAddress addr) {
		// Check if the address is a valid special local or loop back
		if (addr.isAnyLocalAddress() || addr.isLoopbackAddress())
			return true;

		// Check if the address is defined on any interface
		try {
			return NetworkInterface.getByInetAddress(addr) != null;
		} catch (SocketException e) {
			return false;
		}
	}


	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
		boolean isLocal=isThisMyIpAddress(InetAddress.getByName(httpServletRequest.getRemoteAddr()));
		boolean secure=scheme.equals("https") || isLocal;
		MySecurityContext msc = new MySecurityContext(null,secure);
		String authHeader=requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authHeader !=null && authHeader.startsWith("Bearer ")) {
			String token=authHeader.substring("Bearer ".length()).trim();
			try {
				JwtParser parser= Jwts.parser().setSigningKey(AuthenticationResource.key);
				Claims claims=parser.parseClaimsJws(token).getBody();
				String user=claims.getSubject();
				User userObject=User.getUserById(user);
				if(userObject==null) throw new IllegalArgumentException("Niet gevonden!");
				msc=new MySecurityContext(userObject,secure);
			} catch (JwtException | IllegalArgumentException e) {
				System.out.println("Invalid JWT!");
			}
		}
		requestContext.setSecurityContext(msc);
	}
}
