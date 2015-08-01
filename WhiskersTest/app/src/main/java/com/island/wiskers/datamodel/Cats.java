package com.island.wiskers.datamodel;

import org.json.*;


public class Cats {
	
    private String colour;
    private double legs;
    private String size;
    private String preferedFood;
    private Image image;
    private String breed;
    private double whiskers;
    
    
	public Cats () {
		
	}	
        
    public Cats (JSONObject json) {

        if(json != null) {
            this.colour = json.optString("colour");
            this.legs = json.optDouble("legs");
            this.size = json.optString("size");
            this.preferedFood = json.optString("prefered-food");
            this.image = new Image(json.optJSONObject("image"));
            this.breed = json.optString("breed");
            this.whiskers = json.optDouble("whiskers");
        }
    }
    
    public String getColour() {
        return this.colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public double getLegs() {
        return this.legs;
    }

    public void setLegs(double legs) {
        this.legs = legs;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPreferedFood() {
        return this.preferedFood;
    }

    public void setPreferedFood(String preferedFood) {
        this.preferedFood = preferedFood;
    }

    public Image getImage() {
        return this.image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getBreed() {
        return this.breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public double getWhiskers() {
        return this.whiskers;
    }

    public void setWhiskers(double whiskers) {
        this.whiskers = whiskers;
    }


    
}
