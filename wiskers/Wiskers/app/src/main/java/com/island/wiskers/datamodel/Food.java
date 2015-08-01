package com.island.wiskers.datamodel;

import org.json.*;


public class Food {
	
    private String name;
    private String packageProperty;
    
    
	public Food () {
		
	}	
        
    public Food (JSONObject json) {

        if(json != null) {
            this.name = json.optString("name");
            this.packageProperty = json.optString("package");
        }
    }
    
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageProperty() {
        return this.packageProperty;
    }

    public void setPackageProperty(String packageProperty) {
        this.packageProperty = packageProperty;
    }


    
}
