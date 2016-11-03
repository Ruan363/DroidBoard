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

/**
 * Created by ruan on 2/17/2016.
 */
public class GenericSpinnerAdapter extends ArrayAdapter<String>
{
    private Context m_context;
    private ArrayList<String> m_items;
    private int m_resource;

    public GenericSpinnerAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        m_context = context;
        m_resource = resource;
        m_items = objects;
    }

    public int getItemPos(String obj)
    {
        int counter = 0;
        for (String item : m_items) {
            if (item.equalsIgnoreCase(obj))
                return counter;
            counter++;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            //inflate your customlayout for the textview
            LayoutInflater inflater = LayoutInflater.from(m_context);
            row = inflater.inflate(R.layout.generic_text_row, parent, false);
        }
        //put the data in it
        String item = m_items.get(position);

        TextView text = (TextView) row.findViewById(R.id.tvContent);
        text.setText(item);

        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;
        if(row == null)
        {
            //inflate your customlayout for the textview
            LayoutInflater inflater = LayoutInflater.from(m_context);
            row = inflater.inflate(R.layout.generic_text_row, parent, false);
        }
        //put the data in it
        String item = m_items.get(position);

        TextView text = (TextView) row.findViewById(R.id.tvContent);
        text.setText(item);

        return row;
    }
}
