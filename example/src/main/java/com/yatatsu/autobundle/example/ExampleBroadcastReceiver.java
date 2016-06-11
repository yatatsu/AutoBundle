package com.yatatsu.autobundle.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleField;

public class ExampleBroadcastReceiver extends BroadcastReceiver {

    @AutoBundleField
    String message;

    @Override
    public void onReceive(Context context, Intent intent) {
        AutoBundle.bind(this, intent);
        Log.d("BroadcastReceiver", "message: " + message);
    }
}
