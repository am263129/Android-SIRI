package mik.voice.siri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.material.snackbar.Snackbar;
import com.newventuresoftware.waveform.WaveformView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mik.voice.siri.apps.PInfo;
import mik.voice.siri.apps.PInfo_adapter;

public class MainActivity extends AppCompatActivity {

    String CITY = "Tokyo";
    String API = "8118ed6ee68db2debfaaa5a44c832918";

    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, cityName;
    ImageView run;
    LinearLayout container_address, container_overview, container_details, container_apps;
    ListView similar_apps;
    RelativeLayout root_back;
    ImageView api_data;
    boolean Listenting = false;
    boolean show = true;
    boolean hide = false;
    ArrayList<PInfo> apps = new ArrayList<>();
    ArrayList<PInfo> s_apps = new ArrayList<>();

    private WaveformView mRealtimeWaveformView;
    private RecordingThread mRecordingThread;
    private static final int REQUEST_RECORD_AUDIO = 13;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        cityName = findViewById(R.id.edit_city_name);
        container_address = findViewById(R.id.addressContainer);
        container_overview = findViewById(R.id.overviewContainer);
        container_details = findViewById(R.id.detailsContainer);
        container_apps = findViewById(R.id.container_apps);
        similar_apps = findViewById(R.id.similar_apps);
        run = findViewById(R.id.run);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listenting = !Listenting;
                if (Listenting)
                    run.setBackgroundResource(R.drawable.btn_mic_active);
                else
                    run.setBackgroundResource(R.drawable.btn_mic);
                CITY = cityName.getText().toString();
                /**
                 * weather
                 */
                weather_show(show);
                try {
                    new weatherTask().execute();
                }
                catch (Exception E){
                    Log.e("error",E.toString());
                }
                /**
                 * inner Apps
                 */

            }
        });
        root_back = findViewById(R.id.root_back);

//        weather_show(hide);
        get_installed_app();
        AnimationDrawable animationDrawable = (AnimationDrawable) root_back.getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();

        api_data = findViewById(R.id.api_data);
        api_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, api_data.class);
                startActivity(intent);
            }
        });


        // wave animation
        mRealtimeWaveformView = (WaveformView) findViewById(R.id.waveformView);
        mRecordingThread = new RecordingThread(new AudioDataReceivedListener() {
            @Override
            public void onAudioDataReceived(short[] data) {
                mRealtimeWaveformView.setSamples(data);
            }
        });

        if (!mRecordingThread.recording()) {
            startAudioRecordingSafe();
        } else {
            mRecordingThread.stopRecording();
        }

        //end
    }
    @Override
    public void onResume() {

        super.onResume();
        set_init();
    }
    @Override
    protected void onStop() {
        super.onStop();

        mRecordingThread.stopRecording();
    }

    public void inner_app_runner(){
        weather_show(hide);
        s_apps.clear();
        for (Integer i = 0; i < apps.size(); i ++){
            if(apps.get(i).getAppname().toLowerCase().contains(cityName.getText().toString()))
                s_apps.add(apps.get(i));
        }

        if(s_apps.size()>0) {
            if(s_apps.size() ==1){
                String package_name = s_apps.get(0).getPname();
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage(package_name);
                if (launchIntent != null) {
                    startActivity(launchIntent);//null pointer check in case package name was not found
                }
            }
            else {
                PInfo_adapter adapter = new PInfo_adapter(MainActivity.this, R.layout.item_apps, s_apps);
                similar_apps.setAdapter(adapter);
                similar_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String package_name = s_apps.get(position).getPname();
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(package_name);
                        if (launchIntent != null) {
                            startActivity(launchIntent);//null pointer check in case package name was not found
                        }
                    }
                });
            }
        }
    }

    public void weather_show(boolean show){
        if(show){
            container_details.setVisibility(View.VISIBLE);
            container_address.setVisibility(View.VISIBLE);
            container_overview.setVisibility(View.VISIBLE);
            container_apps.setVisibility(View.GONE);
        }
        else{
            container_details.setVisibility(View.GONE);
            container_overview.setVisibility(View.GONE);
            container_address.setVisibility(View.GONE);
            container_apps.setVisibility(View.VISIBLE);
        }
    }
    public void set_init(){
        container_details.setVisibility(View.GONE);
        container_overview.setVisibility(View.GONE);
        container_address.setVisibility(View.GONE);
        container_apps.setVisibility(View.GONE);
    }

    public void get_installed_app(){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities( mainIntent, 0);
        apps.clear();
        apps = getPackages();
    }


    private ArrayList<PInfo> getPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).prettyPrint();
        }
        return apps;
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            PInfo newInfo = new PInfo(p.applicationInfo.loadLabel(getPackageManager()).toString(),p.packageName,p.versionName, p.versionCode, p.applicationInfo.loadIcon(getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            /* Showing the ProgressBar, Making the main design GONE */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
//            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            String response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");

                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                String address = jsonObj.getString("name") + ", " + sys.getString("country");


                /* Populating extracted data into our views */
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
//                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
//                CITY = "London";
//                new weatherTask().execute();
                inner_app_runner();
            }

        }
    }

    private void startAudioRecordingSafe() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.startRecording();
        } else {
            requestMicrophonePermission();
        }
    }

    private void requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
            // Show dialog explaining why we need record audio
            Snackbar.make(mRealtimeWaveformView, "Microphone access is required in order to record audio",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.stopRecording();
        }
    }
}
