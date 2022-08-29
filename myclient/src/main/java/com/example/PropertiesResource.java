package com.example;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;

import org.eclipse.microprofile.jwt.JsonWebToken;

import com.google.common.collect.Maps;

@RequestScoped
@Path("/properties")
public class PropertiesResource {

	@Inject
	private JsonWebToken jwt;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getProperties() {
		HashMap<String, String> map = Maps.newHashMap(Maps.fromProperties(System.getProperties()));
		System.out.println(jwt.getRawToken());
		System.out.println(jwt.getName());
		// System.out.println(jwt.getExpirationTime());
		System.out.println(jwt.getIssuer());
		System.out.println(jwt.getSubject());
		System.out.println(jwt.getClaimNames());
		System.out.println(jwt.getGroups());
		System.out.println(jwt.getTokenID());

		map.put("rawToken", jwt.getRawToken());
		return Response.ok(map).build();
	}
}