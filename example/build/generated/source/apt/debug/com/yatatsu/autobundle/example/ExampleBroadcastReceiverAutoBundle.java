package com.yatatsu.autobundle.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import java.lang.String;

public final class ExampleBroadcastReceiverAutoBundle {
  @NonNull
  public static ExampleBroadcastReceiverAutoBundle.Builder builder(@NonNull String message) {
    return new ExampleBroadcastReceiverAutoBundle.Builder(message);
  }

  public static void bind(@NonNull ExampleBroadcastReceiver target, @NonNull Intent intent) {
    if (intent.getExtras() != null) {
      bind(target, intent.getExtras());
    }
  }

  public static void bind(@NonNull ExampleBroadcastReceiver target, @NonNull Bundle source) {
    if (source.containsKey("message")) {
      target.message = (String) source.getString("message");
    } else {
      throw new IllegalStateException("message is required, but not found in the bundle.");
    }
  }

  public static void pack(@NonNull ExampleBroadcastReceiver source, @NonNull Bundle args) {
    if (source.message != null) {
      args.putString("message", source.message);
    } else {
      throw new IllegalStateException("message must not be null.");
    }
  }

  public static final class Builder {
    private final Bundle args;

    public Builder(@NonNull String message) {
      this.args = new Bundle();
      this.args.putString("message", message);
    }

    @NonNull
    public Intent build(@NonNull Context context) {
      Intent intent = new Intent(context, ExampleBroadcastReceiver.class);
      intent.putExtras(args);
      return intent;
    }

    @NonNull
    public Intent build(@NonNull Intent intent) {
      intent.putExtras(args);
      return intent;
    }

    @NonNull
    public Bundle bundle() {
      return args;
    }
  }
}
