package mik.voice.siri.fragments;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mik.voice.siri.MainActivity;
import mik.voice.siri.R;
import mik.voice.siri.apps.AppAdapter;
import mik.voice.siri.apps.PInfo;
import mik.voice.siri.apps.PInfo_adapter;
import mik.voice.siri.global;

public class app_list extends Fragment {
    View view;
    TextView label;
    AppAdapter adapter;
    ListView similar_apps;
    ArrayList<PInfo> apps = new ArrayList<>();
    PInfo_adapter appadapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_app_list, container, false);
        label = view.findViewById(R.id.label);
        similar_apps = view.findViewById(R.id.similar_apps);
        get_installed_app();
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
    public void get_installed_app(){
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = MainActivity.getInstance().getPackageManager().queryIntentActivities( mainIntent, 0);
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
        List<PackageInfo> packs = MainActivity.getInstance().getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            PInfo newInfo = new PInfo(p.applicationInfo.loadLabel(MainActivity.getInstance().getPackageManager()).toString(),p.packageName,p.versionName, p.versionCode, p.applicationInfo.loadIcon(MainActivity.getInstance().getPackageManager()));
            res.add(newInfo);
        }
        return res;
    }
    public void inner_app_runner(){
//        weather_show(hide);

        PackageManager pm=MainActivity.getInstance().getPackageManager();
        Intent main=new Intent(Intent.ACTION_MAIN, null);
        main.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> launchables=pm.queryIntentActivities(main, 0);

        List<ResolveInfo> similar_launchables = pm.queryIntentActivities(main, 0);
        similar_launchables.clear();
        for (int i = 0; i < launchables.size(); i++){
            String name = launchables.get(i).loadLabel(pm).toString();
            if (name.toLowerCase().contains(global.speech_result.toLowerCase())){
                similar_launchables.add(launchables.get(i));
            }

        }
        Collections.sort(similar_launchables,
                new ResolveInfo.DisplayNameComparator(pm));

        adapter=new AppAdapter(MainActivity.getInstance(),pm, similar_launchables);
        if(similar_launchables.size() ==1){
            ResolveInfo launchable=adapter.getItem(0);
            ActivityInfo activity=launchable.activityInfo;
            ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
            Intent i=new Intent(Intent.ACTION_MAIN);

            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);
            startActivity(i);
        }
        else {
            appadapter = new PInfo_adapter(MainActivity.getInstance(), R.layout.item_apps, apps);
            similar_apps.setAdapter(adapter);
            similar_apps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ResolveInfo launchable=adapter.getItem(position);
                    ActivityInfo activity=launchable.activityInfo;
                    ComponentName name=new ComponentName(activity.applicationInfo.packageName,
                            activity.name);
                    Intent i=new Intent(Intent.ACTION_MAIN);

                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    i.setComponent(name);
                    startActivity(i);

//                        String package_name = s_apps.get(position).getPname();
//                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(package_name);
//                        if (launchIntent != null) {
//                            startActivity(launchIntent);//null pointer check in case package name was not found
//                        }
                }
            });
        }
    }

}
