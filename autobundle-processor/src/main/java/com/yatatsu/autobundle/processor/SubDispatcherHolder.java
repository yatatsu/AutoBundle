package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
class SubDispatcherHolder {

    static List<SubDispatcherHolder> find(String classNames) {
        if (classNames == null || classNames.length() == 0) {
            return Collections.emptyList();
        }
        String[] classNameArray = classNames.split(",");
        return Stream.of(classNameArray)
                .map(SubDispatcherHolder::new)
                .collect(Collectors.toList());
    }

    private final ClassName name;

    private SubDispatcherHolder(String className) {
        name = ClassName.bestGuess(className);
    }

    ClassName getName() {
        return name;
    }
}
