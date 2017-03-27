package com.razor.droidboard.models;

import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by ruan.pieterse on 2017/03/27.
 */

public class MapLocation extends SugarRecord
{
    private String description;

    private double m_latitude;

    private double m_longitude;

    public MapLocation() {

    }

    public MapLocation(String description, double m_latitude, double m_longitude) {
        this.description = description;
        this.m_latitude = m_latitude;
        this.m_longitude = m_longitude;
    }
}
