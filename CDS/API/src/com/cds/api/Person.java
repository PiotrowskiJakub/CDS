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
    private boolean sex;
    private int age;
    private boolean satisfacion;

    public Person(boolean sex, int age) {
        this.sex = sex;
        this.age = age;
        this.satisfacion = false;
        
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
    
    public boolean isSatisfacion() {
        return satisfacion;
    }

    public void setSatisfacion(boolean satisfacion) {
        this.satisfacion = satisfacion;
    }
}
