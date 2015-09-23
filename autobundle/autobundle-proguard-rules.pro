-keep class **AutoBundle { *; }
-keep class com.yatatsu.autobundle.** { *; }
-keepclasseswithmembers class * {
    @com.yatatsu.autobundle.* <fields>;
}