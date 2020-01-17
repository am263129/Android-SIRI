package mik.voice.siri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static mik.voice.siri.global.ID_FLAG;
import static mik.voice.siri.global.S_LANGUAGE;

public class setting extends AppCompatActivity implements View.OnClickListener {

    ImageView btn_info, lan_us, lan_it;
    CheckBox en, it;
    TextView label_language;
    boolean English;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        LinearLayout setting_back = findViewById(R.id.setting_back);
        btn_info = findViewById(R.id.btn_api_data);
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(setting.this, api_data.class);
                startActivity(intent);
            }
        });
        lan_us = findViewById(R.id.lan_us);
        lan_it = findViewById(R.id.lan_it);
        en = findViewById(R.id.en);
        it = findViewById(R.id.it);
        label_language = findViewById(R.id.label_language);
        en.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    set_english();
                }
            }
        });
        it.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    set_italian();
                }
            }
        });
        lan_it.setOnClickListener(this);
        lan_us.setOnClickListener(this);
        AnimationDrawable animationDrawable = (AnimationDrawable) setting_back.getBackground();
        animationDrawable.setEnterFadeDuration(10);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
        English = global.English;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(global.English){
            label_language.setText(MainActivity.getInstance().getString(R.string.label_language_setting_en));
        }
        else{
            label_language.setText(MainActivity.getInstance().getString(R.string.label_language_setting_it));
        }
    }

    protected void set_english(){
        en.setChecked(true);
        it.setChecked(false);
        English = true;
    }
    protected void set_italian(){
        en.setChecked(false);
        it.setChecked(true);
        English = false;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lan_us:
                set_english();
                break;
            case R.id.lan_it:
                set_italian();
                break;

        }
    }
    @Override
    public void onBackPressed(){

        if (English != global.English) {
            if (global.English) {
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage(MainActivity.getInstance().getString(R.string.msg_alert_en))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                save_setting();
                                finish();
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage(MainActivity.getInstance().getString(R.string.msg_alert_it))

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(MainActivity.getInstance().getString(R.string.yes_it), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                save_setting();
                                finish();
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        else
            finish();
    }

    private void save_setting() {
        global.English = English;
        SharedPreferences pref = getApplicationContext().getSharedPreferences(ID_FLAG, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(S_LANGUAGE,English);
        editor.apply();
    }
}
