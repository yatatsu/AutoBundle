package com.yatatsu.autobundle.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleTarget;

@AutoBundleTarget
public class SomeFragment extends Fragment {

    @AutoBundle(key = "hey", required = false)
    int someId;

    @AutoBundle
    String myTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SomeFragmentAutoBundle.bind(this, getArguments());
    }

}
