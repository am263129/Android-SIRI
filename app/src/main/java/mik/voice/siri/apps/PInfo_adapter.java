package mik.voice.siri.apps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import mik.voice.siri.R;
import mik.voice.siri.api_data_class.rules;
import mik.voice.siri.api_data_class.terms;

public class PInfo_adapter extends ArrayAdapter<PInfo> {
    ArrayList<PInfo> array_apps = new ArrayList<>();
    rules task;
    public PInfo_adapter(Context context, int textViewResourceId, ArrayList<PInfo> objects) {
        super(context, textViewResourceId, objects);
        array_apps = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_apps, null);

        ImageView app_icon = v.findViewById(R.id.img_appicon);
        TextView app_name = v.findViewById(R.id.label_appname);
        TextView app_version = v.findViewById(R.id.label_version);

        app_icon.setImageDrawable(array_apps.get(position).getIcon());
        app_name.setText(array_apps.get(position).getAppname());
        app_version.setText(array_apps.get(position).getVersionName());

        return v;

    }
}
