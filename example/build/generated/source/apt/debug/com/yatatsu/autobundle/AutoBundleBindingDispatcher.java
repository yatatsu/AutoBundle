package com.yatatsu.autobundle;

import android.os.Bundle;
import android.support.annotation.NonNull;
import com.yatatsu.autobundle.example.ExampleActivity;
import com.yatatsu.autobundle.example.ExampleActivityAutoBundle;
import com.yatatsu.autobundle.example.ExampleBroadcastReceiver;
import com.yatatsu.autobundle.example.ExampleBroadcastReceiverAutoBundle;
import com.yatatsu.autobundle.example.ExampleBundleDelegate;
import com.yatatsu.autobundle.example.ExampleBundleDelegateAutoBundle;
import com.yatatsu.autobundle.example.ExampleFragment;
import com.yatatsu.autobundle.example.ExampleFragmentAutoBundle;
import com.yatatsu.autobundle.example.ExampleIntentService;
import com.yatatsu.autobundle.example.ExampleIntentServiceAutoBundle;
import com.yatatsu.autobundle.example.NullableExampleActivity;
import com.yatatsu.autobundle.example.NullableExampleActivityAutoBundle;
import java.lang.Object;
import java.lang.Override;

public final class AutoBundleBindingDispatcher implements AutoBundleDispatcher {
  private final AutoBundleDispatcher subDispatcher0 = new com.yatatsu.example.library.AutoBundleBindingDispatcher();

  @Override
  public boolean bind(@NonNull Object target, @NonNull Bundle args) {
    if (target instanceof ExampleActivity) {
      ExampleActivityAutoBundle.bind((ExampleActivity)target, args);
      return true;
    }
    if (target instanceof ExampleBroadcastReceiver) {
      ExampleBroadcastReceiverAutoBundle.bind((ExampleBroadcastReceiver)target, args);
      return true;
    }
    if (target instanceof ExampleBundleDelegate) {
      ExampleBundleDelegateAutoBundle.bind((ExampleBundleDelegate)target, args);
      return true;
    }
    if (target instanceof ExampleFragment) {
      ExampleFragmentAutoBundle.bind((ExampleFragment)target, args);
      return true;
    }
    if (target instanceof ExampleIntentService) {
      ExampleIntentServiceAutoBundle.bind((ExampleIntentService)target, args);
      return true;
    }
    if (target instanceof NullableExampleActivity) {
      NullableExampleActivityAutoBundle.bind((NullableExampleActivity)target, args);
      return true;
    }
    if (subDispatcher0.bind(target, args)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean bind(@NonNull Object target) {
    if (target instanceof ExampleFragment) {
      ExampleFragmentAutoBundle.bind((ExampleFragment)target);
      return true;
    }
    if (subDispatcher0.bind(target)) {
      return true;
    }
    return false;
  }

  @Override
  public boolean pack(@NonNull Object target, @NonNull Bundle args) {
    if (target instanceof ExampleActivity) {
      ExampleActivityAutoBundle.pack((ExampleActivity)target, args);
      return true;
    }
    if (target instanceof ExampleBroadcastReceiver) {
      ExampleBroadcastReceiverAutoBundle.pack((ExampleBroadcastReceiver)target, args);
      return true;
    }
    if (target instanceof ExampleBundleDelegate) {
      ExampleBundleDelegateAutoBundle.pack((ExampleBundleDelegate)target, args);
      return true;
    }
    if (target instanceof ExampleFragment) {
      ExampleFragmentAutoBundle.pack((ExampleFragment)target, args);
      return true;
    }
    if (target instanceof ExampleIntentService) {
      ExampleIntentServiceAutoBundle.pack((ExampleIntentService)target, args);
      return true;
    }
    if (target instanceof NullableExampleActivity) {
      NullableExampleActivityAutoBundle.pack((NullableExampleActivity)target, args);
      return true;
    }
    if (subDispatcher0.pack(target, args)) {
      return true;
    }
    return false;
  }
}
