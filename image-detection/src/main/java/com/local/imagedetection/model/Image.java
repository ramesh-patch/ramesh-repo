package com.local.imagedetection.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Image {
	private Integer id;
	private String label;
	private String url;
	private List<ImageObject> imageObjects = new ArrayList<>();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ImageObject> getImageObjects() {
		return imageObjects;
	}
	public void setImageObjects(List<ImageObject> imageObjects) {
		this.imageObjects = imageObjects;
	}
	
	@Override
	public int hashCode()
	{
		int prime = 31;
		int result =  17;
		int objectsHash = Objects.hash(id,label);
		result = prime * result * objectsHash;
		return result;
	}
	
	@Override 
	public boolean equals(Object other)
	{
		if(other == this)
			return true;
		if(!(other instanceof Image))
				return false;
        if (getClass() != other.getClass())
        {
            return false;
        }
		Image o = (Image) other;
		return 	Objects.equals(id, o.id)
			    && Objects.equals(label, o.label);
	}
	
	
	
	@Override
	public String toString()
	{
		return "Id: " + id + " | Label: " + label + " | Url: " + url;
	}
	
	
}
