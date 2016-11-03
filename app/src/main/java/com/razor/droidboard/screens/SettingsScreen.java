package com.razor.droidboard.screens;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.razor.droidboard.R;
import com.razor.droidboard.models.User;
import com.razor.droidboard.screens.base.BaseDrawerActivity;

import java.util.List;

public class SettingsScreen extends BaseDrawerActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState, R.layout.activity_settings_screen);
    }

    public void logout(View view)
    {
        makeQuestionDialog(SettingsScreen.this, "Logout", "Are you sure?", "YES", m_yesClick, "NO", null);
    }

    DialogInterface.OnClickListener m_yesClick = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which)
        {
            User.clearUser();
            goToActivity(SettingsScreen.this, LoginScreen.class, 0, true);
        }
    };

    public void gotoNotifications(View view)
    {
        goToActivity(SettingsScreen.this, NotificationScreen.class, 0, false);
    }

    public void gotoGoogle(View view)
    {
        goToActivity(SettingsScreen.this, GoogleSettingsScreen.class, 0, false);
    }
}
