package com.razor.droidboard.screens.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.razor.droidboard.R;

/**
 * Created by ruan on 6/14/2016.
 */
public class BaseActivity extends AppCompatActivity
{

    boolean m_doubleBackToExitPressedOnce = false;
    private ProgressDialog mProgressDialog;

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            if (isTaskRoot())
            {
                if (m_doubleBackToExitPressedOnce)
                {
                    super.onBackPressed();
                    return;
                }

                this.m_doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to EXIT", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        m_doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            }
            else super.onBackPressed();
        }
    }

    public void goToActivity(AppCompatActivity currentScreen, Class<? extends Activity> newScreen, int flags, boolean finish)
    {
        if (!currentScreen.getClass().getSimpleName().equalsIgnoreCase(newScreen.getSimpleName()))
        {
            Intent destination = new Intent(currentScreen, newScreen);
            if (flags != 0) destination.setFlags(flags);

            startActivity(destination);
            if (finish) finish();
        }
    }

    public void goToActivityWithData(AppCompatActivity currentScreen, Class<? extends AppCompatActivity> newScreen, Bundle extras, Boolean noHistory)
    {
        if (!currentScreen.getClass().getSimpleName().equalsIgnoreCase(newScreen.getSimpleName()))
        {
            Intent destination = new Intent(currentScreen, newScreen);
            if (noHistory)
                destination.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (extras != null) destination.putExtras(extras);
            startActivity(destination);
        }
    }

    public void goToActivityWithDataForResult(AppCompatActivity currentScreen, Class<? extends AppCompatActivity> newScreen, Bundle extras, int requestCode)
    {
        Intent destination = new Intent(currentScreen, newScreen);
        if (extras != null) destination.putExtras(extras);
        startActivityForResult(destination, requestCode);
    }

    //Toast
    public void makeToast(final CharSequence text, final int gravity, final boolean longDuration)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                Toast t;
                if (longDuration)
                    t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                else t = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                t.setGravity(gravity, 0, 0);
                t.show();
            }
        });

    }

    public void makeToast(CharSequence text, boolean longDuration)
    {
        makeToast(text, Gravity.CENTER, longDuration);
    }

    public void makeQuestionDialog(Context context, String title, String question, String posText, DialogInterface.OnClickListener posClick, String negText, DialogInterface.OnClickListener negClick)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);

        if (question != null && !question.isEmpty())
            builder.setMessage(question);

        builder.setPositiveButton(posText, posClick);

        builder.setNegativeButton(negText, negClick);

        builder.show();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

}
