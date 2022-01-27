package com.local.imagedetection.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import com.local.imagedetection.adapter.ImageAdapter;
import com.local.imagedetection.entity.ImageEntity;
import com.local.imagedetection.entity.ImageObjectEntity;
import com.local.imagedetection.model.Image;
import com.local.imagedetection.model.ImageObject;
import com.local.imagedetection.model.ImageRequest;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
public class ImageService {

	private Logger LOGGER = Logger.getLogger(ImageService.class);

	@Inject
	private ImageAdapter imageAdapter;
	
	@Inject
	private EntityManager entityManager;
	
	@Inject
	@RestClient
	private ImageDetectService imageDetectService;
	
    /**
     * Gets all the images
     * 
     * @return List of images
     */
    public List<Image> getImages() {
    	  List<ImageEntity> imageEntities = entityManager.createNamedQuery("Image.findAll", ImageEntity.class)
                 .getResultList();
    	  return imageAdapter.convertToImageDtos(imageEntities);
    }
    
    /**
     * Returns Image for the image id.
     * 
     * @param id Id of the image
     * @return Image for the id
     */
    public Image getImageById(Integer id) {
        ImageEntity entity = entityManager.find(ImageEntity.class, id);
        if (entity == null) {
            throw new WebApplicationException("Image with id of " + id + " does not exist.", 404);
        }
        return imageAdapter.convertToImageDto(entity);
    }

    /**
     * Retrieves Images that contain the provided image objects.
     * 
     * @param objects Comma seperated list of image objects
     * @return List of Images that contain the image objects
     */
    public List<Image> getImagesByObjects(String objects) {
		List<Image> images = new ArrayList<>();
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<ImageObjectEntity> cq = cb.createQuery(ImageObjectEntity.class);
		Root<ImageObjectEntity> r = cq.from(ImageObjectEntity.class);
		try 
		{
			//Remove quotes at the start and end if they exist.
			objects = objects.replaceAll("^\"|\"$", "");
			String[] objectsArr = objects.split(",");
			LOGGER.info("objectsArr: " + objectsArr);
			for(String object: objectsArr) {
				LOGGER.info("object: " + object);
				Predicate[] predicates = new Predicate[1];
				predicates[0] = cb.equal(r.get("name"),object);
				cq.select(r).where(predicates);
				TypedQuery<ImageObjectEntity> query = entityManager.createQuery(cq);
				List<ImageObjectEntity> imageObjects = query.getResultList();
				for(ImageObjectEntity imageObject: imageObjects) {
					List<ImageEntity> imageEntities = (imageObject.getImages());
					List<Image> resImages = convertImageEntityToImage(imageEntities);
					resImages.removeAll(images);
					images.addAll(resImages);
				}
			}
		}
		catch(NoResultException nre)
		{
			LOGGER.info("No results were found for objects: " + objects);
		}
		
		return images;
	}
    
    /**
     * Creates an Image and persists the Image and if enableObjectDetection is true 
     * retrieve the Image objects from Image detection service and persist to the DB.
     * 
     * @param authorization Basic authorization required to retrieve image objects from Image Detection service
     * @param imageReq The Image request object
     * @return The Image that was created
     */
    public Image create(String authorization,ImageRequest imageReq) {
    	Image image = new Image();
        if(StringUtils.isBlank(imageReq.getLabel())) {
        	image.setLabel(generateImageLabel());
        } else {
        	image.setLabel(imageReq.getLabel());
        }
        image.setUrl(imageReq.getUrl());
        if(imageReq.isEnableObjectDetection()) {
        	List<ImageObject> imageObjects = getImageObjects(authorization,image.getUrl());
        	image.setImageObjects(imageObjects);
        }
        ImageEntity imageEntity = imageAdapter.convertToImageEntity(image);
        entityManager.persist(imageEntity);
        return getImageByLabel(image.getLabel());
    }
    
