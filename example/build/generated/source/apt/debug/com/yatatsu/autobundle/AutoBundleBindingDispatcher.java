package com.yatatsu.autobundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.yatatsu.autobundle.example.ExampleActivity;
import com.yatatsu.autobundle.example.ExampleActivityAutoBundle;
import com.yatatsu.autobundle.example.ExampleBroadcastReceiver;
import com.yatatsu.autobundle.example.ExampleBroadcastReceiverAutoBundle;
import com.yatatsu.autobundle.example.ExampleFragment;
import com.yatatsu.autobundle.example.ExampleFragmentAutoBundle;
import com.yatatsu.autobundle.example.ExampleIntentService;
import com.yatatsu.autobundle.example.ExampleIntentServiceAutoBundle;
import com.yatatsu.autobundle.example.NullableExampleActivity;
import com.yatatsu.autobundle.example.NullableExampleActivityAutoBundle;
import java.lang.Object;

public final class AutoBundleBindingDispatcher {
  public void bind(@NonNull Object target, @NonNull Bundle args) {
    if (target instanceof ExampleActivity) {
      ExampleActivityAutoBundle.bind((ExampleActivity)target, args);
      return;
    }
    if (target instanceof ExampleBroadcastReceiver) {
      ExampleBroadcastReceiverAutoBundle.bind((ExampleBroadcastReceiver)target, args);
      return;
    }
    if (target instanceof ExampleFragment) {
      ExampleFragmentAutoBundle.bind((ExampleFragment)target, args);
      return;
    }
    if (target instanceof ExampleIntentService) {
      ExampleIntentServiceAutoBundle.bind((ExampleIntentService)target, args);
      return;
    }
    if (target instanceof NullableExampleActivity) {
      NullableExampleActivityAutoBundle.bind((NullableExampleActivity)target, args);
      return;
    }
  }

  public void bind(@NonNull Object target, @NonNull Intent intent) {
    if (target instanceof ExampleActivity) {
      ExampleActivityAutoBundle.bind((ExampleActivity)target, intent);
      return;
    }
    if (target instanceof ExampleBroadcastReceiver) {
      ExampleBroadcastReceiverAutoBundle.bind((ExampleBroadcastReceiver)target, intent);
      return;
    }
    if (target instanceof ExampleIntentService) {
      ExampleIntentServiceAutoBundle.bind((ExampleIntentService)target, intent);
      return;
    }
    if (target instanceof NullableExampleActivity) {
      NullableExampleActivityAutoBundle.bind((NullableExampleActivity)target, intent);
      return;
    }
  }

  public void bind(@NonNull Object target) {
    if (target instanceof ExampleFragment) {
      ExampleFragmentAutoBundle.bind((ExampleFragment)target);
      return;
    }
  }

  public void pack(@NonNull Object target, @NonNull Bundle args) {
    if (target instanceof ExampleActivity) {
      ExampleActivityAutoBundle.pack((ExampleActivity)target, args);
      return;
    }
    if (target instanceof ExampleBroadcastReceiver) {
      ExampleBroadcastReceiverAutoBundle.pack((ExampleBroadcastReceiver)target, args);
      return;
    }
    if (target instanceof ExampleFragment) {
      ExampleFragmentAutoBundle.pack((ExampleFragment)target, args);
      return;
    }
    if (target instanceof ExampleIntentService) {
      ExampleIntentServiceAutoBundle.pack((ExampleIntentService)target, args);
      return;
    }
    if (target instanceof NullableExampleActivity) {
      NullableExampleActivityAutoBundle.pack((NullableExampleActivity)target, args);
      return;
    }
  }
}
