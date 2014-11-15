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
    private Coordinates coordinates;    
    private Terrain terrain;
    private Integer height;       
            
    public enum Terrain {
        LAND, WATER;  //; is optional
    }

    public PointParameters(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    
    

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
    
    
    
}
