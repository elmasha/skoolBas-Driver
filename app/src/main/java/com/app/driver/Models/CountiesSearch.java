package com.app.driver.Models;

public class CountiesSearch {
    private String category;


    public CountiesSearch() {
    }

    public CountiesSearch(String county) {
        this.category = county;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }



}