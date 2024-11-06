package com.app.driver.Models;

import java.util.Date;

public class UserAccount {

  private  String  Email,Farm_name, Phone_NO, User_ID,  device_token, farm_category, farm_type, userName,farm_image;

  private long ads_count;
  private Date timestamp;
  private double lat,lng;

    public UserAccount() {
    }

    public UserAccount(String email, String farm_name, String phone_NO, String user_ID, String device_token,
                       String farm_category, String farm_type, String userName, String farm_image, long ads_count, Date timestamp, double lat, double lng) {
        Email = email;
        Farm_name = farm_name;
        Phone_NO = phone_NO;
        User_ID = user_ID;
        this.device_token = device_token;
        this.farm_category = farm_category;
        this.farm_type = farm_type;
        this.userName = userName;
        this.farm_image = farm_image;
        this.ads_count = ads_count;
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
    }


    public String getFarm_image() {
        return farm_image;
    }

    public long getAds_count() {
        return ads_count;
    }

    public String getEmail() {
        return Email;
    }

    public String getFarm_name() {
        return Farm_name;
    }

    public String getPhone_NO() {
        return Phone_NO;
    }

    public String getUser_ID() {
        return User_ID;
    }



    public String getDevice_token() {
        return device_token;
    }

    public String getFarm_category() {
        return farm_category;
    }

    public String getFarm_type() {
        return farm_type;
    }

    public String getUserName() {
        return userName;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
