package datamodels;

import org.json.*;


public class Wind {
	
    private double speed;
    private double deg;
    
    
	public Wind () {
		
	}	
        
    public Wind (JSONObject json) {
    
        this.speed = json.optDouble("speed",0);
        this.deg = json.optDouble("deg",0);

    }
    
    public double getSpeed() {
        return this.speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return this.deg;
    }

    public void setDeg(double deg) {
        this.deg = deg;
    }


    
}
