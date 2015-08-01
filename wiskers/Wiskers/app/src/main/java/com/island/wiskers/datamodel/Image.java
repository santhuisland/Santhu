package com.island.wiskers.datamodel;

import org.json.*;


public class Image {
	
    private String xhdpi;
    private String xxhdpi;
    private String hdpi;
    private String mdpi;
    private String ldpi;
    
    
	public Image () {
		
	}	
        
    public Image (JSONObject json) {

        if(json != null) {
            this.xhdpi = json.optString("xhdpi");
            this.xxhdpi = json.optString("xxhdpi");
            this.hdpi = json.optString("hdpi");
            this.mdpi = json.optString("mdpi");
            this.ldpi = json.optString("ldpi");
        }
    }
    
    public String getXhdpi() {
        return this.xhdpi;
    }

    public void setXhdpi(String xhdpi) {
        this.xhdpi = xhdpi;
    }

    public String getXxhdpi() {
        return this.xxhdpi;
    }

    public void setXxhdpi(String xxhdpi) {
        this.xxhdpi = xxhdpi;
    }

    public String getHdpi() {
        return this.hdpi;
    }

    public void setHdpi(String hdpi) {
        this.hdpi = hdpi;
    }

    public String getMdpi() {
        return this.mdpi;
    }

    public void setMdpi(String mdpi) {
        this.mdpi = mdpi;
    }

    public String getLdpi() {
        return this.ldpi;
    }

    public void setLdpi(String ldpi) {
        this.ldpi = ldpi;
    }


    
}
