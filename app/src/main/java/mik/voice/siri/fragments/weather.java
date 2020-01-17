package mik.voice.siri.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mik.voice.siri.R;
import mik.voice.siri.global;

public class weather extends Fragment {
    View view;
    Button firstButton;
    LinearLayout container_address, container_overview, container_details;
    TextView label_sunrise, label_sunset, label_wind, label_pressure, label_humidity, label_createdby,addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
    sunsetTxt, windTxt, pressureTxt, humidityTxt;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather, container, false);
        container_address = view.findViewById(R.id.addressContainer);
        container_overview = view.findViewById(R.id.overviewContainer);
        container_details = view.findViewById(R.id.detailsContainer);
        label_sunrise = view.findViewById(R.id.label_sunrise);
        label_sunset = view.findViewById(R.id.label_sunset);
        label_wind = view.findViewById(R.id.label_wind);
        label_pressure = view.findViewById(R.id.label_pressure);
        label_humidity = view.findViewById(R.id.label_humidity);
        label_createdby = view.findViewById(R.id.label_createdby);
        addressTxt = view.findViewById(R.id.address);
        updated_atTxt = view.findViewById(R.id.updated_at);
        statusTxt = view.findViewById(R.id.status);
        tempTxt = view.findViewById(R.id.temp);
        temp_minTxt = view.findViewById(R.id.temp_min);
        temp_maxTxt = view.findViewById(R.id.temp_max);
        sunriseTxt = view.findViewById(R.id.sunrise);
        sunsetTxt = view.findViewById(R.id.sunset);
        windTxt = view.findViewById(R.id.wind);
        pressureTxt = view.findViewById(R.id.pressure);
        humidityTxt = view.findViewById(R.id.humidity);
        addressTxt.setText(global.address);
        updated_atTxt.setText(global.updatedAtText);
        statusTxt.setText(global.weatherDescription.toUpperCase());
        tempTxt.setText(global.temp);
        temp_minTxt.setText(global.tempMin);
        temp_maxTxt.setText(global.tempMax);
        sunriseTxt.setText(global.sunrise);
        sunsetTxt.setText(global.sunset);
        windTxt.setText(global.windSpeed);
        pressureTxt.setText(global.pressure);
        humidityTxt.setText(global.humidity);
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        if (global.English){

            label_sunrise.setText(getString(R.string.label_sunrise_en));
            label_sunset.setText(getString(R.string.label_sunset_en));
            label_wind.setText(getString(R.string.label_wind_en));
            label_humidity.setText(getString(R.string.label_humidity_en));
            label_pressure.setText(getString(R.string.label_pressure_en));
            label_createdby.setText(getString(R.string.label_creadedby_en));
        }
        else{
            label_sunrise.setText(getString(R.string.label_sunrise_it));
            label_sunset.setText(getString(R.string.label_sunset_it));
            label_wind.setText(getString(R.string.label_wind_it));
            label_humidity.setText(getString(R.string.label_humidity_it));
            label_pressure.setText(getString(R.string.label_pressure_it));
            label_createdby.setText(getString(R.string.label_creadedby_it));
        }

    }
}
