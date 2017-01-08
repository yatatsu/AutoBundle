package com.yatatsu.autobundle.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button button1 = (Button) findViewById(R.id.button_activity);
        button1.setOnClickListener(this);
        Button button2 = (Button) findViewById(R.id.button_fragment);
        button2.setOnClickListener(this);
        Button button3 = (Button) findViewById(R.id.button_receiver);
        button3.setOnClickListener(this);
        Button button4 = (Button) findViewById(R.id.button_service);
        button4.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_activity:
                startExampleActivity();
                break;
            case R.id.button_fragment:
                startExampleFragment();
                break;
            case R.id.button_receiver:
                startExampleReceiver();
                break;
            case R.id.button_service:
                startExampleService();
                break;
        }
    }

    void startExampleActivity() {
        ArrayList<CharSequence> messages = new ArrayList<>();
        messages.add("hey");
        messages.add("this is");
        messages.add("messages");
        Intent intent = ExampleActivityAutoBundle
                .builder(ExampleActivity.TYPE_A, "hello, example!")
                .optionalName("optionalName")
                .fooList(messages)
                .exampleData(new ExampleData(100, "one hundred"))
                .persons(Person.examples())
                .build(this)
                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    void startExampleFragment() {
        ExampleFragment fragment = ExampleFragmentAutoBundle
                .builder("hello, example!", new Date())
                .build();
        fragment.show(getSupportFragmentManager(), "ExampleFragment");
    }

    void startExampleReceiver() {
        startService(ExampleIntentServiceAutoBundle
                .builder("hello, example!")
                .build(this));
    }

    void startExampleService() {
        sendBroadcast(ExampleBroadcastReceiverAutoBundle
                .builder("hello, example!")
                .build(this));
    }
}
