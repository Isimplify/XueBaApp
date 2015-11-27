package com.dream.Update;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.widget.Toast;

/**
 * Created by 夏目 on 2015/11/9.
 */
public class GetBroadcast extends BroadcastReceiver {
    private static GetBroadcast mReceiver = new GetBroadcast();
    private static IntentFilter mIntentFilter;

    public static void registerReceiver(Context context) {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addDataScheme("package");
        mIntentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        // mIntentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        // mIntentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        // context.registerReceiver(mReceiver, mIntentFilter);
    }

    public static void unregisterReceiver(Context context) {
        context.unregisterReceiver(mReceiver);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String packageName = intent.getDataString().substring(8);
        if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
            Toast.makeText(context, "完成更新！", Toast.LENGTH_LONG).show();
            PackageManager pm = context.getPackageManager();
            Intent intent1 = new Intent();
            intent1 = pm.getLaunchIntentForPackage(packageName);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
            unregisterReceiver(context);
        } // else if (Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
        // // Toast.makeText(context, "有应用被删除", Toast.LENGTH_LONG).show();
        // } else if (Intent.ACTION_PACKAGE_REPLACED.equals(action)) {
        // // Toast.makeText(context, "有应用被替换", Toast.LENGTH_LONG).show();
        // }
    }
}
