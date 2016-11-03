package com.razor.droidboard.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.razor.droidboard.MyApplication;
import com.razor.droidboard.R;
import com.razor.droidboard.screens.SettingsScreen;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ruan on 12/11/2015.
 */
public class RetrofitUtils
{
    private static String baseUrl = "https://project-9138099718536031788.firebaseio.com";        //Dev
//    private static String baseUrl = "https://logbook.logbox.co.za";       //Live
//    private static String baseUrl = "https://qa.logbox.co.za";            //QA

    /**
     * Stuff to remember to change before release
     *
     * shared pref type default on this screen and settingsscreen
     *
     * @return
     */

    public static Retrofit build()
    {
        OkHttpClient client = createClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit;
    }

    private static OkHttpClient createClient()
    {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);
        return client.build();
    }

    public static String getBaseUrl()
    {
        return baseUrl;
    }

    public static void setBaseUrl(String url)
    {
        baseUrl = url;
    }
}
