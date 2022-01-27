package com.local.imagedetection.adapter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import com.local.imagedetection.entity.ImageEntity;
import com.local.imagedetection.entity.ImageObjectEntity;
import com.local.imagedetection.model.Image;
import com.local.imagedetection.model.ImageObject;

@ApplicationScoped
public class ImageAdapter {
	public ImageEntity convertToImageEntity(Image image)
	{
		ImageEntity imageEntity = new ImageEntity();
		imageEntity.setLabel(image.getLabel());
		imageEntity.setUrl(image.getUrl());
		imageEntity.setImageObjects(convertToImageObjectEntities(image.getImageObjects()));
		return imageEntity;
	}
	
	public Image convertToImageDto(ImageEntity imageEntity)
	{
		Image image = new Image();
		image.setId(imageEntity.getId());
		image.setLabel(imageEntity.getLabel());
		image.setUrl(imageEntity.getUrl());
		image.setImageObjects(convertToImageObjectDtos(imageEntity.getImageObjects()));
		return image;
	}
	
	public List<Image> convertToImageDtos(List<ImageEntity> imageEntityList)
	{
		List<Image> imageList = new ArrayList<>();
		/*
		 * If imageEntityList is null return empty imageList
		 */
		if(imageEntityList == null)
			return imageList;
		
		for(ImageEntity imageEntity: imageEntityList) {
			Image image = convertToImageDto(imageEntity);
			imageList.add(image);
		}
		return imageList;
	}
	private List<ImageObject> convertToImageObjectDtos(List<ImageObjectEntity> imageObjectEntitites) {
		List<ImageObject> imageObjects = new ArrayList<>();
		for(ImageObjectEntity imageObjectEntity: imageObjectEntitites) {
			ImageObject imageObject = new ImageObject(imageObjectEntity.getName());
			imageObject.setId(imageObjectEntity.getId());
			imageObject.setConfidence(imageObjectEntity.getConfidence());
			imageObjects.add(imageObject);
		}
		return imageObjects;
	}

	private List<ImageObjectEntity> convertToImageObjectEntities(List<ImageObject> imageObjects)
	{
		List<ImageObjectEntity> imageObjectEntities = new ArrayList<>();
		for(ImageObject imageObj: imageObjects)
		{
			ImageObjectEntity imageObjectEntity = new ImageObjectEntity();
			imageObjectEntity.setName(imageObj.getName());
			imageObjectEntity.setConfidence(imageObj.getConfidence());
			imageObjectEntities.add(imageObjectEntity);
		}
		return imageObjectEntities;
	}
	
}
