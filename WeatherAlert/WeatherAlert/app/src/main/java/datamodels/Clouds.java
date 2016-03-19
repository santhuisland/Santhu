package datamodels;

import org.json.*;


public class Clouds {
	
    private double all;
    
    
	public Clouds () {
		
	}	
        
    public Clouds (JSONObject json) {
    
        this.all = json.optDouble("all");

    }
    
    public double getAll() {
        return this.all;
    }

    public void setAll(double all) {
        this.all = all;
    }


    
}
