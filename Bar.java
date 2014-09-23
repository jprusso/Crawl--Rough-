/*
		Crawl
		Version Rough(ish)
	
		Created by: John Russo
	
		Alley Catz Production
		Sumemer 2014																*/


package com.alpha.crawl;

public class Bar {
    
    private String 	barName;
    private String	barStreetAddress;
    private String  barAddress;
    private String 	barReference;
    private String  barPhoto;
    private String  barImage;
    private String  barRating;
    private double 	latitude;
    private double 	longitude;
    private boolean barStatus;
    private boolean isSaved;
    
    public String getBarName() {
        return barName;
    }
    
    public void setBarName(String barName) {
        this.barName = barName;
    }
    
    public String getBarStreetAddress() {
        return barStreetAddress;
    }
    
    public void setBarStreetAddress(String barStreetAddress) {
        this.barStreetAddress = barStreetAddress ;
    }
    
    public String getBarAddress() {
        return barAddress;
    }
    
    public void setBarAddress(String barAddress) {
        this.barAddress = barAddress ;
    }
    
    public void setBarLatitude(double barLatitude) {
    	this.latitude = barLatitude;
    }
    
    public double getBarLatitude() {
        return latitude;
    }
    
    public void setBarLongitude(double barLongitude) {
    	this.longitude = barLongitude;
    }
    
    public double getBarLongitude() {
        return longitude;
    }
    
    public void setBarStatus(boolean barStatus) {
    	this.barStatus = barStatus;
    }
    
    public boolean getBarStatus() {
        return barStatus;
    }  
    
    public void setBarReference(String barReference) {
    	this.barReference = barReference;
    }
    
    public String getBarReference() {
        return barReference;
    } 
    
    public void setBarRatingImage(String barPhoto) {
    	this.barPhoto = barPhoto;
    }
    
    public String getBarRatingImage() {
        return barPhoto;
    } 
    
    public void setBarImage(String barImage) {
    	this.barImage = barImage;
    }
    
    public String getBarImage() {
        return barImage;
    } 
    
    public void setDetailSaved(boolean isSaved) {
    	this.isSaved = isSaved;
    }
    
    public boolean getDetailSaved() {
    	return isSaved;
    }
    
    public void setBarRating(String barRating){
    	this.barRating = barRating;
    }
    
    public String getBarRating() {
    	return barRating;
    }
      
}
