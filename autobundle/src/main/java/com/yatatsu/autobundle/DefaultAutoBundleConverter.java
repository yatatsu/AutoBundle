package com.yatatsu.autobundle;

public class DefaultAutoBundleConverter implements AutoBundleConverter<Object, Object> {

    @Override
    public Object convert(Object o) {
        return o;
    }

    @Override
    public Object original(Object s) {
        return s;
    }
}
