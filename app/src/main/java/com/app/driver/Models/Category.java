package com.app.driver.Models;

public class Category {

    private String cat_id,category;
    public Category() {
    }

    public Category(String cat_id, String category) {
        this.cat_id = cat_id;
        this.category = category;
    }

    public String getCat_id() {
        return cat_id;
    }

    public String getCategory() {
        return category;
    }
}
