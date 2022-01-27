package com.local.imagedetection.services;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/v2")
@ApplicationScoped
@RegisterClientHeaders
@RegisterRestClient
public interface ImageDetectService
{
	@GET
	@Path("/tags")
	public Response getImageDetect(@HeaderParam("Authorization") String authorization, @QueryParam("image_url") String imageUrl);
}
