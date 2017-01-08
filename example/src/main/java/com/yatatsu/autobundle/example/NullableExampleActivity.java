package com.yatatsu.autobundle.example;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yatatsu.autobundle.AutoBundleField;

/**
 * Example for @Nullable
 */
public class NullableExampleActivity extends AppCompatActivity {

    @Nullable @AutoBundleField
    String name;

    @Nullable @AutoBundleField
    Integer number;
}
