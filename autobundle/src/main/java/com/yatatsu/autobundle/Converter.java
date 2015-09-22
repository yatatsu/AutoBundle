package com.yatatsu.autobundle;


public interface Converter<Original, Stored> {
    Stored convert(Original o);
    Original original(Stored s);
}
