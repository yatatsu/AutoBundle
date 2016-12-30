package com.yatatsu.autobundle.example;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.lang.CharSequence;
import java.lang.String;
import java.util.ArrayList;

public final class ExampleActivityAutoBundle {
  public static ExampleActivityAutoBundle.IntentBuilder createIntentBuilder(@ExampleActivity.IntType int type2,
      String name) {
    return new ExampleActivityAutoBundle.IntentBuilder(type2,name);
  }

  public static void bind(ExampleActivity target, Intent intent) {
    if (intent.getExtras() != null) {
      bind(target, intent.getExtras());
    }
  }

  public static void bind(ExampleActivity target, Bundle source) {
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
  }

  public static void pack(ExampleActivity source, Bundle args) {
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
  }

  public static final class IntentBuilder {
    final Bundle args;

    public IntentBuilder(@ExampleActivity.IntType int type2, String name) {
      this.args = new Bundle();
      this.args.putInt("type2", type2);
      this.args.putString("name", name);
    }

    public ExampleActivityAutoBundle.IntentBuilder type1(@ExampleActivity.IntType int type1) {
      args.putInt("type1", type1);
      return this;
    }

    public ExampleActivityAutoBundle.IntentBuilder optionalName(String optionalName) {
      if (optionalName != null) {
        args.putString("optionalName", optionalName);
      }
      return this;
    }

    public ExampleActivityAutoBundle.IntentBuilder fooList(ArrayList<CharSequence> fooList) {
      if (fooList != null) {
        args.putCharSequenceArrayList("fooList", fooList);
      }
      return this;
    }

    public ExampleActivityAutoBundle.IntentBuilder exampleData(ExampleData exampleData) {
      if (exampleData != null) {
        ParcelableConverter exampleDataConverter = new ParcelableConverter();
        args.putParcelable("exampleData", exampleDataConverter.convert(exampleData) );
      }
      return this;
    }

    public ExampleActivityAutoBundle.IntentBuilder persons(ArrayList<Person> persons) {
      if (persons != null) {
        args.putParcelableArrayList("persons", persons);
      }
      return this;
    }

    public ExampleActivityAutoBundle.IntentBuilder exampleData2(ExampleData exampleData2) {
      if (exampleData2 != null) {
        ParcelableConverter exampleData2Converter = new ParcelableConverter();
        args.putParcelable("exampleData2", exampleData2Converter.convert(exampleData2) );
      }
      return this;
    }

    public Intent build(Context context) {
      Intent intent = new Intent(context, ExampleActivity.class);
      intent.putExtras(args);
      return intent;
    }

    public Intent build(Intent intent) {
      intent.putExtras(args);
      return intent;
    }
  }
}
