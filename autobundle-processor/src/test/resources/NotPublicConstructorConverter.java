package com.yatatsu.autobundle.example;

import android.app.Activity;

import com.yatatsu.autobundle.AutoBundleConverter;
import com.yatatsu.autobundle.AutoBundleField;

import java.util.Date;


public class NotPublicConstructorConverter extends Activity {

    @AutoBundleField(converter = DateArgConverter.class)
    Date date;

    public static class DateArgConverter implements AutoBundleConverter<Date, Long> {

        private DateArgConverter() {}

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
