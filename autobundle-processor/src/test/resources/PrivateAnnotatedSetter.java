package com.yatatsu.autobundle.example;

import android.app.Activity;

import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleSetter;


public class PrivateAnnotatedSetter extends Activity {

    @AutoBundleField(key = "itemName")
    private String name;

    @AutoBundleSetter(key = "itemName")
    private void setName(String name) {
        this.name = name;
    }
}
