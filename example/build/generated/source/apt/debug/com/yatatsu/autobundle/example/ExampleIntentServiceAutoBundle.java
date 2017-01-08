package com.yatatsu.autobundle.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import java.lang.String;

public final class ExampleIntentServiceAutoBundle {
  public static @NonNull ExampleIntentServiceAutoBundle.Builder builder(@NonNull String message) {
    return new ExampleIntentServiceAutoBundle.Builder(message);
  }

  public static void bind(@NonNull ExampleIntentService target, @NonNull Intent intent) {
    if (intent.getExtras() != null) {
      bind(target, intent.getExtras());
    }
  }

  public static void bind(@NonNull ExampleIntentService target, @NonNull Bundle source) {
    if (source.containsKey("message")) {
      target.string = (String) source.getString("message");
    } else {
      throw new IllegalStateException("message is required, but not found in the bundle.");
    }
  }

  public static void pack(@NonNull ExampleIntentService source, @NonNull Bundle args) {
    if (source.string != null) {
      args.putString("message", source.string);
    } else {
      throw new IllegalStateException("string must not be null.");
    }
  }

  public static final class Builder {
    final Bundle args;

    public Builder(@NonNull String message) {
      this.args = new Bundle();
      this.args.putString("message", message);
    }

    public @NonNull Intent build(@NonNull Context context) {
      Intent intent = new Intent(context, ExampleIntentService.class);
      intent.putExtras(args);
      return intent;
    }

    public @NonNull Intent build(@NonNull Intent intent) {
      intent.putExtras(args);
      return intent;
    }
  }
}
