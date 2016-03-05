package com.yatatsu.autobundle.example;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.yatatsu.autobundle.AutoBundleField;


public class ExampleIntentService extends IntentService {

    public ExampleIntentService() {
        super("ExampleIntentService");
    }

    @AutoBundleField(key = "message")
    String string;

    @Override
    protected void onHandleIntent(Intent intent) {
        ExampleIntentServiceAutoBundle.bind(this, intent);
        Log.d("ExampleIntentService", "message: " + string);
    }
}
