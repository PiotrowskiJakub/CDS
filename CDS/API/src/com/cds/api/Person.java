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
public class Person 
{
    private int age;
    private boolean satisfacion;

    public Person(int age, boolean satisfacion) {
        this.age = age;
        this.satisfacion = satisfacion;
    }
    
    public Person(Person person)
    {
        this.age = person.getAge();
        this.satisfacion = person.isSatisfacion();
    }

    public void addYear()
    {
        age++;
    }
    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    
    public boolean isSatisfacion() {
        return satisfacion;
    }

    public void setSatisfacion(boolean satisfacion) {
        this.satisfacion = satisfacion;
    }
}
