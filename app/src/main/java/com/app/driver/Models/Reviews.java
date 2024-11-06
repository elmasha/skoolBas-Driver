package com.app.driver.Models;

import java.util.Date;

public class Reviews {
    private String user,comment;
    private Date timestamp;

    private long likes, rating;

    public Reviews() {
    }

    public String getUser() {
        return user;
    }

    public String getComment() {
        return comment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public long getLikes() {
        return likes;
    }

    public long getRating() {
        return rating;
    }
}
