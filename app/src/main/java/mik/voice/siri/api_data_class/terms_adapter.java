package mik.voice.siri.api_data_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import mik.voice.siri.R;

public class terms_adapter extends ArrayAdapter<terms> {
    ArrayList<terms> array_terms = new ArrayList<>();
    rules task;
    public terms_adapter(Context context, int textViewResourceId, ArrayList<terms> objects) {
        super(context, textViewResourceId, objects);
        array_terms = objects;
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
        id.setText(array_terms.get(position).getId());
        description.setText(array_terms.get(position).getDescription());

        return v;

    }
}
