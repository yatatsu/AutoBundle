package com.yatatsu.autobundle.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.yatatsu.autobundle.Arg;
import com.yatatsu.autobundle.AutoBundle;

import java.util.ArrayList;


public class ExampleActivity extends AppCompatActivity {

    @Arg
    String name;

    @Arg(required = false)
    String optionalName;

    @Arg(required = false)
    ArrayList<CharSequence> fooList;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        setContentView(view);
        view.setText("");
        if (savedInstanceState != null) {
            AutoBundle.bind(this, savedInstanceState);
            Log.d("ExampleActivity", "savedInstanceState: " + savedInstanceState);
        } else {
            AutoBundle.bind(this, getIntent());
            Log.d("ExampleActivity", "intent: " + getIntent().getExtras());
        }
        setTitle(name);
        if (TextUtils.isEmpty(optionalName)) {
            view.setText(optionalName);
            Log.d("ExampleActivity", "optionalName: " + optionalName);
        }
        if (fooList != null) {
            for (CharSequence c : fooList) {
                view.setText(view.getText() + "\n" + c.toString());
                Log.d("ExampleActivity", "fooList: " + c);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        AutoBundle.pack(this, outState);
        Log.d("ExampleActivity", "outState: " + outState);
    }
}
