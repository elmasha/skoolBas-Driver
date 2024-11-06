package com.app.driver.Models;

import java.util.Date;

public class Followers {



    private String id,user,uid,image;
    private Date created_at;
    public Followers() {
    }

    public Followers(String id, String user, String uid, String image, Date created_at) {
        this.id = id;
        this.user = user;
        this.uid = uid;
        this.image = image;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getUid() {
        return uid;
    }

    public String getImage() {
        return image;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
