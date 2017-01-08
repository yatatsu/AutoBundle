package com.yatatsu.autobundle.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import java.lang.String;
import java.util.Date;

public final class ExampleFragmentAutoBundle {
  public static @NonNull ExampleFragmentAutoBundle.Builder builder(@NonNull String title,
      @NonNull Date targetDate) {
    return new ExampleFragmentAutoBundle.Builder(title, targetDate);
  }

  public static void bind(@NonNull ExampleFragment target) {
    if (target.getArguments() != null) {
      bind(target, target.getArguments());
    }
  }

  public static void bind(@NonNull ExampleFragment target, @NonNull Bundle source) {
    if (source.containsKey("title")) {
      target.title = (String) source.getString("title");
    } else {
      throw new IllegalStateException("title is required, but not found in the bundle.");
    }
    if (source.containsKey("targetDate")) {
      ExampleFragment.DateArgConverter targetDateConverter = new ExampleFragment.DateArgConverter();
      target.targetDate = (Date) targetDateConverter.original( source.getLong("targetDate") );
    } else {
      throw new IllegalStateException("targetDate is required, but not found in the bundle.");
    }
  }

  public static void pack(@NonNull ExampleFragment source, @NonNull Bundle args) {
    if (source.title != null) {
      args.putString("title", source.title);
    } else {
      throw new IllegalStateException("title must not be null.");
    }
    if (source.targetDate != null) {
      ExampleFragment.DateArgConverter targetDateConverter = new ExampleFragment.DateArgConverter();
      args.putLong("targetDate", targetDateConverter.convert(source.targetDate) );
    } else {
      throw new IllegalStateException("targetDate must not be null.");
    }
  }

  public static final class Builder {
    final Bundle args;

    public Builder(@NonNull String title, @NonNull Date targetDate) {
      this.args = new Bundle();
      this.args.putString("title", title);
      ExampleFragment.DateArgConverter targetDateConverter = new ExampleFragment.DateArgConverter();
      this.args.putLong("targetDate", targetDateConverter.convert(targetDate) );
    }

    public @NonNull ExampleFragment build() {
      ExampleFragment fragment = new ExampleFragment();
      fragment.setArguments(args);
      return fragment;
    }

    public @NonNull ExampleFragment build(@NonNull ExampleFragment fragment) {
      fragment.setArguments(args);
      return fragment;
    }
  }
}
