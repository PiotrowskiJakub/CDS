/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cds.api;

import java.util.ArrayList;

/**
 *
 * @author T530
 */
public class MapsParameter 
{
    private boolean land; // if true -> land, else if false -> water
    private ArrayList<Person> peopleList = new ArrayList<Person>();

    public boolean isLand() {
        return land;
    }

    public void setLand(boolean land) {
        this.land = land;
    }

    public ArrayList<Person> getPeopleList() {
        return peopleList;
    }

    public void setPeopleList(ArrayList<Person> peopleList) {
        this.peopleList = peopleList;
    }
}
