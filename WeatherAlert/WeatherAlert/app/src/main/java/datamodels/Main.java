package datamodels;

import org.json.*;


public class Main {
	
    private double humidity;
    private double tempMax;
    private double tempMin;
    private double temp;
    private double pressure;
    
    
	public Main () {
		
	}	
        
    public Main (JSONObject json) {
    
        this.humidity = json.optDouble("humidity");
        this.tempMax = json.optDouble("temp_max");
        this.tempMin = json.optDouble("temp_min");
        this.temp = json.optDouble("temp");
        this.pressure = json.optDouble("pressure");

    }
    
    public double getHumidity() {
        return this.humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTempMax() {
        return this.tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempMin() {
        return this.tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTemp() {
        return this.temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getPressure() {
        return this.pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }


    
}
