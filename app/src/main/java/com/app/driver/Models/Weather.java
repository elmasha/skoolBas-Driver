package com.app.driver.Models;

public class Weather {
    private String name,country,temp,description,humidity,temp_min,temp_max;
    private long dt,sunset,sunrise;

    public Weather() {
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getTemp() {
        return temp;
    }

    public String getDescription() {
        return description;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public long getDt() {
        return dt;
    }

    public long getSunset() {
        return sunset;
    }

    public long getSunrise() {
        return sunrise;
    }
}
