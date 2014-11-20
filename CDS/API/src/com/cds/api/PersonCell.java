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
public class PersonCell 
{
    private boolean sex;
    private int age;
    private PointCoordinates livingPlace;
    private boolean newGeneration;

    public PersonCell(boolean sex, int age, PointCoordinates livingPlace) {
        this.sex = sex;
        this.age = age;
        this.livingPlace = livingPlace;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PointCoordinates getLivingPlace() {
        return livingPlace;
    }

    public void setLivingPlace(PointCoordinates livingPlace) {
        this.livingPlace = livingPlace;
    }

    public boolean isNewGeneration() {
        return newGeneration;
    }

    public void setNewGeneration(boolean newGeneration) {
        this.newGeneration = newGeneration;
    }
}
