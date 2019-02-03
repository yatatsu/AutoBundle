package com.yatatsu.autobundle.example;


import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import android.util.SparseArray;

import com.yatatsu.autobundle.AutoBundleField;

public class ValidFragment extends Fragment {

    @AutoBundleField
    int id;

    @AutoBundleField(required = false)
    String name;

    @AutoBundleField(key = "models")
    SparseArray<Parcelable> sparseArray;
}
