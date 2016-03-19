package datamodels;

import org.json.*;


public class Weather {
	
    private double id;
    private String main;
    private String icon;
    private String description;
    
    
	public Weather () {
		
	}	
        
    public Weather (JSONObject json) {
    
        this.id = json.optDouble("id");
        this.main = json.optString("main");
        this.icon = json.optString("icon");
        this.description = json.optString("description");

    }
    
    public double getId() {
        return this.id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public String getMain() {
        return this.main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    
}
