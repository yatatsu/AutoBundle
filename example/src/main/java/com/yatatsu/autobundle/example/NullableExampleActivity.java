package com.yatatsu.autobundle.example;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
