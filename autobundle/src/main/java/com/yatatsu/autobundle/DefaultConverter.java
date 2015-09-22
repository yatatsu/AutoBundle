package com.yatatsu.autobundle;

public class DefaultConverter implements Converter<Object, Object> {

    @Override
    public Object convert(Object o) {
        return o;
    }

    @Override
    public Object original(Object s) {
        return s;
    }
}
