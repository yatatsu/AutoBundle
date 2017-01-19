package com.yatatsu.autobundle;

import android.os.Bundle;

/**
 * Dispatcher interface
 */
public interface AutoBundleDispatcher {

    /**
     * bind target with bundle
     *
     * @param target binding target
     * @param args bundle
     * @return true if binding match
     */
    boolean bind(Object target, Bundle args);

    /**
     * shorthand for {@link AutoBundleDispatcher#bind(Object, Bundle)}
     *
     * @param target may be fragment or compat
     * @return true if binding match
     */
    boolean bind(Object target);

    /**
     * store state to bundle
     *
     * @param source target
     * @param args bundle
     * @return true if binding match
     */
    boolean pack(Object source, Bundle args);
}