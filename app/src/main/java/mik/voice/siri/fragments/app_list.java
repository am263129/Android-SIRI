package mik.voice.siri.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

import mik.voice.siri.R;
import mik.voice.siri.global;

public class app_list extends Fragment {
    View view;
    TextView label;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app_list, container, false);
        label = view.findViewById(R.id.label);
        return view;
    }
    @Override
    public void onResume(){
        super.onResume();
        if (global.English){
            label.setText(getString(R.string.label_app_list_en));

        }
        else{
            label.setText(getString(R.string.label_app_list_it));

        }

    }

}
