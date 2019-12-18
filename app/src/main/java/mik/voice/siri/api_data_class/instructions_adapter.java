package mik.voice.siri.api_data_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mik.voice.siri.R;

public class instructions_adapter extends ArrayAdapter<instructions> {
    ArrayList<instructions> array_instructions = new ArrayList<>();
    rules task;
    public instructions_adapter(Context context, int textViewResourceId, ArrayList<instructions> objects) {
        super(context, textViewResourceId, objects);
        array_instructions = objects;
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
        id.setText(array_instructions.get(position).getId());
        description.setText(array_instructions.get(position).getDescription());

        return v;

    }
}
