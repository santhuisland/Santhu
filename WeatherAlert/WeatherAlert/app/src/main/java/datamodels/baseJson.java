package datamodels;

import org.json.*;
import java.util.ArrayList;

public class baseJson {
	
    private Wind wind;
    private String base;
    private Clouds clouds;
    private Coord coord;
    private double id;
    private double dt;
    private double cod;
    private ArrayList<Weather> weather;
    private Main main;
    private Sys sys;
    private String name;
    
    
	public baseJson () {
		
	}	
        
    public baseJson (JSONObject json) {
    
        this.wind = new Wind(json.optJSONObject("wind"));
        this.base = json.optString("base");
        this.clouds = new Clouds(json.optJSONObject("clouds"));
        this.coord = new Coord(json.optJSONObject("coord"));
        this.id = json.optDouble("id");
        this.dt = json.optDouble("dt");
        this.cod = json.optDouble("cod");

        this.weather = new ArrayList<Weather>();
        JSONArray arrayWeather = json.optJSONArray("weather");
        if (null != arrayWeather) {
            int weatherLength = arrayWeather.length();
            for (int i = 0; i < weatherLength; i++) {
                JSONObject item = arrayWeather.optJSONObject(i);
                if (null != item) {
                    this.weather.add(new Weather(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("weather");
            if (null != item) {
                this.weather.add(new Weather(item));
            }
        }

        this.main = new Main(json.optJSONObject("main"));
        this.sys = new Sys(json.optJSONObject("sys"));
        this.name = json.optString("name");

    }
    
    public Wind getWind() {
        return this.wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public String getBase() {
        return this.base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Clouds getClouds() {
        return this.clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Coord getCoord() {
        return this.coord;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public double getId() {
        return this.id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public double getDt() {
        return this.dt;
    }

    public void setDt(double dt) {
        this.dt = dt;
    }

    public double getCod() {
        return this.cod;
    }

    public void setCod(double cod) {
        this.cod = cod;
    }

    public ArrayList<Weather> getWeather() {
        return this.weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return this.main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Sys getSys() {
        return this.sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    
}
