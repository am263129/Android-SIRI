package mik.voice.siri.api_data_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mik.voice.siri.R;

public class rules_adapter extends ArrayAdapter<rules> {
    ArrayList<rules> array_rules = new ArrayList<>();
    rules task;
    public rules_adapter(Context context, int textViewResourceId, ArrayList<rules> objects) {
        super(context, textViewResourceId, objects);
        array_rules = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.item_api_data, null);

        TextView id = (TextView)v.findViewById(R.id.id);
        TextView description = (TextView)v.findViewById(R.id.descriptions);
        id.setText(array_rules.get(position).getId());
        description.setText(array_rules.get(position).getDescription());

        return v;

    }
}
