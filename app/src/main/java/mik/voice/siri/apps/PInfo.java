package mik.voice.siri.apps;

import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class PInfo {
    private String appname = "";
    private String pname = "";
    private String versionName = "";
    private int versionCode = 0;
    private Drawable icon;

    public void prettyPrint() {
        Log.i("Here",appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
    }
    public PInfo(String appname, String pname, String versionName, int versionCode, Drawable icon){
        this.appname = appname;
        this.pname = pname;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.icon = icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getAppname() {
        return appname;
    }

    public String getPname() {
        return pname;
    }

    public String getVersionName() {
        return versionName;
    }

}

