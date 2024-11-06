package com.app.driver.Models;

import java.util.Date;

public class FarmerAround {

  private String farm_Name,farm_id,farm_category,farm_location,farm_image;
  private double farm_rating;
  private Date timestamp;

    public FarmerAround() {
    }

    public FarmerAround(String farm_Name, String farm_id, String farm_category, String farm_location, String farm_image, double farm_rating, Date timestamp) {
        this.farm_Name = farm_Name;
        this.farm_id = farm_id;
        this.farm_category = farm_category;
        this.farm_location = farm_location;
        this.farm_image = farm_image;
        this.farm_rating = farm_rating;
        this.timestamp = timestamp;
    }




    public String getFarm_image() {
        return farm_image;
    }

    public String getFarm_location() {
        return farm_location;
    }

    public String getFarm_Name() {
        return farm_Name;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public String getFarm_category() {
        return farm_category;
    }

    public double getFarm_rating() {
        return farm_rating;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
