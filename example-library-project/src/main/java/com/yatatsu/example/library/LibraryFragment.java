package com.yatatsu.example.library;

import androidx.fragment.app.Fragment;

import com.yatatsu.autobundle.AutoBundleField;


public class LibraryFragment extends Fragment {

    @AutoBundleField(required = false) String title;
    @AutoBundleField(required = false) String message;
}
