package com.razor.droidboard.models;

/**
 * Created by ruan on 6/14/2016.
 */
public class Car
{
    String make;
    String model;

    public Car()
    {

    }

    public Car(String make, String model)
    {
        this.make = make;
        this.model = model;
    }

    public String getMake()
    {
        return make;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public String getModel()
    {
        return model;
    }

    public void setModel(String model)
    {
        this.model = model;
    }
}
