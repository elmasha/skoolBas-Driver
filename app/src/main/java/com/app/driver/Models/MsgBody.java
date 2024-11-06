package com.app.driver.Models;

import java.util.Date;

public class MsgBody {


    public String content,type,uid;
    public Date created_at;


    public MsgBody() {
    }

    public MsgBody(String content, String type, String uid, Date created_at) {
        this.content = content;
        this.type = type;
        this.uid = uid;
        this.created_at = created_at;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public String getUid() {
        return uid;
    }

    public Date getCreated_at() {
        return created_at;
    }
}
