package com.gq.com.easy_parking.bean;

/**
 * Created by hasee on 2017/4/20.
 */
public class ParkingSpotInfo {
    private int id;
    private String title;
    private double longitude;
    private double latitude;
    private boolean isFull;
    private int rest;
    private int capacity;

    public ParkingSpotInfo(int id, String title, double longitude, double latitude, boolean isFull, int rest, int capacity) {
        this.id = id;
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isFull = isFull;
        this.rest = rest;
        this.capacity = capacity;
    }

    public ParkingSpotInfo(String title, double longitude, double latitude) {
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = 0;
        this.isFull = false;
        this.rest = 10;
        this.capacity = 100;
    }

    public ParkingSpotInfo() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
