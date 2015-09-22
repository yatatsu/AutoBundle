package com.yatatsu.autobundle.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleTarget;

@AutoBundleTarget
public class FooBroadcastReceiver extends BroadcastReceiver {

    @AutoBundle
    String message;

    @Override
    public void onReceive(Context context, Intent intent) {
        FooBroadcastReceiverAutoBundle.bind(this, intent);
    }
}
