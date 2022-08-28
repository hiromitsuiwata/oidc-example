package com.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.jwt.JsonWebToken;

@RequestScoped
@Path("/properties")
public class PropertiesResource {

	@Inject
	private JsonWebToken jwt;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties() {
		System.out.println(jwt);

		return Response.ok(System.getProperties().toString() + jwt.toString()).build();
	}
}