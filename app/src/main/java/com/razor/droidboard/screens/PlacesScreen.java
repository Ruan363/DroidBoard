package com.razor.droidboard.screens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.razor.droidboard.R;
import com.razor.droidboard.screens.base.BaseDrawerActivity;

public class PlacesScreen extends BaseDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState, R.layout.activity_places_screen);
    }

    public void addMarker(View view)
    {
        goToActivity(this, PlacesPhotosMapScreen.class, 0, false);
    }
}
