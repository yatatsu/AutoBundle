package com.yatatsu.autobundle.example;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.yatatsu.autobundle.AutoBundle;
import com.yatatsu.autobundle.AutoBundleTarget;
import com.yatatsu.autobundle.Converter;

import java.util.Date;

@AutoBundleTarget
public class SomeFragment extends Fragment {

    @AutoBundle(key = "hey", required = false)
    int someId;

    @AutoBundle
    String myTitle;

    @AutoBundle(converter = DateArgConverter.class)
    Date targetDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SomeFragmentAutoBundle.bind(this, getArguments());
    }

    public static class DateArgConverter implements Converter<Date, Long> {

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
