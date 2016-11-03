package com.razor.droidboard.models;

import com.razor.droidboard.interfaces.IStringItem;

/**
 * Created by ruan on 6/15/2016.
 */
public class Country implements IStringItem
{
    String name;

    public Country()
    {
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
