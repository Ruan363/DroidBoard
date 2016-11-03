package com.razor.droidboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.razor.droidboard.R;
import com.razor.droidboard.models.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruan on 6/15/2016.
 */
public class CountrySpinnerAdapter extends ArrayAdapter<Country>
{
    Context m_context;
    int m_resource;
    ArrayList<Country> m_items;

    public CountrySpinnerAdapter(Context context, int resource, ArrayList<Country> objects)
    {
        super(context, resource, objects);
        m_context = context;
        m_resource = resource;
        m_items = objects;
    }

    public int getItemPos(String obj)
    {
        int counter = 0;
        for (Country item : m_items)
        {
            if (item.getName().equalsIgnoreCase(obj))
            {
                return counter;
            }
            counter++;
        }
        return 0;
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent)
//    {
//        View row = convertView;
//        if(row == null)
//        {
//            //inflate your customlayout for the textview
//            LayoutInflater inflater = LayoutInflater.from(m_context);
//            row = inflater.inflate(R.layout.dialog_text_row, parent, false);
//        }
//        //put the data in it
//        Country item = m_items.get(position);
//
//        TextView text = (TextView) row.findViewById(R.id.tvContent);
//        text.setText(item.getName());
//
//        return row;
//    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            //inflate your customlayout for the textview
            LayoutInflater inflater = LayoutInflater.from(m_context);
            row = inflater.inflate(R.layout.dialog_text_row, parent, false);
        }
        //put the data in it
        Country item = m_items.get(position);

        TextView text = (TextView) row.findViewById(R.id.tvContent);
        text.setText(item.getName());

        return row;
    }
}
