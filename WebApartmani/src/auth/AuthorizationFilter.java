package auth;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Priority;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import beans.UserRole;

@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;
	@Context
	private HttpServletRequest request;

	@Override
	public void filter(ContainerRequestContext arg0) throws IOException {
		Method resourceMethod = resourceInfo.getResourceMethod();
		List<UserRole> allowedRoles = extractRoles(resourceMethod);

		AuthenticatedUser user = (AuthenticatedUser) request.getSession().getAttribute("user");

		if (!allowedRoles.isEmpty() && !allowedRoles.contains(user.getRole()))
			arg0.abortWith(Response.status(Response.Status.FORBIDDEN).build());
	}

	private List<UserRole> extractRoles(AnnotatedElement annotatedElement) {
		if (annotatedElement == null) {
			return new ArrayList<UserRole>();
		} else {
			Secured secured = annotatedElement.getAnnotation(Secured.class);
			if (secured == null) {
				return new ArrayList<UserRole>();
			} else {
				UserRole[] allowedRoles = secured.value();
				return Arrays.asList(allowedRoles);
			}
		}
	}

}
