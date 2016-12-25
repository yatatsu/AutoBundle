package com.yatatsu.autobundle.example;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;

import com.yatatsu.autobundle.AutoBundleField;

import java.io.Serializable;
import java.util.ArrayList;


public class AllType extends Activity {

    @AutoBundleField
    boolean boolean1;

    @AutoBundleField
    boolean[] booleanArray1;

    @AutoBundleField
    byte byte1;

    @AutoBundleField
    byte[] byteArray1;

    @AutoBundleField
    char char1;

    @AutoBundleField
    char[] charArray1;

    @AutoBundleField
    CharSequence charSequence1;

    @AutoBundleField
    CharSequence[] charSequenceArray1;

    @AutoBundleField
    double double1;

    @AutoBundleField
    double[] doubleArray1;

    @AutoBundleField
    float float1;

    @AutoBundleField
    float[] floatArray1;

    @AutoBundleField
    int int1;

    @AutoBundleField
    int[] intArray1;

    @AutoBundleField
    long long1;

    @AutoBundleField
    long[] longArray1;

    @AutoBundleField
    short short1;

    @AutoBundleField
    short[] shortArray1;

    @AutoBundleField
    String string1;

    @AutoBundleField
    String[] stringArray1;

    @AutoBundleField
    Parcelable parcelable1;

    @AutoBundleField
    Parcelable[] parcelableArray1;

    @AutoBundleField
    Serializable serializable1;

    @AutoBundleField
    Bundle bundle1;

    @AutoBundleField
    ArrayList<Integer> integerArrayList1;

    @AutoBundleField
    ArrayList<String> stringArrayList1;

    @AutoBundleField
    ArrayList<CharSequence> charSequenceArrayList1;

    @AutoBundleField
    ArrayList<Parcelable> parcelableArrayList1;

    @AutoBundleField
    SparseArray<Parcelable> parcelableSparseArray1;
}
