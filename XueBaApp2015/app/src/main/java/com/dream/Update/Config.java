package com.dream.Update;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import com.dream.XueBaApp2015.R;

/**
 * Created by 夏目 on 2015/11/8.
 */
public class Config {
    private static final String TAG = "Config";

    public static final String UPDATE_SERVER = "http://10.2.26.62:80/FileAffairs/upload/";
    public static final String UPDATE_APKNAME = "XueBa.apk";
    public static final String UPDATE_VERJSON = "ver.json";
    public static final String UPDATE_SAVENAME = "XueBa_update.apk";

    public static int getVerCode(Context context) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(
                    "com.example.xueba", 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().getPackageInfo(
                    "com.example.xueba", 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }
        return verName;

    }

    public static String getAppName(Context context) {
        String verName = context.getResources().getText(R.string.app_name)
                .toString();
        return verName;
    }
}
