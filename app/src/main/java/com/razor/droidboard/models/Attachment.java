package com.razor.droidboard.models;

/**
 * Created by ruan on 10/11/2016.
 */

public class Attachment
{
    private Integer id;
    private String imagePath;
    private String attachmentDescription;
    private Integer position;
    private Boolean isMainAtt;

    public Attachment()
    {

    }

    public Attachment(String attachmentDescription)
    {
        this.attachmentDescription = attachmentDescription;
    }

    public Attachment(Integer id, String attachmentDescription)
    {
        this(attachmentDescription);
        this.id = id;
    }

    public Attachment(Integer id, Integer position, String attachmentDescription, Boolean isMainAtt)
    {
        this(id, attachmentDescription);
        this.position = position;
        this.isMainAtt = isMainAtt;
    }

    public Attachment(Integer id, Integer position, String attachmentDescription, Boolean isMainAtt, String imagePath)
    {
        this(id, position, attachmentDescription, isMainAtt);
        this.imagePath = imagePath;
    }

    public String getImagePath()
    {
        return imagePath;
    }

    public void setImagePath(String imagePath)
    {
        this.imagePath = imagePath;
    }

    public String getAttachmentDescription()
    {
        return attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription)
    {
        this.attachmentDescription = attachmentDescription;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getPosition()
    {
        return position;
    }

    public void setPosition(Integer position)
    {
        this.position = position;
    }

    public Boolean getIsMainAtt()
    {
        return isMainAtt;
    }

    public void setIsMainAtt(Boolean isMainAtt)
    {
        this.isMainAtt = isMainAtt;
    }
}
