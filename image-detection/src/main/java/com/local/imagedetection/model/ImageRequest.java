package com.local.imagedetection.model;

import java.util.Objects;

public class ImageRequest {
	private String label;
	private String url;
	private boolean enableObjectDetection;
	
	public ImageRequest()
	{
		//By default object detection is not enabled.
		this.enableObjectDetection = false;
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
	
	public boolean isEnableObjectDetection() {
		return enableObjectDetection;
	}
	public void setEnableObjectDetection(boolean enableObjectDetection) {
		this.enableObjectDetection = enableObjectDetection;
	}
	@Override
	public int hashCode()
	{
		int prime = 31;
		int result =  17;
		int objectsHash = Objects.hash(label);
		result = prime * result * objectsHash;
		return result;
	}
	
	@Override 
	public boolean equals(Object other)
	{
		if(other == this)
			return true;
		if(!(other instanceof ImageRequest))
				return false;
        if (getClass() != other.getClass())
        {
            return false;
        }
		ImageRequest o = (ImageRequest) other;
		return Objects.equals(label, o.label);
	}
	
	
	
	@Override
	public String toString()
	{
		return " | Label: " + label + " | Url: " + url;
	}
	
	
}
