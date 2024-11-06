package com.app.driver.Models;

import java.util.Date;
import java.util.List;

public class Messages {


    public String from_name,from_uid,last_msg,to_uid,to_name,last_uid;
    public Date created_at;
    public long msg_count;
    public boolean status;

    public List<String> userId;


    public Messages() {
    }


    public Messages(String from_name, String from_uid, String last_msg,
                    String to_uid, String to_name, String last_uid, Date created_at, long msg_count, boolean status, List<String> userId) {
        this.from_name = from_name;
        this.from_uid = from_uid;
        this.last_msg = last_msg;
        this.to_uid = to_uid;
        this.to_name = to_name;
        this.last_uid = last_uid;
        this.created_at = created_at;
        this.msg_count = msg_count;
        this.status = status;
        this.userId = userId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public String getLast_uid() {
        return last_uid;
    }

    public boolean isStatus() {
        return status;
    }

    public String getFrom_name() {
        return from_name;
    }

    public String getFrom_uid() {
        return from_uid;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public String getTo_uid() {
        return to_uid;
    }

    public String getTo_name() {
        return to_name;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public long getMsg_count() {
        return msg_count;
    }
}
