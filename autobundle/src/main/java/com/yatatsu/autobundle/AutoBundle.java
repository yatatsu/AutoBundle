package com.yatatsu.autobundle;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Injection helper.
 * resolve each binding class for target.
 */
public class AutoBundle {

    private static AutoBundleBindingDispatcher dispatcher = new AutoBundleBindingDispatcher();

    private AutoBundle() {
        throw new AssertionError("no instances");
    }

    /**
     * assign to target fields from {@link Activity#getIntent()}
     * @param activity target activity which has {@link AutoBundleField} annotated fields.
     */
    public static void bind(@NonNull Activity activity) {
        bind(activity, activity.getIntent());
    }

    /**
     * assign to target fields from {@link Fragment#getArguments()}.
     *
     * target may be {@link Fragment} or compat.
     *
     * @param target target Fragment which has {@link AutoBundleField} annotated fields.
     */
    public static void bind(@NonNull Object target) {
        try {
            dispatcher.bind(target);
        } catch (Exception e) {
            throw new RuntimeException("AutoBundle cannot bind with " + target.getClass(), e);
        }
    }

    /**
     * assign arguments to target fields.
     *
     * target may be {@link android.app.Activity},
     * {@link android.content.BroadcastReceiver},
     * {@link android.app.Service},
     * {@link android.app.Fragment} or these compatibility class.
     *
     * @param target target which has {@link AutoBundleField} annotated fields.
     * @param args source bundle.
     */
    public static void bind(@NonNull Object target, @NonNull Bundle args) {
        try {
            dispatcher.bind(target, args);
        } catch (Exception e) {
            throw new RuntimeException("AutoBundle cannot bind with " + target.getClass(), e);
        }
    }

    /**
     * assign to target fields from {@link Intent#getExtras()}.
     *
     * target may be {@link android.app.Activity},
     * {@link android.content.BroadcastReceiver},
     * {@link android.app.Service} or these compatibility class.
     *
     * @param target target which has {@link AutoBundleField} annotated fields.
     * @param intent source bundle.
     */
    public static void bind(@NonNull Object target, @NonNull Intent intent) {
        try {
            if (intent.getExtras() != null) {
                dispatcher.bind(target, intent.getExtras());
            }
        } catch (Exception e) {
            throw new RuntimeException("AutoBundle cannot bind with " + target.getClass(), e);
        }
    }

    /**
     * set arguments from source fields.
     *
     * target may be {@link android.app.Activity},
     * {@link android.content.BroadcastReceiver},
     * {@link android.app.Service},
     * {@link android.app.Fragment} or these compatibility class.
     *
     * @param source source instance which has {@link AutoBundleField} annotated fields.
     * @param args target bundle.
     */
    public static void pack(@NonNull Object source, @NonNull Bundle args) {
        try {
            dispatcher.pack(source, args);
        } catch (Exception e) {
            throw new RuntimeException("AutoBundle cannot bind with " + source.getClass(), e);
        }
    }
}