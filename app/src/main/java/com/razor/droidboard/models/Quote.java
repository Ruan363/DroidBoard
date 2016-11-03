package com.razor.droidboard.models;

import com.orm.SugarRecord;

/**
 * Created by ruan on 4/22/2016.
 */
public class Quote extends SugarRecord
{
    private String author;
    private String quote;

    public Quote()
    {
    }

    public Quote(String author, String quote)
    {
        this.author = author;
        this.quote = quote;
    }

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getQuote()
    {
        return quote;
    }

    public void setQuote(String quote)
    {
        this.quote = quote;
    }
}
