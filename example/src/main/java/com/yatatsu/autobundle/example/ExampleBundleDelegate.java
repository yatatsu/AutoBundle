package com.yatatsu.autobundle.example;


import com.yatatsu.autobundle.AutoBundleField;

import java.util.ArrayList;

public class ExampleBundleDelegate {

    @AutoBundleField String name;

    @AutoBundleField(required = false) int number;

    @AutoBundleField(required = false) ArrayList<String> list;
}
