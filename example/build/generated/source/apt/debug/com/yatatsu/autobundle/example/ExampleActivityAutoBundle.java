package com.yatatsu.autobundle.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.lang.Boolean;
import java.lang.CharSequence;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;

public final class ExampleActivityAutoBundle {
  @NonNull
  public static ExampleActivityAutoBundle.Builder builder(@ExampleActivity.IntType int type2,
      @NonNull String name) {
    return new ExampleActivityAutoBundle.Builder(type2, name);
  }

  public static void bind(@NonNull ExampleActivity target, @NonNull Intent intent) {
    if (intent.getExtras() != null) {
      bind(target, intent.getExtras());
    }
  }

  public static void bind(@NonNull ExampleActivity target, @NonNull Bundle source) {
    if (source.containsKey("type2")) {
      target.type2 = (int) source.getInt("type2");
    } else {
      throw new IllegalStateException("type2 is required, but not found in the bundle.");
    }
    if (source.containsKey("name")) {
      target.setName( (String) source.getString("name") );
    } else {
      throw new IllegalStateException("name is required, but not found in the bundle.");
    }
    if (source.containsKey("type1")) {
      target.type1 = (int) source.getInt("type1");
    }
    if (source.containsKey("optionalName")) {
      target.setAltName( (String) source.getString("optionalName") );
    }
    if (source.containsKey("fooList")) {
      target.fooList = (ArrayList<CharSequence>) source.getCharSequenceArrayList("fooList");
    }
    if (source.containsKey("exampleData")) {
      ParcelableConverter exampleDataConverter = new ParcelableConverter();
      target.exampleData = (ExampleData) exampleDataConverter.original( source.getParcelable("exampleData") );
    }
    if (source.containsKey("persons")) {
      target.persons = source.getParcelableArrayList("persons");
    }
    if (source.containsKey("exampleData2")) {
      ParcelableConverter exampleData2Converter = new ParcelableConverter();
      target.setExampleData2( (ExampleData) exampleData2Converter.original( source.getParcelable("exampleData2") ) );
    }
    if (source.containsKey("integerField")) {
      target.integerField = (Integer) source.getInt("integerField");
    }
    if (source.containsKey("booleanField")) {
      target.booleanField = (Boolean) source.getBoolean("booleanField");
    }
    if (source.containsKey("intOption")) {
      target.intOption = (int) source.getInt("intOption");
    }
  }

  public static void pack(@NonNull ExampleActivity source, @NonNull Bundle args) {
    args.putInt("type2", source.type2);
    if (source.getName() != null) {
      args.putString("name", source.getName());
    } else {
      throw new IllegalStateException("name must not be null.");
    }
    args.putInt("type1", source.type1);
    if (source.getAltName() != null) {
      args.putString("optionalName", source.getAltName());
    }
    if (source.fooList != null) {
      args.putCharSequenceArrayList("fooList", source.fooList);
    }
    if (source.exampleData != null) {
      ParcelableConverter exampleDataConverter = new ParcelableConverter();
      args.putParcelable("exampleData", exampleDataConverter.convert(source.exampleData) );
    }
    if (source.persons != null) {
      args.putParcelableArrayList("persons", source.persons);
    }
    if (source.getExampleData2() != null) {
      ParcelableConverter exampleData2Converter = new ParcelableConverter();
      args.putParcelable("exampleData2", exampleData2Converter.convert(source.getExampleData2()) );
    }
    if (source.integerField != null) {
      args.putInt("integerField", source.integerField);
    }
    if (source.booleanField != null) {
      args.putBoolean("booleanField", source.booleanField);
    }
    args.putInt("intOption", source.intOption);
  }

  public static final class Builder {
    private final Bundle args;

    public Builder(@ExampleActivity.IntType int type2, @NonNull String name) {
      this.args = new Bundle();
      this.args.putInt("type2", type2);
      this.args.putString("name", name);
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder type1(@ExampleActivity.IntType int type1) {
      args.putInt("type1", type1);
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder optionalName(@Nullable String optionalName) {
      if (optionalName != null) {
        args.putString("optionalName", optionalName);
      }
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder fooList(@Nullable ArrayList<CharSequence> fooList) {
      if (fooList != null) {
        args.putCharSequenceArrayList("fooList", fooList);
      }
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder exampleData(@Nullable ExampleData exampleData) {
      if (exampleData != null) {
        ParcelableConverter exampleDataConverter = new ParcelableConverter();
        args.putParcelable("exampleData", exampleDataConverter.convert(exampleData) );
      }
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder persons(@Nullable ArrayList<Person> persons) {
      if (persons != null) {
        args.putParcelableArrayList("persons", persons);
      }
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder exampleData2(@Nullable ExampleData exampleData2) {
      if (exampleData2 != null) {
        ParcelableConverter exampleData2Converter = new ParcelableConverter();
        args.putParcelable("exampleData2", exampleData2Converter.convert(exampleData2) );
      }
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder integerField(@Nullable Integer integerField) {
      if (integerField != null) {
        args.putInt("integerField", integerField);
      }
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder booleanField(@Nullable Boolean booleanField) {
      if (booleanField != null) {
        args.putBoolean("booleanField", booleanField);
      }
      return this;
    }

    @NonNull
    public ExampleActivityAutoBundle.Builder intOption(int intOption) {
      args.putInt("intOption", intOption);
      return this;
    }

    @NonNull
    public Intent build(@NonNull Context context) {
      Intent intent = new Intent(context, ExampleActivity.class);
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
