package com.example.dxkapplication;

public class Camera {

    private String description;
    private String imageURL;
    private Double[] coordinates;

    public Camera(){
    }

    public Camera(String description, String imageURL, Double[] coordinates ) {
        this.description = description;
        this.imageURL = imageURL;
        this.coordinates = coordinates;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Double[] getCoordinates(){
        return coordinates;
    };

    public void setCoordinates(Double[] coordinates){
        this.coordinates = coordinates;
    };
}