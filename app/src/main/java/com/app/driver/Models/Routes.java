package com.app.driver.Models;

public class Routes {


    private Long start_location_lat,
            start_location_lng, end_location_lat,
            end_location_lng;
    private String
            estimated_arrival_time,start_location,end_location,
            estimated_departure_time,
            ref_id,
            route_name,bus_type,bus_RegNo;

    public Routes() {
    }


    public Routes(Long start_location_lat, Long start_location_lng, Long end_location_lat, Long end_location_lng, String estimated_arrival_time, String start_location, String end_location, String estimated_departure_time, String ref_id,
                  String route_name, String bus_type, String bus_RegNo) {
        this.start_location_lat = start_location_lat;
        this.start_location_lng = start_location_lng;
        this.end_location_lat = end_location_lat;
        this.end_location_lng = end_location_lng;
        this.estimated_arrival_time = estimated_arrival_time;
        this.start_location = start_location;
        this.end_location = end_location;
        this.estimated_departure_time = estimated_departure_time;
        this.ref_id = ref_id;
        this.route_name = route_name;
        this.bus_type = bus_type;
        this.bus_RegNo = bus_RegNo;
    }


    public String getBus_type() {
        return bus_type;
    }

    public void setBus_type(String bus_type) {
        this.bus_type = bus_type;
    }

    public String getBus_RegNo() {
        return bus_RegNo;
    }

    public void setBus_RegNo(String bus_RegNo) {
        this.bus_RegNo = bus_RegNo;
    }

    public String getStart_location() {
        return start_location;
    }

    public void setStart_location(String start_location) {
        this.start_location = start_location;
    }

    public String getEnd_location() {
        return end_location;
    }

    public void setEnd_location(String end_location) {
        this.end_location = end_location;
    }

    public Long getStart_location_lat() {
        return start_location_lat;
    }

    public void setStart_location_lat(Long start_location_lat) {
        this.start_location_lat = start_location_lat;
    }

    public Long getStart_location_lng() {
        return start_location_lng;
    }

    public void setStart_location_lng(Long start_location_lng) {
        this.start_location_lng = start_location_lng;
    }

    public Long getEnd_location_lat() {
        return end_location_lat;
    }

    public void setEnd_location_lat(Long end_location_lat) {
        this.end_location_lat = end_location_lat;
    }

    public Long getEnd_location_lng() {
        return end_location_lng;
    }

    public void setEnd_location_lng(Long end_location_lng) {
        this.end_location_lng = end_location_lng;
    }

    public String getEstimated_arrival_time() {
        return estimated_arrival_time;
    }

    public void setEstimated_arrival_time(String estimated_arrival_time) {
        this.estimated_arrival_time = estimated_arrival_time;
    }

    public String getEstimated_departure_time() {
        return estimated_departure_time;
    }

    public void setEstimated_departure_time(String estimated_departure_time) {
        this.estimated_departure_time = estimated_departure_time;
    }

    public String getRef_id() {
        return ref_id;
    }

    public void setRef_id(String ref_id) {
        this.ref_id = ref_id;
    }

    public String getRoute_name() {
        return route_name;
    }

    public void setRoute_name(String route_name) {
        this.route_name = route_name;
    }
}


