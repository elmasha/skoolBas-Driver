package com.app.driver.Models;

import java.util.Date;

public class Ads {

    private String Ad_title,Ad_description,Ad_category,Ad_price,Ad_type,Ad_image,Ad_id;
    private Date timestamp;

    public Ads() {
    }


    public Ads(String ad_title, String ad_description, String ad_category,
               String ad_price, String ad_type, String ad_image, String ad_id, Date timestamp) {
        Ad_title = ad_title;
        Ad_description = ad_description;
        Ad_category = ad_category;
        Ad_price = ad_price;
        Ad_type = ad_type;
        Ad_image = ad_image;
        Ad_id = ad_id;
        this.timestamp = timestamp;
    }


    public String getAd_id() {
        return Ad_id;
    }

    public String getAd_category() {
        return Ad_category;
    }

    public String getAd_image() {
        return Ad_image;
    }

    public String getAd_type() {
        return Ad_type;
    }

    public String getAd_title() {
        return Ad_title;
    }

    public String getAd_description() {
        return Ad_description;
    }

    public String getAd_price() {
        return Ad_price;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
