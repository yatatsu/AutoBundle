package com.yatatsu.autobundle.example;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.yatatsu.autobundle.Arg;
import com.yatatsu.autobundle.AutoBundleTarget;

import java.util.ArrayList;


@AutoBundleTarget
public class FooActivity extends AppCompatActivity {

    @Arg(required = false)
    String fooName;

    @Arg(required = false)
    int[] fooIds;

    @Arg
    ArrayList<CharSequence> fooList;

    @Arg
    SparseArray<Parcelable> array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FooActivityAutoBundle.bind(this, getIntent());
        if (savedInstanceState != null) {
            FooActivityAutoBundle.bind(this, savedInstanceState);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FooActivityAutoBundle.pack(this, outState);
    }
}
