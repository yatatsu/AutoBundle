package com.yatatsu.autobundle.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.lang.String;

public final class ExampleBroadcastReceiverAutoBundle {
  public static ExampleBroadcastReceiverAutoBundle.IntentBuilder createIntentBuilder(String message) {
    return new ExampleBroadcastReceiverAutoBundle.IntentBuilder(message);
  }

  public static void bind(ExampleBroadcastReceiver target, Intent intent) {
    if (intent.getExtras() != null) {
      bind(target, intent.getExtras());
    }
  }

  public static void bind(ExampleBroadcastReceiver target, Bundle source) {
    if (source.containsKey("message")) {
      target.message = (String) source.getString("message");
    } else {
      throw new IllegalStateException("message is required, but not found in the bundle.");
    }
  }

  public static void pack(ExampleBroadcastReceiver source, Bundle args) {
    if (source.message != null) {
      args.putString("message", source.message);
    } else {
      throw new IllegalStateException("message must not be null.");
    }
  }

  public static final class IntentBuilder {
    final Bundle args;

    public IntentBuilder(String message) {
      this.args = new Bundle();
      this.args.putString("message", message);
    }

    public Intent build(Context context) {
      Intent intent = new Intent(context, ExampleBroadcastReceiver.class);
      intent.putExtras(args);
      return intent;
    }

    public Intent build(Intent intent) {
      intent.putExtras(args);
      return intent;
    }
  }
}
