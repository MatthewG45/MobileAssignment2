package com.example.assignment2;

public class LocationModel {
    private int ID;
    private String address;
    private double latitude;
    private double longitude;
    private String state;

    public LocationModel(int ID, String address, double latitude, double longitude, String state) {
        this.ID = ID;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state = state;
    }

    @Override
    public String toString() {
        return "LocationModel{" +
                "ID=" + ID +
                ", address='" + address + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", state='" + state + '\'' +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
