# AutoBundle

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![Build Status](https://travis-ci.org/yatatsu/AutoBundle.svg?branch=master)](https://travis-ci.org/yatatsu/AutoBundle)
[ ![Download](https://api.bintray.com/packages/yatatsu/maven/autobundle/images/download.svg) ](https://bintray.com/yatatsu/maven/autobundle/_latestVersion)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AutoBundle-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/2600)

AutoBundle generates boilerplate code for field binding with ``android.os.Bundle``.

1. Usage
2. Advanced
3. Example
4. Principle
5. Download
6. Props
7. License

## Usage

1. Generate builder method
2. Bind annotated fields
3. Store annotated fields

### 1. Generate builder method

In your class which has state from `Bundle`
 (`Activity`, `BroadcastReceiver`, `Service`, `Fragment` or others),

declare fields with `@AutoBundleField`.

Here is example for Activity.

```java
public class MyActivity extends Activity {
    // field with @AutoBundleField, must not be private/protected.
    @AutoBundleField
    String title;

    @AutoBundleField
    int exampleId;

    @AutoBundleField(required = false) // default is true
    int optionalId;
}
```

After build, `{YourClass}AutoBundle` class will be generated.

```java
public final class MyActivityAutoBundle {

  // ~~~

  public static final class Builder {
    private final Bundle args;

    public Builder(@NonNull String title, int exampleId) {
      this.args = new Bundle();
      this.args.putString("title", title);
      this.args.putInt("exampleId", exampleId);
    }

    public @NonNull MyActivityAutoBundle.Builder optionalId(int optionalId) {
      args.putInt("optionalId", optionalId);
      return this;
    }

    public @NonNull Intent build(@NonNull Context context) {
      Intent intent = new Intent(context, MyActivity.class);
      intent.putExtras(args);
      return intent;
    }

    public @NonNull Intent build(@NonNull Intent intent) {
      intent.putExtras(args);
      return intent;
    }

    public @NonNull Bundle bundle() {
      return args;
    }
  }
}
```

And you can create intent from builder.

```java
Intent intent = MyActivityAutoBundle.builder("hello, example!", 1)
    .optionalId(2) // here is optional
    .build(context);
// your can also set bundle to other intent
MyActivityAutoBundle.builder("hello, example!", 1)
    .build(otherIntent);
```

If target class is subclass of these, builder can create intent.

- `Activity`
- `Service`
- `BroadcastReceiver`

Or if target is subclass of `android.app.Fragment` or `android.support.v4.app.Fragment`,

builder can create fragment.


```java
builder = ExampleFragmentAutoBundle.builder("hello, example!", 1)
    .optionalId(2);
ExampleFragment fragment = builder.build();
// you can also set bundle to other fragment
builder.build(otherFragment);
```

And all builder has `bundle()`, which returns `Bundle`.

### 2. Bind annotated fields

In target class, Call binding method in ``onCreate``.

```java
public class ExampleFragment extends DialogFragment {
    // field with @AutoBundleField, must not be private/protected.
    @AutoBundleField
	String title;

    @AutoBundleField
	int exampleId;

    @AutoBundleField(required = false) // default is true
    int optionalId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExampleFragmentAutoBundle.bind(this, getArguments());
		// `AutoBundle` is providing easier interface. 
		// This code is equals to above.
		AutoBundle.bind(this);
    }
}
```

- ``bind(Object target, Intent intent)``
- ``bind(Object target, Bundle bundle)``
- ``bind(Activity target)`` (equals to ``bind(activity, activity.getIntent())``)
- ``bind(Object target)`` (equals to ``bind(fragment, fragment.getArguments())``)

### 3. Store annotated fields

AutoBundle bindings are also useful as restoring value in ``onSaveInstanceState(Bundle outState)``.

``pack(Object object, Bundle bundle)`` stores field value to bundle.
For example, store in ``onSaveInstanceState`` and restore in ``onCreate``.

```java
@Override
public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    ExampleFragmentAutoBundle.pack(this, outState);
    // or
    AutoBundle.pack(this, outState);
}

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
        // restore
        AutoBundle.bind(this, savedInstanceState);
    } else {
        AutoBundle.bind(this)
    }
}
```

## Advanced

- custom key name
- required
- getter/setter
- converter
- use in library

### Set key name

``key`` is key for ``Bundle``. Default is field name.
You cannot define duplicate key in one class.

```java
@AutoBundleField(key = "exampleId")
int id;
```

### Required

``required`` option is true by default.
If ``false``, Builder class has method which named key name, instead as contructor argument.

```java
@AutoBundleField(required = false)
int optionalId;
```

then,

```java
ExampleFragment fragment = ExampleFragmentAutoBundle.builder()
        .optionalId(2)
        .build();
```

### Getter/Setter

You can use getter/setter for fields. 
The method named `get/set{key}` As a default,
But you can specify with `@AutoBundleGetter` and `@AutoBundleSetter`.

```java
@AutoBundleField
private String userId;

// get{key} use as default.
// no need for @AutoBundleGetter 
public String getUserId() {
    return userId;
}

@AutoBundleGetter(key = "userId")
public String getId() {
   return userId;
}

@AutoBundleSetter(key = "userId")
public void setId(String id) {
   this.userId = id;
}
```

### CustomConverter

``converter`` option provide custom converter for storing to bundle.
You can specify class which implements ``AutoBundleConverter<T, U>``.

```java
public class ExampleFragment extends Fragment {

    @AutoBundleField(converter = DateArgConverter.class)
    Date targetDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoBundle.bind(this);
    }

    public static class DateArgConverter implements AutoBundleConverter<Date, Long> {

        @Override
        public Long convert(Date o) {
            return o.getTime();
        }

        @Override
        public Date original(Long s) {
            return new Date(s);
        }
     }
}
```

### Use in library module

If you use in library module,
you need to pass the custom package for `AutoBundleBindingDispatcher`,
which is generated by processor.

Set `autoBundleAsLibrary` option as following.

with annotationProcessor (in jack),

```groovy
android {
    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                // pachage name as you like
                arguments = [ autoBundleAsLibrary : 'com.yatatsu.example.library' ]
            }
        }
    }
}
```

or with android-apt,

```groovy
apt {
    arguments {
        autoBundleAsLibrary 'com.yatatsu.example.library'
    }
}
```

You can use library module's binding from app module by `subDispatchers` option.
Pass package name of library's dispatcher.
 (It maybe `your.package.AutoBundleBindingDispatcher`!)

```groovy
annotationProcessorOptions {
    // pass package of library's dispatcher (multi dispatchers with comma)
    arguments = [ subDispatchers : 'com.yatatsu.example.library.AutoBundleBindingDispatcher'
            + ',com.yatatsu.example.library2.AutoBundleBindingDispatcher' ]
}
```

## Example

For more information or usage, see the sample application!

(See `example/` and generated codes are under `example/build`.) 

## Principle

Both Fragment and Intent are able to pass value by storing Bundle.

There is a good pattern, known as "createIntent" or "newInstance".
Static method in called class (e.g. Fragment, Activity) promised the expected data will be passed.
AutBundle provide this pattern as builder method instead of "createIntent".

## Download

```groovy
dependencies {
    compile 'com.github.yatatsu:autobundle:4.1.0'
    annotationProcessor 'com.github.yatatsu:autobundle-processor:4.1.0'
}
```

If your gradle plugin < 2.2, use `android-apt` plugin.

```groovy
buildscript {
    dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
}

apply plugin: 'android-apt'

dependencies {
    compile 'com.github.yatatsu:autobundle:4.1.0'
    apt 'com.github.yatatsu:autobundle-processor:4.1.0'
}
```

## Props

- [sockeqwe/FragmentArgs](https://github.com/sockeqwe/fragmentargs)
- [emilsjolander/IntentBuilder](https://github.com/emilsjolander/IntentBuilder)

Actually, AutoBundle is that just integrates above both great libraries.

## License

```
Copyright 2015 KITAGAWA, Tatsuya

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
