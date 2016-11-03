package com.razor.droidboard.utilities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.razor.droidboard.R;

import java.util.ArrayList;

/**
 * Created by ruan on 10/13/2016.
 */

public class ScreenUtils
{
    public static void showListDialog(Context context, ArrayList<String> results, String title, DialogInterface.OnClickListener clickListener, String negButtonText, DialogInterface.OnClickListener negClick, Boolean cancelable)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);

        View v = LayoutInflater.from(context).inflate(R.layout.custom_popup_title, null, false);
        TextView m_tvTitle = (TextView) v.findViewById(R.id.tvTitle);
        m_tvTitle.setText(title);

        builderSingle.setCustomTitle(v);
//        builderSingle.setTitle(title);

        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(context, R.layout.dialog_text_row);
        arrayAdapter.addAll(results);

        builderSingle.setNegativeButton(negButtonText, negClick);

        builderSingle.setAdapter(arrayAdapter, clickListener);

        if (cancelable != null)
            builderSingle.setCancelable(cancelable);
        else
            builderSingle.setCancelable(true);

//        builderSingle.show();
        AlertDialog alertDialogObject = builderSingle.create();
        ListView listView=alertDialogObject.getListView();
        listView.setDivider(new ColorDrawable(context.getResources().getColor(R.color.lightestGrey))); // set color
        listView.setDividerHeight(1); // set height
        alertDialogObject.show();
    }

    public static void showListDialog(Context context, ArrayList<String> results, String title, DialogInterface.OnClickListener clickListener, String negButtonText, DialogInterface.OnClickListener negClick)
    {
        showListDialog(context, results, title, clickListener, negButtonText, negClick, null);
    }

    public static void showConfirmationDialog(Context context, DialogInterface.OnClickListener yesClick, DialogInterface.OnClickListener noClick)
    {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
        builderSingle.setTitle("Are you sure?");

        builderSingle.setPositiveButton("YES", yesClick);
        builderSingle.setNegativeButton("NO", noClick);

        builderSingle.show();
    }

    public static ProgressDialog showProgressDialog(Context context, String message)
    {
        if (TextUtils.isEmpty(message))
            message = "Please Wait...";

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
        return progressDialog;
    }
}