    /**
     * Update the Image based on the Image request object and if the enableObjectDetection is true and image Url has changed
     *  then retrieve the image objects from the image detection service.
     *  
     * @param authorization
     * @param id
     * @param imageReq
     * @return
     */
    public Image put(String authorization,Integer id, ImageRequest imageReq) {
    	Image image = getImageById(id);
    	//If a label is provided update it.
        if(!StringUtils.isBlank(imageReq.getLabel())) {
        	image.setLabel(imageReq.getLabel());
        }
        //Since the Url has not changed and imageObjects already exist in DB, no need to retrieve the image objects again.
        List<ImageObject> existingimageObjects = image.getImageObjects();
        LOGGER.debug("existingimageObjects: " + existingimageObjects);
        LOGGER.debug("existingimageObjects size: " + existingimageObjects.size());
        LOGGER.debug("enableObjectDetection1: " + imageReq.isEnableObjectDetection());
        if(image.getUrl().equals(imageReq.getUrl()) 
        		&& existingimageObjects != null 
        		&& existingimageObjects.size() > 0) {
        	imageReq.setEnableObjectDetection(false);
        }
        else {	
        	image.setUrl(imageReq.getUrl());
        }
        LOGGER.debug("enableObjectDetection2: " + imageReq.isEnableObjectDetection());
        if(imageReq.isEnableObjectDetection()) {
        	List<ImageObject> imageObjects = getImageObjects(authorization,image.getUrl());
        	image.setImageObjects(imageObjects);
        }
        ImageEntity imageEntity = imageAdapter.convertToImageEntity(image);
        //The id needs to be set for the imageEntity to reuse the existing Image id.
        imageEntity.setId(id);
        entityManager.merge(imageEntity);
        return getImageByLabel(image.getLabel());
    }
    
    /**
     * Delete the Image corrsponding to the image id provided.
     * 
     * @param id image id to be deleted
     */
    public void delete(Integer id) {
		ImageEntity imageEntity = entityManager.find(ImageEntity.class, id);
		if(imageEntity == null)
		{
			throw new NotFoundException();
		}
		entityManager.remove(imageEntity);
	}
    
    private Image getImageByLabel(String label) {
        ImageEntity entity = entityManager.createNamedQuery("Image.findbyLabel",ImageEntity.class)
        		.setParameter("imageLabel", label)
        		.getSingleResult();
        if (entity == null) {
            throw new WebApplicationException("Image with label of " + label + " does not exist.", 404);
        }
        Image image = imageAdapter.convertToImageDto(entity);
        return image;
    }
    
    private List<ImageObject> getImageObjects(String authorization,String url) {
    	List<ImageObject> imageObjects = new ArrayList<>();
    	Response resp = imageDetectService.getImageDetect(authorization,url);
    	if(resp == null) {
    		LOGGER.info("Nothing detected from the Image detect service for url " + url);
    		return imageObjects;
    	}
    	String respString = resp.readEntity(String.class);
    	LOGGER.debug("respString: " + respString);
    	JsonObject payload = new JsonObject(respString);
    	JsonObject result = payload.getJsonObject("result");
    	JsonArray tags = result.getJsonArray("tags");
    	LOGGER.debug("tags: " + tags);
    	for(int i=0;i<tags.size();i++) {
    		ImageObject imageObject = new ImageObject();
        	LOGGER.debug("tags.getJsonObject(i): " + tags.getJsonObject(i));
        	imageObject.setConfidence(tags.getJsonObject(i).getDouble("confidence"));
    		imageObject.setName(tags.getJsonObject(i).getJsonObject("tag").getString("en"));
    		imageObjects.add(imageObject);
    	}
		return imageObjects;
	}

	private String generateImageLabel()
    {
    	return "Image" + new Random().nextInt();
    }

	private List<Image> convertImageEntityToImage(List<ImageEntity> imageEntities) 
	{
		List<Image> images = new ArrayList<>();
		for(ImageEntity imageEntity: imageEntities) {
			images.add(getImageById(imageEntity.getId()));
		}
		return images;
	}

}
