package com.yatatsu.autobundle.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.lang.String;

public final class ExampleIntentServiceAutoBundle {
  public static ExampleIntentServiceAutoBundle.IntentBuilder createIntentBuilder(String message) {
    return new ExampleIntentServiceAutoBundle.IntentBuilder(message);
  }

  public static void bind(ExampleIntentService target, Intent intent) {
    if (intent.getExtras() != null) {
      bind(target, intent.getExtras());
    }
  }

  public static void bind(ExampleIntentService target, Bundle source) {
    if (source.containsKey("message")) {
      target.string = (String) source.getString("message");
    } else {
      throw new IllegalStateException("message is required, but not found in the bundle.");
    }
  }

  public static void pack(ExampleIntentService source, Bundle args) {
    if (source.string != null) {
      args.putString("message", source.string);
    } else {
      throw new IllegalStateException("string must not be null.");
    }
  }

  public static final class IntentBuilder {
    final Bundle args;

    public IntentBuilder(String message) {
      this.args = new Bundle();
      this.args.putString("message", message);
    }

    public Intent build(Context context) {
      Intent intent = new Intent(context, ExampleIntentService.class);
      intent.putExtras(args);
      return intent;
    }

    public Intent build(Intent intent) {
      intent.putExtras(args);
      return intent;
    }
  }
}
