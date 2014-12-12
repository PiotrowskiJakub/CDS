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
    public static int POPULATION_SIZE = 0;
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
    
    public boolean isLand(int x, int y)
    {
        return parametersArray[x][y].isLand();
    }
    
    public void addPerson(int x, int y, Person perosn)
    {
        parametersArray[x][y].getPeopleList().add(perosn);
    }
    
    public boolean isEmpty(int x, int y)
    {
        return parametersArray[x][y].getPeopleList().isEmpty();
    }
    
    public boolean isSatisfaction(int x, int y, int which)
    {
        return parametersArray[x][y].getPeopleList().get(which).isSatisfacion();
    }
    
    public int size(int x, int y)
    {
        return parametersArray[x][y].getPeopleList().size();
    }
    
    public Person get(int x, int y, int which)
    {
        return parametersArray[x][y].getPeopleList().get(which);
    }
    
    public void clear(int x, int y)
    {
        parametersArray[x][y].getPeopleList().clear();
    }
    
    public int getAge(int x, int y, int which)
    {
        return parametersArray[x][y].getPeopleList().get(which).getAge();
    }
    
    public void addYear()
    {
        for(int x = 0; x < SIZE; x++)
        {
            for(int y = 0; y < SIZE; y++)
            {
                if(!isEmpty(x, y))
                {
                    for(int i = 0; i < size(x, y); i++)
                        get(x, y, i).addYear();
                }
            }
        }
    }
}
