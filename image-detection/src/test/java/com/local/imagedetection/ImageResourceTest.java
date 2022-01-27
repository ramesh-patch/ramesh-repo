package com.local.imagedetection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import com.local.imagedetection.model.Image;
import com.local.imagedetection.model.ImageObject;
import com.local.imagedetection.model.ImageRequest;
import com.local.imagedetection.services.ImageDetectService;
import com.local.imagedetection.services.ImageService;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

@QuarkusTest
@TestHTTPEndpoint(ImageResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ImageResourceTest {

	Logger LOGGER = Logger.getLogger(ImageService.class);
	
	@InjectMock
	@RestClient 
	ImageDetectService imageDetectClient;
	
   
	
	/**
	 * Retrieve the image with imageId 1.
	 */
	@Test
	@Order(1)
    public void testGetImage() {
		Image[] images = createImageResponse();
		final Response response =  given()
        		.pathParam("imageId", 1)
                .when().get("/{imageId}")
                .then()
                .statusCode(200)
                .extract().response();
		LOGGER.info("Returned body: " + response.getBody().asPrettyString());
		Image newImage = response.getBody().as(Image.class);
		assertThat(images[0].getLabel(),equalTo(newImage.getLabel()));
		assertThat(images[0].getUrl(),equalTo(newImage.getUrl()));
    }

    /**
     * Retrieves the two images that matches the object dog.
     */
    @Test
    @Order(2)
    public void testGetImageByObjects() {
    	Image[] images = createImageResponse();
        String objects = "dog";
        final Response response = given()
                .queryParam("objects", objects)
                .when().get("")
                .then()
                .statusCode(200)
                .extract().response();
        LOGGER.info("Returned body: " + response.getBody().asPrettyString());
		final Image[] imageResp = response.getBody().as(Image[].class);
		if(imageResp != null && imageResp.length > 0) 
		{
			for(int i=0;i<imageResp.length;i++) {
				assertThat(images[i].getLabel(),equalTo(imageResp[i].getLabel()));
				assertThat(images[i].getUrl(),equalTo(imageResp[i].getUrl()));
			}
		}
    }

   
    
    /**
     * Update the image with imageId 2.
     */
    @Test
    @Order(3)
    public void testUpdateImage() {
    	ImageRequest imageReq = createImageRequest();
    	final Response response = given()
							    	.pathParam("imageId", 2)
							    	.contentType(ContentType.JSON).body(imageReq)
							        .when().put("/{imageId}")
							        .then()
							        .statusCode(201)
							        .extract().response();
    	LOGGER.info("Returned body: " + response.getBody().asPrettyString());
		Image imageRes = response.getBody().as(Image.class);
		assertThat(imageReq.getLabel(),equalTo(imageRes.getLabel()));
		assertThat(imageReq.getUrl(),equalTo(imageRes.getUrl()));
    }
    
    /**
     * Delete the image with imageId 2.
     */
    @Test
    @Order(4)
    public void testDeleteImage() {
    	given().pathParam("imageId", 2)
        .when().delete("/{imageId}")
        .then()
        .statusCode(200)
        .extract().response();
    	
    }
    
    /**
     * Get all Images retrieves the image with imageId 1. The Image with imageId 2 was deleted in earlier test.
     */
    @Test
    @Order(5)
    public void testGetImages() {
    	Image image = createFirstImageResponse();
    	final Response response = given()
                .when().get("")
                .then()
                .statusCode(200)
                .extract().response();
        LOGGER.info("Returned body: " + response.getBody().asPrettyString());
		final Image[] imageResp = response.getBody().as(Image[].class);
		if(imageResp != null && imageResp.length > 0) 
		{
				assertThat(image.getLabel(),equalTo(imageResp[0].getLabel()));
				assertThat(image.getUrl(),equalTo(imageResp[0].getUrl()));
		}
    }
    
    /**
     * Delete the image with imageId 1.
     */
    @Test
    @Order(5)
    public void testDeleteOtherImage() {
    	given().pathParam("imageId", 1)
        .when().delete("/{imageId}")
        .then()
        .statusCode(200)
        .extract().response();
    	
    }
    private ImageRequest createImageRequest() {
		ImageRequest image = new ImageRequest();
		image.setLabel("Image3");
		image.setUrl("url3");
		return image;
	}
    
    /**Creates two images to verify with the images retrieved.
     * 
     * @return array of Images
     */
    private Image[] createImageResponse() {
    	Image[] images = new Image[2];
    	images[0] = createFirstImageResponse();
		images[1] = createSecondImageResponse();
		return images;
    }
    
    /** Creates the Image with imageId 1 to verify.
     * @return Image
     */
    private Image createFirstImageResponse() {
    	Image image = new Image();
    	image.setId(1);
    	image.setLabel("Image1");
    	image.setUrl("url1");
		List<ImageObject> imageObjects1 = new ArrayList<>();
		ImageObject imageObject11 = new ImageObject("dog");
		imageObjects1.add(imageObject11);
		ImageObject imageObject12 = new ImageObject("cat");
		imageObjects1.add(imageObject12);
		image.setImageObjects(imageObjects1);
		return image;
    }
    
    /** Creates the Image with imageId 2 to verify.
     * @return Image
     */
    private Image createSecondImageResponse() {
    	Image image = new Image();
    	image.setId(2);
    	image.setLabel("Image2");
    	image.setUrl("url2");
		List<ImageObject> imageObjects1 = new ArrayList<>();
		ImageObject imageObject11 = new ImageObject("dog");
		imageObjects1.add(imageObject11);
		ImageObject imageObject12 = new ImageObject("cow");
		imageObjects1.add(imageObject12);
		image.setImageObjects(imageObjects1);
		return image;
    }
    
//    private Image createUpdatedImageResponse() {
//    	Image image = new Image();
//    	image.setId(2);
//    	image.setLabel("Image3");
//    	image.setUrl("url3");
//		List<ImageObject> imageObjects1 = new ArrayList<>();
//		ImageObject imageObject11 = new ImageObject("dog");
//		imageObjects1.add(imageObject11);
//		ImageObject imageObject12 = new ImageObject("cow");
//		imageObjects1.add(imageObject12);
//		image.setImageObjects(imageObjects1);
//		return image;
//    }
}
