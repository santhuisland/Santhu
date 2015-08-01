package com.island.wiskers.datamodel;

import org.json.*;
import java.util.ArrayList;

public class bw {
	
    private ArrayList<Cats> cats;
    private ArrayList<Food> food;
    
    
	public bw () {
		
	}	
        
    public bw (JSONObject json) {
    
        if(json != null) {
            this.cats = new ArrayList<Cats>();
            JSONArray arrayCats = json.optJSONArray("cats");
            if (null != arrayCats) {
                int catsLength = arrayCats.length();
                for (int i = 0; i < catsLength; i++) {
                    JSONObject item = arrayCats.optJSONObject(i);
                    if (null != item) {
                        this.cats.add(new Cats(item));
                    }
                }
            } else {
                JSONObject item = json.optJSONObject("cats");
                if (null != item) {
                    this.cats.add(new Cats(item));
                }
            }


            this.food = new ArrayList<Food>();
            JSONArray arrayFood = json.optJSONArray("food");
            if (null != arrayFood) {
                int foodLength = arrayFood.length();
                for (int i = 0; i < foodLength; i++) {
                    JSONObject item = arrayFood.optJSONObject(i);
                    if (null != item) {
                        this.food.add(new Food(item));
                    }
                }
            } else {
                JSONObject item = json.optJSONObject("food");
                if (null != item) {
                    this.food.add(new Food(item));
                }
            }
        }

    }
    
    public ArrayList<Cats> getCats() {
        return this.cats;
    }

    public void setCats(ArrayList<Cats> cats) {
        this.cats = cats;
    }

    public ArrayList<Food> getFood() {
        return this.food;
    }

    public void setFood(ArrayList<Food> food) {
        this.food = food;
    }


    
}
