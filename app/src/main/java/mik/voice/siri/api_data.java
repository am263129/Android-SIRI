package mik.voice.siri;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import mik.voice.siri.api_data_class.instructions;
import mik.voice.siri.api_data_class.instructions_adapter;
import mik.voice.siri.api_data_class.rules;
import mik.voice.siri.api_data_class.rules_adapter;
import mik.voice.siri.api_data_class.terms;
import mik.voice.siri.api_data_class.terms_adapter;

public class api_data extends AppCompatActivity {

    private String token, user_id;
    private String REQUEST_AUTH = "http://192.168.208.34/apicore/auth/login";
    private String REQUEST_RULES = "http://192.168.208.34/apicore/rules/index";
    private String REQUEST_INSTRUCTIONS = "http://192.168.208.34/apicore/instructions/index";
    private String REQUEST_TERMS_CONDITIONS = "http://192.168.208.34/apicore/TermsAndConditions";

    private ArrayList<rules> rules_list = new ArrayList<>();
    private ArrayList<instructions> instructions_list = new ArrayList<>();
    private ArrayList<terms> terms_list = new ArrayList<>();

    private Handler mHandler = new Handler();
    ListView list_rules, list_instructions, list_terms;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_data);

        LinearLayout api_back = findViewById(R.id.api_back);
        list_instructions = findViewById(R.id.list_instructions);
        list_rules = findViewById(R.id.list_rules);
        list_terms = findViewById(R.id.list_terms);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading Data...");
        dialog.setCancelable(false);
        dialog.show();
        AnimationDrawable animationDrawable = (AnimationDrawable) api_back.getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        sendPost();

    }
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            view_result();
            mHandler.postDelayed(mStatusChecker, 1000);
        }
    };
    public void sendPost() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(REQUEST_AUTH);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                    conn.setRequestProperty("Accept","application/json");
                    conn.setRequestProperty("Auth-Key","simplerestapi");
                    conn.setRequestProperty("Client-Service","frontend-client");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("username", "admin");
                    jsonParam.put("password", "Admin123$");

                    Log.i("JSON", jsonParam.toString());
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(jsonParam.toString());

                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    //Parse the response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    String json = sb.toString();
                    //Convert the response to JSON Object
                    JSONObject jObj = new JSONObject(json);
                    user_id = jObj.get("id").toString();
                    token = jObj.get("token").toString();
                    Log.i("MSG" , jObj.get("token").toString());
                    conn.disconnect();
                    check_get();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("Error",e.toString());
                }
            }
        });

        thread.start();
    }

    private void check_get() {
        if (user_id != null && token != null){
            sendGet(REQUEST_RULES,"rules");
            sendGet(REQUEST_INSTRUCTIONS, "instructions");
            sendGet(REQUEST_TERMS_CONDITIONS , "terms");
            dialog.dismiss();
            mStatusChecker.run();
        }

    }
    private void view_result() {
        try {

            rules_adapter adt_rules = new rules_adapter(this, R.layout.item_api_data, rules_list);
            instructions_adapter adt_instructions = new instructions_adapter(this, R.layout.item_api_data, instructions_list);
            terms_adapter adt_terms = new terms_adapter(this, R.layout.item_api_data, terms_list);
            list_terms.setAdapter(adt_terms);
            list_rules.setAdapter(adt_rules);
            list_instructions.setAdapter(adt_instructions);
            set_height(list_rules, adt_rules);
            set_height(list_terms, adt_terms);
            set_height(list_instructions, adt_instructions);
        }
        catch (Exception e){
            Log.e("error", e.toString());
        }

    }
    private void set_height(ListView view, ArrayAdapter adapter){
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.AT_MOST);
        int item_height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, view);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
            item_height = listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params = view.getLayoutParams();
        params.height = (int)totalHeight/3 + item_height + 10;
        view.setLayoutParams(params);
    }

    public void sendGet(final String request_url, final String class_name){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(request_url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Auth-Key","simplerestapi");
                    conn.setRequestProperty("Client-Service","frontend-client");
                    conn.setRequestProperty("user-id",user_id);
                    conn.setRequestProperty("authorization",token);

                    Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder sb = new StringBuilder();
                    String line;

                    //Parse the response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    String json = sb.toString().replace("[","").replace("]","");
                    Log.i("Res",json);
                    if (json.split("[}],").length > 0) {
                        for (int i = 0; i < json.split("[}],").length; i++) {
                            String temp = json.split("[}],")[i];
                            if (i != json.split("[}],").length -1)
                                temp += "}";
                            JSONObject jObj = new JSONObject(temp);
                            String id = jObj.get("id").toString();
                            String description = jObj.get("description").toString().replaceFirst("\\s++$", "");
                            Log.i("id",jObj.get("id").toString());
                            Log.i("desc",jObj.get("description").toString());
                            switch (class_name){
                                case "rules":
                                    rules_list.add(new rules(id, description));
                                    break;
                                case "instructions":
                                    instructions_list.add(new instructions(id, description));
                                    break;
                                case "terms":
                                    terms_list.add(new terms(id, description));
                                    break;
                            }
                        }
                    }

                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
