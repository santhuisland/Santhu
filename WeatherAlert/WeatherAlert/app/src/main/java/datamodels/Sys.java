package datamodels;

import org.json.*;


public class Sys {
	
    private double id;
    private double message;
    private String country;
    private double type;
    private double sunset;
    private double sunrise;
    
    
	public Sys () {
		
	}	
        
    public Sys (JSONObject json) {
    
        this.id = json.optDouble("id");
        this.message = json.optDouble("message");
        this.country = json.optString("country");
        this.type = json.optDouble("type");
        this.sunset = json.optDouble("sunset");
        this.sunrise = json.optDouble("sunrise");

    }
    
    public double getId() {
        return this.id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getMessage() {
        return this.message;
    }

    public void setMessage(double message) {
        this.message = message;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getType() {
        return this.type;
    }

    public void setType(double type) {
        this.type = type;
    }

    public double getSunset() {
        return this.sunset;
    }

    public void setSunset(double sunset) {
        this.sunset = sunset;
    }

    public double getSunrise() {
        return this.sunrise;
    }

    public void setSunrise(double sunrise) {
        this.sunrise = sunrise;
    }


    
}
