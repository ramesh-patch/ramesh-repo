package com.local.imagedetection;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.logging.Logger;

import com.local.imagedetection.model.Image;
import com.local.imagedetection.model.ImageRequest;
import com.local.imagedetection.services.ImageService;

/**
 * Image Resource class for the Image Detect Rest Web Application.
 *
 */
@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ImageResource {
	
	private Logger LOGGER = Logger.getLogger(ImageResource.class);
	
    @Inject
    ImageService service;
    
    /**
     * Get the image details for a given image id.
     * 
     * @param imageId The id of the image to be retrieved.
     * @return Response object with the Image details.
     */
    @GET
    @Path("/{imageId}")
    @Operation(summary = "Retrieve image by imageId")
    public Response getImage(@PathParam(value = "imageId") Integer imageId)
    {
    	Image image = service.getImageById(imageId);
    	return Response.ok(image).build();
    }
    
    /**
     * Get a list of images for a a given comma separated String objects that are contained in the Images.
     * 
     * @param objects Comma separated list of String objects
     * @return Response object with list of images.
     */
    @GET
    @Path("")
    @Operation(summary = "Retrieve images by image objects")
    public Response getImageByObjects(@QueryParam("objects") String objects)
    {
    	List<Image> images = new ArrayList<>();
    	LOGGER.debug("objects: " + objects);
    	if(objects == null || objects.isEmpty()) {
    		images = service.getImages();
    		LOGGER.debug("images: " + images);
    	}
    	else {
    		images = service.getImagesByObjects(objects);
    	}
    	return Response.ok(images).build();
    }
    
    /**
     * Create the image and image objects for a given url and optional label.
     * If a label is not provided it will be created for you.
     * 
     * @param authorization The basic authorization key String value.
     * @param imageReq The image request
     * @return
     */
    @POST
    @Transactional
    @Operation(summary = "Create an Image and retrieve and persist the image objects if enableImageDetection is set to true")
    public Response create(@HeaderParam("Authorization") String authorization, ImageRequest imageReq)
    {
    	Image createdImage = service.create(authorization,imageReq);
    	return Response.ok(createdImage).status(201).build();
    }

    @PUT
    @Transactional
    @Path("/{imageId}")
    @Operation(summary = "Update an existing Image by imageId")
    public Response put(@PathParam("imageId") Integer imageId, @HeaderParam("Authorization") String authorization, ImageRequest imageReq)
    {
    	Image createdImage = service.put(authorization,imageId,imageReq);
    	return Response.ok(createdImage).status(201).build();
    }

    /**
     * Delete the image for the provided image id.
     * @param imageId The id of the image to be deleted.
     * @return Response to verify if the delete is successful.
     */
    @DELETE
    @Path("/{imageId}")
    @Transactional
    @Operation(summary = "Delete an existing Image by imageId")
    public Response delete(@PathParam(value = "imageId") Integer imageId)
    {
    	service.delete(imageId);
    	return Response.ok().build();
    }

}