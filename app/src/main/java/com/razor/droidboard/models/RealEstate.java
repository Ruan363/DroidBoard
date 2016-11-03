package com.razor.droidboard.models;

import java.util.ArrayList;

/**
 * Created by ruan on 10/14/2016.
 */

public class RealEstate
{
    private String price;
    private String suburb;
    private String province;
    private String country;
    private Integer amountAttachments;
    private ArrayList<Attachment> attachments;
    private String date;
    private Integer flagId;

    public RealEstate()
    {

    }

    public RealEstate(String price, String suburb, String province, String country, Integer amountAttachments, String date)
    {
        this.price = price;
        this.suburb = suburb;
        this.province = province;
        this.country = country;
        this.amountAttachments = amountAttachments;
        this.date = date;
    }

    public Integer getFlagId()
    {
        return flagId;
    }

    public void setFlagId(Integer flagId)
    {
        this.flagId = flagId;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price = price;
    }

    public String getSuburb()
    {
        return suburb;
    }

    public void setSuburb(String suburb)
    {
        this.suburb = suburb;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public Integer getAmountAttachments()
    {
        return amountAttachments;
    }

    public void setAmountAttachments(Integer amountAttachments)
    {
        this.amountAttachments = amountAttachments;
    }

    public ArrayList<Attachment> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments)
    {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }

        if (!(o instanceof RealEstate))
        {
            return false;
        }

        RealEstate compObj = (RealEstate) o;

        Boolean suburbSame = this.suburb.equalsIgnoreCase(compObj.getSuburb());
        Boolean provinceSame = this.province.equalsIgnoreCase(compObj.getProvince());
        Boolean priceSame = this.price.equalsIgnoreCase(compObj.getPrice());

        if (suburbSame && provinceSame && priceSame)
        {
            return true;
        }

        return false;
    }
}
