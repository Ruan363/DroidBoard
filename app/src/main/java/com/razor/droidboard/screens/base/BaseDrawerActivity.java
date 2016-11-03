package com.razor.droidboard.screens.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.razor.droidboard.R;
import com.razor.droidboard.screens.HomeScreen;
import com.razor.droidboard.screens.LoginScreen;
import com.razor.droidboard.screens.QuotesScreen;
import com.razor.droidboard.screens.SettingsScreen;
import com.razor.droidboard.screens.SnapshotScreen;

/**
 * Created by ruan on 4/21/2016.
 */
public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Toolbar m_toolbar;
    DrawerLayout m_drawer;
    ActionBarDrawerToggle m_toggle;
    NavigationView m_navigationView;
    boolean m_doubleBackToExitPressedOnce = false;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    protected void onCreate(@Nullable Bundle savedInstanceState, int layout)
    {
        setContentView(layout);

        m_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(m_toolbar);

        m_drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        m_toggle = new ActionBarDrawerToggle(this, m_drawer, m_toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        m_drawer.setDrawerListener(m_toggle);
        m_toggle.syncState();

        m_navigationView = (NavigationView) findViewById(R.id.nav_view);
        m_navigationView.setVisibility(View.VISIBLE);
        m_navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_quotes)
        {
            goToActivity(BaseDrawerActivity.this, QuotesScreen.class, 0, true);
        }
        else if (id == R.id.nav_home)
        {
            goToActivity(BaseDrawerActivity.this, HomeScreen.class, 0, true);
        }
        else if (id == R.id.nav_snapshots)
        {
            goToActivity(BaseDrawerActivity.this, SnapshotScreen.class, 0, true);
        }
        else if (id == R.id.nav_settings)
        {
            goToActivity(BaseDrawerActivity.this, SettingsScreen.class, 0, true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
