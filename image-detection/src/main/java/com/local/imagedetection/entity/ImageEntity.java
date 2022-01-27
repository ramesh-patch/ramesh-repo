package com.local.imagedetection.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="image_entity")
@NamedQuery(name = "Image.findAll", query = "SELECT ie FROM ImageEntity ie ORDER BY ie.id", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@NamedQuery(name = "Image.findbyLabel", query = "SELECT ie FROM ImageEntity ie where ie.label = :imageLabel", hints = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
@Cacheable
public class ImageEntity {

	@Id
    @SequenceGenerator(name = "imagesSequence", sequenceName = "image_entity_id_seq", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "imagesSequence")
    private Integer id;
	
	@Column(length=50,unique=true)
	private String label;
	
	@Column(length=250)
	private String url;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable (name = "ImageEntity_ImageObjectEntity", 
	joinColumns = {@JoinColumn(name="image_id")},
	inverseJoinColumns = {@JoinColumn(name="image_object_id")})
	List<ImageObjectEntity> imageObjects = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public List<ImageObjectEntity> getImageObjects() {
		return imageObjects;
	}

	public void setImageObjects(List<ImageObjectEntity> imageObjects) {
		this.imageObjects = imageObjects;
	}
	
	
	

}
