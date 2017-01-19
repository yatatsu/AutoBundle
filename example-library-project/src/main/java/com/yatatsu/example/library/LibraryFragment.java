package com.yatatsu.example.library;

import android.support.v4.app.Fragment;

import com.yatatsu.autobundle.AutoBundleField;


public class LibraryFragment extends Fragment {

    @AutoBundleField(required = false) String title;
    @AutoBundleField(required = false) String message;
}
