package com.yatatsu.autobundle.example;

import android.os.Parcelable;

import com.yatatsu.autobundle.AutoBundleConverter;

import org.parceler.Parcels;


public final class ParcelableConverter implements AutoBundleConverter<Object, Parcelable> {

    @Override
    public Parcelable convert(Object o) {
        return Parcels.wrap(o);
    }

    @Override
    public Object original(Parcelable s) {
        return Parcels.unwrap(s);
    }
}
