package com.yatatsu.autobundle.processor.data;

/**
 * Created by kitagawatatsuya on 2015/09/23.
 */
public interface SourceBase {
    String getTargetClassName();
    String getTargetSource();
    String getExpectClassName();
    String getExpectSource();
}
