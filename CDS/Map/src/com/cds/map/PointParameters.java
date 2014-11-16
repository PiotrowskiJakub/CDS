/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.map;

import com.cds.api.Coordinates;

/**
 *
 * @author Sebastian
 */
public class PointParameters {
    private double latitude;
    private double longitude;
    private boolean land;
    private Integer height;       

    public PointParameters(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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

    public boolean getLand() {
        return land;
    }

    public void setLand(boolean land) {
        this.land = land;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
            
   

    
    
    
    
}
