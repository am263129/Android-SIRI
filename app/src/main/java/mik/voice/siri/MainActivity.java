package mik.voice.siri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androdocs.httprequest.HttpRequest;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.newventuresoftware.waveform.WaveformView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mik.voice.siri.apps.AppAdapter;
import mik.voice.siri.apps.PInfo;
import mik.voice.siri.apps.PInfo_adapter;
import mik.voice.siri.fragments.app_list;
import mik.voice.siri.fragments.calculator;
import mik.voice.siri.fragments.weather;
import mik.voice.siri.utils.tool_functions;

import static mik.voice.siri.global.ID_FLAG;
import static mik.voice.siri.global.S_LANGUAGE;

public class MainActivity extends AppCompatActivity implements RecognitionListener {


    String CITY = "Tokyo";
    String API = "8118ed6ee68db2debfaaa5a44c832918";
    TextView  cityName;
    ImageView run;
    LinearLayout container_apps;

    RelativeLayout root_back;
    ImageView setting;
    boolean Listenting = false;
    boolean show = true;
    boolean hide = false;

    ArrayList<PInfo> s_apps = new ArrayList<>();
    private WaveformView mRealtimeWaveformView;
    private RecordingThread mRecordingThread;
    private static final int REQUEST_RECORD_AUDIO = 13;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    Dialog dialog;
    Integer MY_PERMISSIONS_REQUEST_READ_STATE = 9003;
    Button test_1,test_2,test_3,test_4;
    public static MainActivity self;
    AlarmManager alarmMgr;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        self = this;
        get_setting();
        check_permission();
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        cityName = findViewById(R.id.edit_city_name);
        container_apps = findViewById(R.id.container_apps);

        run = findViewById(R.id.run);
        test_1 = findViewById(R.id.button);
        test_2 = findViewById(R.id.button2);
        test_3 = findViewById(R.id.button3);
        test_4 = findViewById(R.id.button4);
        test_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_weathre_data();
            }
        });
        test_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new calculator());
            }
        });
        test_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String escapedQuery = null;
                try {
                    escapedQuery = URLEncoder.encode("Calculator", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        test_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=restaurants");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Listenting = !Listenting;
                if (Listenting) {
                    run.setBackgroundResource(R.drawable.btn_mic_active);
                    promptSpeechInput(true);
                }
                else
                    run.setBackgroundResource(R.drawable.btn_mic);

            }
        });
        root_back = findViewById(R.id.root_back);

        AnimationDrawable animationDrawable = (AnimationDrawable) root_back.getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.menu_setting:
                        intent = new Intent(MainActivity.this, setting.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_about:
                        intent = new Intent(MainActivity.this, api_data.class);
                        startActivity(intent);
                        return true;
                    case R.id.menu_mytask:
                        return true;
                    default:
                        return true;
                }
            }
        });


        dialog = new Dialog(this);
        // wave animation
//        set_animation();


    }

    private void get_setting() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(ID_FLAG, MODE_PRIVATE);
        global.English = pref.getBoolean(S_LANGUAGE,true);

    }

    private void check_permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_READ_STATE);
            } else {
                return;
            }
        } else {
            int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

            if (permission == PermissionChecker.PERMISSION_GRANTED) {
                return;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_READ_STATE);
            }
        }
    }

    private void loadFragment(Fragment fragment) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.mainFragment, fragment);
        fragmentTransaction.commit();
    }



    private void set_animation(){
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
    }

    private void promptSpeechInput(boolean dialog) {
        SpeechRecognizer speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizer.setRecognitionListener(this);

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,Locale.getDefault());
//        if(global.English) {
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
//        }
//        else{
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "it-IT");
//        }
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        speechRecognizer.startListening(intent);
        if (dialog){
            show_shadow_dialog();
        }
