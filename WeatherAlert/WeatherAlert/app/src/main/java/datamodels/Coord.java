package datamodels;

import org.json.*;


public class Coord {
	
    private double lon;
    private double lat;
    
    
	public Coord () {
		
	}	
        
    public Coord (JSONObject json) {
    
        this.lon = json.optDouble("lon");
        this.lat = json.optDouble("lat");

    }
    
    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }


    
}
