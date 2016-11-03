package com.razor.droidboard.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

/**
 * Created by ruan on 5/17/2016.
 */
public class Snapshot extends SugarRecord
{
    private String description;
    private String filePath;
    @Ignore
    private int height;
    @Ignore
    private int width;

    public Snapshot()
    {
    }

    public Snapshot(String description, String filePath)
    {
        this.description = description;
        this.filePath = filePath;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getFilePath()
    {
        return filePath;
    }

    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }
}
