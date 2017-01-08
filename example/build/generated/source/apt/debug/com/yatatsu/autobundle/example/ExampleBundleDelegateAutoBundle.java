package com.yatatsu.autobundle.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import java.lang.String;
import java.util.ArrayList;

public final class ExampleBundleDelegateAutoBundle {
  public static @NonNull ExampleBundleDelegateAutoBundle.Builder builder(@NonNull String name) {
    return new ExampleBundleDelegateAutoBundle.Builder(name);
  }

  public static void bind(@NonNull ExampleBundleDelegate target, @NonNull Bundle source) {
    if (source.containsKey("name")) {
      target.name = (String) source.getString("name");
    } else {
      throw new IllegalStateException("name is required, but not found in the bundle.");
    }
    if (source.containsKey("number")) {
      target.number = (int) source.getInt("number");
    }
    if (source.containsKey("list")) {
      target.list = (ArrayList<String>) source.getStringArrayList("list");
    }
  }

  public static void pack(@NonNull ExampleBundleDelegate source, @NonNull Bundle args) {
    if (source.name != null) {
      args.putString("name", source.name);
    } else {
      throw new IllegalStateException("name must not be null.");
    }
    args.putInt("number", source.number);
    if (source.list != null) {
      args.putStringArrayList("list", source.list);
    }
  }

  public static final class Builder {
    private final Bundle args;

    public Builder(@NonNull String name) {
      this.args = new Bundle();
      this.args.putString("name", name);
    }

    public @NonNull ExampleBundleDelegateAutoBundle.Builder number(int number) {
      args.putInt("number", number);
      return this;
    }

    public @NonNull ExampleBundleDelegateAutoBundle.Builder list(@Nullable ArrayList<String> list) {
      if (list != null) {
        args.putStringArrayList("list", list);
      }
      return this;
    }

    public @NonNull Bundle bundle() {
      return args;
    }
  }
}
