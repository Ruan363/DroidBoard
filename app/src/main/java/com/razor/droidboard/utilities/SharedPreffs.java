package com.razor.droidboard.utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ruan on 5/17/2016.
 */
public class SharedPreffs
{
    public static final String PREFS_USER = "UserPreferences";



    public static void setRememberUser(Context context, Boolean remember)
    {
        SharedPreferences userSettings = context.getSharedPreferences(PREFS_USER, 0);

        SharedPreferences.Editor editor = userSettings.edit();
        editor.putBoolean("remember_me", remember);

        editor.commit();
    }

    public static Boolean getRememberUser(Context context)
    {
        SharedPreferences userSettings = context.getSharedPreferences(PREFS_USER, 0);

        Boolean remember = userSettings.getBoolean("remember_me", false);

        return remember;
    }
}
