package com.gq.com.easy_parking.bean;

/**
 * Created by hasee on 2017/4/20.
 */
public class ParkingMarker {
    private int id;
    private String title;
    private double longitude;
    private double latitude;
    private int isFull;
    private int rest;
    private int capacity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getIsFull() {
        return isFull;
    }

    public void setIsFull(int isFull) {
        this.isFull = isFull;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public ParkingMarker(String title, double longitude, double latitude) {
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = 0;
        this.isFull = 0;
        this.rest = 10;
        this.capacity = 100;
    }
    public ParkingMarker(int id, String title, double longitude, double latitude, int isFull, int rest, int capacity) {
        this.id = id;
        this.title = title;
        this.longitude = longitude;
        this.latitude = latitude;
        this.isFull = isFull;
        this.rest = rest;
        this.capacity = capacity;
    }
    public ParkingMarker() {
        super();
    }
}
