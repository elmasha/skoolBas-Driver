package com.app.driver.Models;

public class Counties {
    private String county;
    private String state;
    private long no;


    public Counties() {
    }

    public Counties(String county, String state, long no) {
        this.county = county;
        this.state = state;
        this.no = no;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public long getNo() {
        return no;
    }

    public void setNo(long no) {
        this.no = no;
    }

    public String getState() {
        return state;
    }
}