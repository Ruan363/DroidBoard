package com.razor.droidboard.interfaces;

import com.razor.droidboard.models.Car;
import com.razor.droidboard.models.Country;
import com.razor.droidboard.models.Location;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ruan on 6/14/2016.
 */
public interface IMainConttroller
{
    @GET("/cars.json")
    Call<ArrayList<Car>> getCars();

    @GET("/locations.json")
    Call<ArrayList<Location>> getLocations();

    @GET("/language.json")
    Call<String> getLanguage();

    @GET("v1/all")
    Call<ArrayList<Country>> getCountries();
}
