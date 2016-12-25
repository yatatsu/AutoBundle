package com.yatatsu.autobundle.example;

import android.app.Activity;

import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleGetter;


public class PrivateAnnotatedGetter extends Activity {

    @AutoBundleField(key = "itemName")
    private String name;

    @AutoBundleGetter(key = "itemName")
    private String getName() {
        return name;
    }
}
