package com.yatatsu.autobundle.example;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleConverter;
import com.yatatsu.autobundle.AutoBundleField;

import java.util.Date;

public class ExampleFragment extends DialogFragment {

    @AutoBundleField
    String title;

    @AutoBundleField(converter = DateArgConverter.class)
    Date targetDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoBundle.bind(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // restore from savedInstanceState.
            AutoBundle.bind(this, savedInstanceState);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setMessage("date is " + targetDate.toString());
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        title = String.format("%s!", title);
        // store state
        AutoBundle.pack(this, outState);
    }

    public static class DateArgConverter implements AutoBundleConverter<Date, Long> {

        @Override
        public Long convert(Date o) {
            return o.getTime();
        }

        @Override
        public Date original(Long s) {
            return new Date(s);
        }
    }
}
