package com.app.driver.Models;

import java.util.Date;
import java.util.List;

public class AllFarms {

    private String farmCategory, farmContacts, farmDescription, farmName, farmOffers, image, user_id, user, County, Country;
    private Date timestamp;
    private double rating, lat, lng;

    private boolean active;

    private List<String> followers;
    private List<String> following;

    public AllFarms() {
        //...empty...
    }

    public AllFarms(String farmCategory, String farmContacts, String farmDescription, String farmName, String farmOffers, String image, String user_id, String user, String county, String country,
                    Date timestamp, double rating, double lat, double lng, boolean active, List<String> followers, List<String> following) {
        this.farmCategory = farmCategory;
        this.farmContacts = farmContacts;
        this.farmDescription = farmDescription;
        this.farmName = farmName;
        this.farmOffers = farmOffers;
        this.image = image;
        this.user_id = user_id;
        this.user = user;
        County = county;
        Country = country;
        this.timestamp = timestamp;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.active = active;
        this.followers = followers;
        this.following = following;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    public String getCounty() {
        return County;
    }

    public String getCountry() {
        return Country;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getUser() {
        return user;
    }

    public double getRating() {
        return rating;
    }

    public String getFarmCategory() {
        return farmCategory;
    }

    public String getFarmContacts() {
        return farmContacts;
    }

    public String getFarmDescription() {
        return farmDescription;
    }

    public String getFarmName() {
        return farmName;
    }

    public String getFarmOffers() {
        return farmOffers;
    }

    public String getImage() {
        return image;
    }

    public String getUser_id() {
        return user_id;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
