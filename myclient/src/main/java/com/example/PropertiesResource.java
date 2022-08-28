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
		map.put("rawToken", jwt.getRawToken());
		return Response.ok(map).build();
	}
}