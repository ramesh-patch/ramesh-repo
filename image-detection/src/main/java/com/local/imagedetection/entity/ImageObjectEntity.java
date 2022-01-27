package com.local.imagedetection.entity;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="image_object_entity")
public class ImageObjectEntity {
	@Id
    @SequenceGenerator(name = "imageObjectsSequence", sequenceName = "image_object_entity_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "imageObjectssSequence")
    private Integer id;
	
	@Column(length=50)
	private String name;
	
	@Column
	private double confidence;
	
	@ManyToMany(mappedBy="imageObjects")
	List<ImageEntity> images = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public double getConfidence() {
		return confidence;
	}

	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}

	public List<ImageEntity> getImages() {
		return images;
	}

	public void setImages(List<ImageEntity> images) {
		this.images = images;
	}
	
	
}
