package com.yatatsu.autobundle;


public interface AutoBundleConverter<Original, Stored> {
    Stored convert(Original o);
    Original original(Stored s);
}
