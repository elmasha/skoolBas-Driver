package com.app.driver.Models;

public class AllFarmsSearch extends AllFarms {

    private String id,Farm_name;
    public AllFarmsSearch() {
        //...empty...
    }

    public AllFarmsSearch( String id, String farm_name) {

        this.id = id;
        Farm_name = farm_name;

    }



    public String getId() {
        return id;
    }

    public String getFarm_name() {
        return Farm_name;
    }


}
