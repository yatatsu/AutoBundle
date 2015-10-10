package com.yatatsu.autobundle.example;

import org.parceler.Parcel;

@Parcel
public class ExampleData {

    public int id;
    public String message;

    public ExampleData() {

    }

    public ExampleData(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
