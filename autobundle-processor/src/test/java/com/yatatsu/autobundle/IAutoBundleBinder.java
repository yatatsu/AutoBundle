package com.yatatsu.autobundle;

import android.content.Intent;
import android.os.Bundle;

/**
 * fake for testing.
 */
public interface IAutoBundleBinder {
    void bind(Object target, Bundle args);
    void bind(Object target, Intent intent);
    void bind(Object target);
    void pack(Object source, Bundle args);
}