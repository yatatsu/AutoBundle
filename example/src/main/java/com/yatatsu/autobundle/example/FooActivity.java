package com.yatatsu.autobundle.example;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleTarget;

import java.util.ArrayList;


@AutoBundleTarget
public class FooActivity extends AppCompatActivity {

    @AutoBundle(required = false)
    String fooName;

    @AutoBundle(required = false)
    int[] fooIds;

    @AutoBundle
    ArrayList<CharSequence> fooList;

    @AutoBundle
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
