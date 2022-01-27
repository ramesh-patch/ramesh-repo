package com.local.imagedetection.model;

public class ImageObject {
	private Integer id;
	private String name;
	private double confidence;
	
	public ImageObject() {
		
	}
	public ImageObject(String name)
	{
		this.name = name;
	}
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
	
	
}
