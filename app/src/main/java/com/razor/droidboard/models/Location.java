package com.razor.droidboard.models;

/**
 * Created by ruan on 6/14/2016.
 */
public class Location
{
    String country;
    String province;
    String city;

    public Location()
    {
    }

    public Location(String country, String province, String city)
    {
        this.country = country;
        this.province = province;
        this.city = city;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }
}
