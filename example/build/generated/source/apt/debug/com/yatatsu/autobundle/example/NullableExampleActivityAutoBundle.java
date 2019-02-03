package com.yatatsu.autobundle.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.Integer;
import java.lang.String;

public final class NullableExampleActivityAutoBundle {
  @NonNull
  public static NullableExampleActivityAutoBundle.Builder builder() {
    return new NullableExampleActivityAutoBundle.Builder();
  }

  public static void bind(@NonNull NullableExampleActivity target, @NonNull Intent intent) {
    if (intent.getExtras() != null) {
      bind(target, intent.getExtras());
    }
  }

  public static void bind(@NonNull NullableExampleActivity target, @NonNull Bundle source) {
    if (source.containsKey("name")) {
      target.name = (String) source.getString("name");
    }
    if (source.containsKey("number")) {
      target.number = (Integer) source.getInt("number");
    }
  }

  public static void pack(@NonNull NullableExampleActivity source, @NonNull Bundle args) {
    if (source.name != null) {
      args.putString("name", source.name);
    }
    if (source.number != null) {
      args.putInt("number", source.number);
    }
  }

  public static final class Builder {
    private final Bundle args;

    public Builder() {
      this.args = new Bundle();
    }

    @NonNull
    public NullableExampleActivityAutoBundle.Builder name(@Nullable String name) {
      if (name != null) {
        args.putString("name", name);
      }
      return this;
    }

    @NonNull
    public NullableExampleActivityAutoBundle.Builder number(@Nullable Integer number) {
      if (number != null) {
        args.putInt("number", number);
      }
      return this;
    }

    @NonNull
    public Intent build(@NonNull Context context) {
      Intent intent = new Intent(context, NullableExampleActivity.class);
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
