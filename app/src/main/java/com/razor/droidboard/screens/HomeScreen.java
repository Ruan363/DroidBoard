package com.razor.droidboard.screens;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.razor.droidboard.R;
import com.razor.droidboard.adapters.RealEstatePagerAdapter;
import com.razor.droidboard.screens.base.BaseDrawerActivity;

public class HomeScreen extends BaseDrawerActivity
{
    private AppBarLayout m_appBar;
    private ViewPager m_viewPager;
    private TabLayout m_tabLayout;
    private RealEstatePagerAdapter m_sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState, R.layout.activity_home_screen);

        instantiateScreen();
    }

    private void instantiateScreen()
    {
        m_appBar = (AppBarLayout) findViewById(R.id.appbar);
        m_sectionsPagerAdapter = new RealEstatePagerAdapter(getSupportFragmentManager(), this);

        m_viewPager = (ViewPager) findViewById(R.id.container);
        m_viewPager.setAdapter(m_sectionsPagerAdapter);

        m_tabLayout = (TabLayout) findViewById(R.id.tabs);
        m_tabLayout.setupWithViewPager(m_viewPager);
    }

    public void addRealEstate(View view)
    {
        goToActivity(this, AddRealEstateScreen.class, 0, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.multi_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.upload:

                return true;
            case R.id.help:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
