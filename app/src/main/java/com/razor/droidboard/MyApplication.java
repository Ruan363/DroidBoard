package com.razor.droidboard;

import android.app.Application;
import android.content.Context;

import com.orm.SugarApp;

/**
 * Created by ruan on 6/14/2016.
 */
public class MyApplication extends SugarApp
{
//    private Tracker m_tracker;
    private static Context m_context;

    @Override
    public void onCreate()
    {
        super.onCreate();

        MyApplication.m_context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.m_context;
    }
}
