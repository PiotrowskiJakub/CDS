/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.api;

/**
 *
 * @author T530
 */
public class MapsParametersContainer {
    public final static int SIZE = 500;
    private MapsParameter[][] parametersArray = new MapsParameter[SIZE][SIZE];
    private final static MapsParametersContainer instance = new MapsParametersContainer();
    
    private MapsParametersContainer()
    {
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
                parametersArray[i][j] = new MapsParameter();
        }
    }
    
    public static MapsParametersContainer getInstance()
    {
        return instance;
    }
    
    public MapsParameter get(int x, int y)
    {
        if(x < SIZE && y < SIZE)
            return parametersArray[x][y];
        else
            return null;
    }
    
    public void clearPopulation()
    {
        for(int i = 0; i < SIZE; i++)
        {
            for(int j = 0; j < SIZE; j++)
                parametersArray[i][j].getPeopleList().clear();
        }
    }
    
    public boolean checkWater(int x, int y)
    {
        if(parametersArray[x][y].isLand())
            return false;
        else
            return true;
    }
}
