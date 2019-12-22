package mik.voice.siri.apps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mik.voice.siri.R;

public class AppAdapter extends ArrayAdapter<ResolveInfo> {
    private PackageManager pm=null;
    List<ResolveInfo> array_apps;
    public AppAdapter(Context context, PackageManager pm, List<ResolveInfo> apps) {
        super(context, R.layout.item_apps, apps);
        this.pm=pm;
        array_apps = apps;
    }

    private void bindView(int position, View row) {
        TextView label=(TextView)row.findViewById(R.id.label);

        label.setText(getItem(position).loadLabel(pm));

        ImageView icon=(ImageView)row.findViewById(R.id.icon);

        icon.setImageDrawable(getItem(position).loadIcon(pm));
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

        app_icon.setImageDrawable(array_apps.get(position).loadIcon(pm));
        app_name.setText(array_apps.get(position).loadLabel(pm));
        app_name.setSelected(true);
//        app_version.setText(array_apps.get(position).getVersionName());

        return v;

    }
}