//        try {
//            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
////            show_shadow_dialog();
//        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(),
//                    getString(R.string.speech_not_supported),
//                    Toast.LENGTH_SHORT).show();
//        }
    }

    private void show_shadow_dialog() {

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.shadow_dialog);
        dialog.setCanceledOnTouchOutside(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        LinearLayout back = dialog.findViewById(R.id.dlg_back);
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.9f);
        FrameLayout.LayoutParams paramsLcl = (FrameLayout.LayoutParams)back.getLayoutParams();
        paramsLcl.width = widthLcl;
        TextView message = (TextView) dialog.findViewById(R.id.message);
        if(global.English) {
            message.setText(getString(R.string.message_en));
        }
        else
        {
            message.setText(getString(R.string.message_it));
        }
        back.setLayoutParams(paramsLcl);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Listenting = !Listenting;
                run.setBackgroundResource(R.drawable.btn_mic);
            }
        });
        dialog.show();
    }


    @Override
    protected void onStop() {
        super.onStop();

//        mRecordingThread.stopRecording();
    }



//    public void weather_show(boolean show){
//        if(show){
//            container_details.setVisibility(View.VISIBLE);
//            container_address.setVisibility(View.VISIBLE);
//            container_overview.setVisibility(View.VISIBLE);
//            container_apps.setVisibility(View.GONE);
//        }
//        else{
//            container_details.setVisibility(View.GONE);
//            container_overview.setVisibility(View.GONE);
//            container_address.setVisibility(View.GONE);
//            container_apps.setVisibility(View.VISIBLE);
//        }
//    }
//    public void set_init(){
//        container_details.setVisibility(View.GONE);
//        container_overview.setVisibility(View.GONE);
//        container_address.setVisibility(View.GONE);
//        container_apps.setVisibility(View.GONE);
//    }








    public String word_to_num(String input){
        boolean isValidInput = true;
        long result = 0;
        long finalResult = 0;
        List<String> allowedStrings = Arrays.asList
                (
                        "zero","one","two","three","four","five","six","seven",
                        "eight","nine","ten","eleven","twelve","thirteen","fourteen",
                        "fifteen","sixteen","seventeen","eighteen","nineteen","twenty",
                        "thirty","forty","fifty","sixty","seventy","eighty","ninety",
                        "hundred","thousand","million","billion","trillion"
                );
        if(input != null && input.length()> 0)
        {
            input = input.replaceAll("-", " ");
            input = input.toLowerCase().replaceAll(" and", " ");
            String[] splittedParts = input.trim().split("\\s+");

            for(String str : splittedParts)
            {
                if(!allowedStrings.contains(str))
                {
                    isValidInput = false;
                    System.out.println("Invalid word found : "+str);
                    break;
                }
            }
            if(isValidInput)
            {
                for(String str : splittedParts)
                {
                    if(str.equalsIgnoreCase("zero")) {
                        result += 0;
                    }
                    else if(str.equalsIgnoreCase("one")) {
                        result += 1;
                    }
                    else if(str.equalsIgnoreCase("two")) {
                        result += 2;
                    }
                    else if(str.equalsIgnoreCase("three")) {
                        result += 3;
                    }
                    else if(str.equalsIgnoreCase("four")) {
                        result += 4;
                    }
                    else if(str.equalsIgnoreCase("five")) {
                        result += 5;
                    }
                    else if(str.equalsIgnoreCase("six")) {
                        result += 6;
                    }
                    else if(str.equalsIgnoreCase("seven")) {
                        result += 7;
                    }
                    else if(str.equalsIgnoreCase("eight")) {
                        result += 8;
                    }
                    else if(str.equalsIgnoreCase("nine")) {
                        result += 9;
                    }
                    else if(str.equalsIgnoreCase("ten")) {
                        result += 10;
                    }
                    else if(str.equalsIgnoreCase("eleven")) {
                        result += 11;
                    }
                    else if(str.equalsIgnoreCase("twelve")) {
                        result += 12;
                    }
                    else if(str.equalsIgnoreCase("thirteen")) {
                        result += 13;
                    }
                    else if(str.equalsIgnoreCase("fourteen")) {
                        result += 14;
                    }
                    else if(str.equalsIgnoreCase("fifteen")) {
                        result += 15;
                    }
                    else if(str.equalsIgnoreCase("sixteen")) {
                        result += 16;
                    }
                    else if(str.equalsIgnoreCase("seventeen")) {
                        result += 17;
                    }
                    else if(str.equalsIgnoreCase("eighteen")) {
                        result += 18;
                    }
                    else if(str.equalsIgnoreCase("nineteen")) {
                        result += 19;
                    }
                    else if(str.equalsIgnoreCase("twenty")) {
                        result += 20;
                    }
                    else if(str.equalsIgnoreCase("thirty")) {
                        result += 30;
                    }
                    else if(str.equalsIgnoreCase("forty")) {
                        result += 40;
                    }
                    else if(str.equalsIgnoreCase("fifty")) {
                        result += 50;
                    }
                    else if(str.equalsIgnoreCase("sixty")) {
                        result += 60;
                    }
                    else if(str.equalsIgnoreCase("seventy")) {
                        result += 70;
                    }
                    else if(str.equalsIgnoreCase("eighty")) {
                        result += 80;
                    }
                    else if(str.equalsIgnoreCase("ninety")) {
                        result += 90;
                    }
                    else if(str.equalsIgnoreCase("hundred")) {
                        result *= 100;
                    }
                    else if(str.equalsIgnoreCase("thousand")) {
                        result *= 1000;
                        finalResult += result;
                        result=0;
                    }
                    else if(str.equalsIgnoreCase("million")) {
                        result *= 1000000;
                        finalResult += result;
                        result=0;
                    }
                    else if(str.equalsIgnoreCase("billion")) {
                        result *= 1000000000;
                        finalResult += result;
                        result=0;
                    }
                    else if(str.equalsIgnoreCase("trillion")) {
                        result *= 1000000000000L;
                        finalResult += result;
                        result=0;
                    }
                }

                finalResult += result;
                result=0;

            }
        }
        return String.valueOf(finalResult);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.e("ss","something comeon");
    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.e("ss","something comeon");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.e("ss","something comeon");

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {
        Toast.makeText(this,String.valueOf(error),Toast.LENGTH_LONG).show();
        dialog.dismiss();
    }

    @Override
    public void onResults(Bundle results) {
        dialog.dismiss();
        ArrayList<String> result = new ArrayList(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION));
        CITY = result.get(0);
        cityName.setText(result.get(0).toString());
        Integer Action = 0;

        if(global.CALCULATOR){
            if(result.get(0).toString().equals("exit") || result.get(0).toString().equals("quit")){
                global.CALCULATOR = false;
                promptSpeechInput(true);
            }
            else if(result.get(0).toString().equals("clear") || result.get(0).toString().equals("back") || result.get(0).toString().equals("add") || result.get(0).toString().equals("sub") || result.get(0).toString().equals("div") || result.get(0).toString().equals("mul"))
            calculator.speech_calculate(result.get(0).toString());

            else{
               String num = word_to_num(result.get(0).toString());
               for (int i = 0; i< num.length(); i ++){
                   calculator.speech_calculate(num.substring(i,i+1));
               }
            }
        }
        else {
            if (result.get(0).toString().equals("calculator")) {

                promptSpeechInput(false);
                loadFragment(new calculator());
                global.CALCULATOR = true;
            }
            else if (result.get(0).toString().indexOf("open map") >= 0) {
                global.CALCULATOR = false;
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/(%s)", "Where the party is at");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }

            else if (result.get(0).toString().indexOf("open cam") >= 0) {
                global.CALCULATOR = false;
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivity(intent);
            }
            else if (result.get(0).toString().indexOf("set alarm") >= 0) {
                global.CALCULATOR = false;
                String time = result.get(0).toString().split("on")[1];
                boolean PM = false;
                if (time.indexOf("pm") > 0) {
                    PM = true;
                }
                String num_time = tool_functions.get_time(time);
                Integer Hour = Integer.parseInt(num_time.split(":")[0]);
                if (PM) {
                    Hour += 12;
                }
                Integer Minute = Integer.parseInt(num_time.split(":")[1]);
                Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
                i.putExtra(AlarmClock.EXTRA_HOUR, Hour);
                i.putExtra(AlarmClock.EXTRA_MINUTES, Minute);
                startActivity(i);

            }
            else if ((result.get(0).toString().indexOf("search") >= 0) || (result.get(0).toString().indexOf("what is") >= 0) || (result.get(0).toString().indexOf("who is") >= 0) || (result.get(0).toString().indexOf("find me") >= 0)) {
                String substr = "";
                global.CALCULATOR = false;
                if (result.get(0).toString().indexOf("search") >= 0) {
                    substr = result.get(0).toString().substring(result.get(0).toString().indexOf("search") + 6);
                } else if (result.get(0).toString().indexOf("what is") >= 0) {
                    substr = result.get(0).toString().substring(result.get(0).toString().indexOf("what is") + 7);
                } else if (result.get(0).toString().indexOf("who is") >= 0) {
                    substr = result.get(0).toString().substring(result.get(0).toString().indexOf("who is") + 6);
                } else {
                    substr = result.get(0).toString().substring(result.get(0).toString().indexOf("find me") + 7);
                }

                String escapedQuery = null;
                try {
                    escapedQuery = URLEncoder.encode(substr, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

            else{
                get_weathre_data();
            }


        }
    }
    public void get_weathre_data(){
        global.CALCULATOR = false;
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            Toast.makeText(this, "Network is OK", Toast.LENGTH_LONG).show();
//            weather_show(show);
            try {
                new weatherTask().execute();
            } catch (Exception E) {
                Log.e("error", E.toString());
                Toast.makeText(this, "Error " + E.toString(), Toast.LENGTH_LONG).show();
            }
//                        inner_app_runner();

        } else {
            Toast.makeText(this, "Network Error", Toast.LENGTH_LONG).show();
            loadFragment(new app_list());
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

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
                global.address = address;
                global.updatedAtText = updatedAtText;
                global.weatherDescription = weatherDescription.toUpperCase();
                global.temp = temp;
                global.tempMin = tempMin;
                global.tempMax = tempMax;
                global.sunrise = (new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                global.sunset = (new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                global.windSpeed = windSpeed;
                global.pressure = pressure;
                global.humidity = humidity;

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);

                loadFragment(new weather());
                ////show weather fragment_answer!


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this,"Post Error"+e.toString(), Toast.LENGTH_LONG).show();
                findViewById(R.id.loader).setVisibility(View.GONE);


                ////show app list!
                loadFragment(new app_list());
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

    public static MainActivity getInstance(){
        return self;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RECORD_AUDIO && grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mRecordingThread.stopRecording();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                run.setBackgroundResource(R.drawable.btn_mic);
                Listenting = false;
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    CITY = result.get(0);
                    cityName.setText(result.get(0).toString());

                    ConnectivityManager ConnectionManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo=ConnectionManager.getActiveNetworkInfo();
                    if(networkInfo != null && networkInfo.isConnected()==true )
                    {
                        Toast.makeText(this, "Network is OK", Toast.LENGTH_LONG).show();
//                        weather_show(show);
                        try {
                            new weatherTask().execute();
                        }
                        catch (Exception E){
                            Log.e("error",E.toString());
                            Toast.makeText(this,"Error "+E.toString(),Toast.LENGTH_LONG).show();
                        }
//                        inner_app_runner();

                    }
                    else
                    {
                        Toast.makeText(this,"Network Error",Toast.LENGTH_LONG).show();
                        loadFragment(new app_list());
                    }
                    /**
                     * weather
                     */

                }
                break;
            }

        }
    }
}
