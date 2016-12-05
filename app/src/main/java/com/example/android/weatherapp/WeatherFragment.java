package com.example.android.weatherapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by USUARIO on 05/12/2016.
 */
public class WeatherFragment extends Fragment {

    Typeface weatherfont;

    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;

    Handler handler;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState); //possible cause to error

        View rootView = inflater.inflate(R.layout.weather_fragment,container,false);

        cityField = (TextView) rootView.findViewById(R.id.cityField);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        detailsField = (TextView) rootView.findViewById(R.id.detailsField);
        currentTemperatureField = (TextView)  rootView.findViewById(R.id.detailsField);
        weatherIcon = (TextView) rootView.findViewById(R.id.weatherIcon);

        weatherIcon.setTypeface(weatherfont);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weatherfont = Typeface.createFromAsset(getActivity().getAssets(),"font/weathericons-regular-webfont.ttf");
        updateWeatherData(new CityPreference(getActivity()).getCity());

    }

    public void updateWeatherData(final String city) {

        try{
            new Thread() {
                @Override
                public void run() {
                    final JSONObject jsondata = RemoteFetch.getJSON(getActivity(), city);
                    if (jsondata == null) {
                        try {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getActivity(), getActivity().getString(R.string.nodata), Toast.LENGTH_LONG).show();
                                }
                            });
                        }catch(Exception e) {
                            Toast.makeText(getActivity(),"Exited",Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                renderWeather(jsondata);
                            }
                        });
                    }
                }
            }.start();
        }
        catch(Exception e) {
            Log.d("Erreur" ,"Cannot proceed ");
        }

    }
    public void renderWeather(JSONObject jsondata) {
        try {
            cityField.setText(jsondata.getString("name").toUpperCase(Locale.FRANCE)
            + jsondata.getJSONObject("sys").getString("country"));

            JSONObject details = jsondata.getJSONArray("weather").getJSONObject(0);
            JSONObject main = jsondata.getJSONArray("main").getJSONObject(0);
            detailsField.setText(details.getString("description") +" \n Humidity "+main.getString("humidity")+"\n Pressure "+
            main.getString("pressure")+" hPa");

            currentTemperatureField.setText(main.getString("temp")+" ºC");

            DateFormat dt = DateFormat.getDateTimeInstance();

            String update= dt.format(new Date(jsondata.getLong("dt")*1000));
            updatedField.setText("Last update :"+update);

            setWeatherIcon(details.getInt("id"),jsondata.getJSONObject("sys").getLong("sunrise")*1000,jsondata.getJSONObject("sys").getLong("sunset")*1000);


        } catch(Exception e) {
            Log.d("Error :","One or many fields missing in the json data");
        }
    }
    private void setWeatherIcon(int actualId,long sunrise,long sunset) {
        int id = actualId/100;

        String icon="";
        if(actualId == 800) {
            long currenttime = new Date().getTime();
            if(currenttime>sunrise && currenttime<sunset) {
                icon = getActivity().getString(R.string.sunny);
            } else icon = getActivity().getString(R.string.nighttime);

        } else {
            switch(id) {
                case 2:
                    icon = getActivity().getString(R.string.thunderstorm);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.drizzle);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.rain);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.snow);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.cloudy);
                    break;

            }
        }
        weatherIcon.setText(icon);
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }
}
