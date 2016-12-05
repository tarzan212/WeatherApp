package com.example.android.weatherapp;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by USUARIO on 04/12/2016.
 */
public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return prefs.getString("city","Beirut, LB"); }

    public void setCity(String city) {
        prefs.edit().putString("city",city).commit();
    }
}
