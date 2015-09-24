package com.yatatsu.autobundle.example;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.yatatsu.autobundle.Arg;
import com.yatatsu.autobundle.AutoBundle;


public class ExampleIntentService extends IntentService {

    public ExampleIntentService() {
        super("ExampleIntentService");
    }

    @Arg(key = "message")
    String string;

    @Override
    protected void onHandleIntent(Intent intent) {
        AutoBundle.bind(this, intent);
        Log.d("ExampleIntentService", "message: " + string);
    }
}
