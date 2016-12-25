package com.yatatsu.autobundle.example;

import android.app.Activity;

import com.yatatsu.autobundle.AutoBundleField;


public class PrivateFieldWithoutGetterSetter extends Activity {

    @AutoBundleField(key = "key")
    private String name;
}
