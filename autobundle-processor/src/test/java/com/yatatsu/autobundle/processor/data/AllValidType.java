package com.yatatsu.autobundle.processor.data;


public class AllValidType implements SourceBase {

    @Override
    public String getTargetClassName() {
        return "com.yatatsu.autobundle.example.ExampleActivity";
    }

    @Override
    public String getTargetSource() {
        return "package com.yatatsu.autobundle.example;\n" +
                "\n" +
                "import android.app.Activity;\n" +
                "import android.os.Bundle;\n" +
                "import android.os.Parcelable;\n" +
                "import android.util.SparseArray;\n" +
                "\n" +
                "import com.yatatsu.autobundle.AutoBundle;\n" +
                "import com.yatatsu.autobundle.AutoBundleTarget;\n" +
                "\n" +
                "import java.io.Serializable;\n" +
                "import java.util.ArrayList;\n" +
                "\n" +
                "@AutoBundleTarget\n" +
                "public class ExampleActivity extends Activity {\n" +
                "\n" +
                "@AutoBundle\n" +
                "boolean booelean1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "boolean[] booeleanArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "byte byte1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "byte[] byteArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "char char1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "char[] charArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "CharSequence charSequence1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "CharSequence[] charSequenceArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "double double1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "double[] doubleArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "float float1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "float[] floatArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "int int1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "int[] intArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "long long1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "long[] longArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "short short1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "short[] shortArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "String string1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "String[] stringArray1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "Parcelable parcelable;\n" +
                "\n" +
                "@AutoBundle\n" +
                "Parcelable[] parcelableArray;\n" +
                "\n" +
                "@AutoBundle\n" +
                "Serializable serializable1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "Bundle bundle1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "ArrayList<Integer> integerArrayList1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "ArrayList<String> stringArrayList1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "ArrayList<CharSequence> charSequenceArrayList1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "ArrayList<Parcelable> parcelableArrayList1;\n" +
                "\n" +
                "@AutoBundle\n" +
                "SparseArray<Parcelable> parcelableSparseArray1;\n" +
                "}";
    }

    @Override
    public String getExpectClassName() {
        return "com.yatatsu.autobundle.example.ExampleActivityAutoBundle";
    }

    @Override
    public String getExpectSource() {
        return "package com.yatatsu.autobundle.example;\n" +
                "\n" +
                "import android.content.Context;\n" +
                "import android.content.Intent;\n" +
                "import android.os.Bundle;\n" +
                "import android.os.Parcelable;\n" +
                "import android.util.SparseArray;\n" +
                "import java.io.Serializable;\n" +
                "import java.lang.CharSequence;\n" +
                "import java.lang.Integer;\n" +
                "import java.lang.String;\n" +
                "import java.util.ArrayList;\n" +
                "\n" +
                "public final class ExampleActivityAutoBundle {\n" +
                "public static IntentBuilder createIntentBuilder(boolean booelean1, boolean[] booeleanArray1, byte byte1, byte[] byteArray1, char char1, char[] charArray1, CharSequence charSequence1, CharSequence[] charSequenceArray1, double double1, double[] doubleArray1, float float1, float[] floatArray1, int int1, int[] intArray1, long long1, long[] longArray1, short short1, short[] shortArray1, String string1, String[] stringArray1, Parcelable parcelable, Parcelable[] parcelableArray, Serializable serializable1, Bundle bundle1, ArrayList<Integer> integerArrayList1, ArrayList<String> stringArrayList1, ArrayList<CharSequence> charSequenceArrayList1, ArrayList<Parcelable> parcelableArrayList1, SparseArray<Parcelable> parcelableSparseArray1) {\n" +
                "return new IntentBuilder(booelean1,booeleanArray1,byte1,byteArray1,char1,charArray1,charSequence1,charSequenceArray1,double1,doubleArray1,float1,floatArray1,int1,intArray1,long1,longArray1,short1,shortArray1,string1,stringArray1,parcelable,parcelableArray,serializable1,bundle1,integerArrayList1,stringArrayList1,charSequenceArrayList1,parcelableArrayList1,parcelableSparseArray1);\n" +
                "}\n" +
                "\n" +
                "public static void bind(ExampleActivity target, Intent intent) {\n" +
                "if (intent.getExtras() != null) {\n" +
                "bind(target, intent.getExtras());\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "public static void bind(ExampleActivity target, Bundle source) {\n" +
                "if (source.containsKey(\"booelean1\")) {\n" +
                "target.booelean1 = source.getBoolean(\"booelean1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"booelean1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"booeleanArray1\")) {\n" +
                "target.booeleanArray1 = source.getBooleanArray(\"booeleanArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"booeleanArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"byte1\")) {\n" +
                "target.byte1 = source.getByte(\"byte1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"byte1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"byteArray1\")) {\n" +
                "target.byteArray1 = source.getByteArray(\"byteArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"byteArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"char1\")) {\n" +
                "target.char1 = source.getChar(\"char1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"char1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"charArray1\")) {\n" +
                "target.charArray1 = source.getCharArray(\"charArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"charArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"charSequence1\")) {\n" +
                "target.charSequence1 = source.getCharSequence(\"charSequence1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"charSequence1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"charSequenceArray1\")) {\n" +
                "target.charSequenceArray1 = source.getCharSequenceArray(\"charSequenceArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"charSequenceArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"double1\")) {\n" +
                "target.double1 = source.getDouble(\"double1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"double1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"doubleArray1\")) {\n" +
                "target.doubleArray1 = source.getDoubleArray(\"doubleArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"doubleArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"float1\")) {\n" +
                "target.float1 = source.getFloat(\"float1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"float1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"floatArray1\")) {\n" +
                "target.floatArray1 = source.getFloatArray(\"floatArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"floatArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"int1\")) {\n" +
                "target.int1 = source.getInt(\"int1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"int1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"intArray1\")) {\n" +
                "target.intArray1 = source.getIntArray(\"intArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"intArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"long1\")) {\n" +
                "target.long1 = source.getLong(\"long1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"long1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"longArray1\")) {\n" +
                "target.longArray1 = source.getLongArray(\"longArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"longArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"short1\")) {\n" +
                "target.short1 = source.getShort(\"short1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"short1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"shortArray1\")) {\n" +
                "target.shortArray1 = source.getShortArray(\"shortArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"shortArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"string1\")) {\n" +
                "target.string1 = source.getString(\"string1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"string1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"stringArray1\")) {\n" +
                "target.stringArray1 = source.getStringArray(\"stringArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"stringArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"parcelable\")) {\n" +
                "target.parcelable = source.getParcelable(\"parcelable\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"parcelable is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"parcelableArray\")) {\n" +
                "target.parcelableArray = source.getParcelableArray(\"parcelableArray\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"parcelableArray is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"serializable1\")) {\n" +
                "target.serializable1 = source.getSerializable(\"serializable1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"serializable1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"bundle1\")) {\n" +
                "target.bundle1 = source.getBundle(\"bundle1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"bundle1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"integerArrayList1\")) {\n" +
                "target.integerArrayList1 = source.getIntegerArrayList(\"integerArrayList1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"integerArrayList1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"stringArrayList1\")) {\n" +
                "target.stringArrayList1 = source.getStringArrayList(\"stringArrayList1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"stringArrayList1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"charSequenceArrayList1\")) {\n" +
                "target.charSequenceArrayList1 = source.getCharSequenceArrayList(\"charSequenceArrayList1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"charSequenceArrayList1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"parcelableArrayList1\")) {\n" +
                "target.parcelableArrayList1 = source.getParcelableArrayList(\"parcelableArrayList1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"parcelableArrayList1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "if (source.containsKey(\"parcelableSparseArray1\")) {\n" +
                "target.parcelableSparseArray1 = source.getSparseParcelableArray(\"parcelableSparseArray1\");\n" +
                "} else {\n" +
                "throw new IllegalStateException(\"parcelableSparseArray1 is required, but not found in the bundle.\");\n" +
                "}\n" +
                "}\n" +
                "\n" +
                "public static void pack(ExampleActivity source, Bundle args) {\n" +
                "args.putBoolean(\"booelean1\", source.booelean1);\n" +
                "if (source.booeleanArray1 == null) {\n" +
                "throw new IllegalStateException(\"booeleanArray1 must not be null.\");\n" +
                "}\n" +
                "args.putBooleanArray(\"booeleanArray1\", source.booeleanArray1);\n" +
                "args.putByte(\"byte1\", source.byte1);\n" +
                "if (source.byteArray1 == null) {\n" +
                "throw new IllegalStateException(\"byteArray1 must not be null.\");\n" +
                "}\n" +
                "args.putByteArray(\"byteArray1\", source.byteArray1);\n" +
                "args.putChar(\"char1\", source.char1);\n" +
                "if (source.charArray1 == null) {\n" +
                "throw new IllegalStateException(\"charArray1 must not be null.\");\n" +
                "}\n" +
                "args.putCharArray(\"charArray1\", source.charArray1);\n" +
                "if (source.charSequence1 == null) {\n" +
                "throw new IllegalStateException(\"charSequence1 must not be null.\");\n" +
                "}\n" +
                "args.putCharSequence(\"charSequence1\", source.charSequence1);\n" +
                "if (source.charSequenceArray1 == null) {\n" +
                "throw new IllegalStateException(\"charSequenceArray1 must not be null.\");\n" +
                "}\n" +
                "args.putCharSequenceArray(\"charSequenceArray1\", source.charSequenceArray1);\n" +
                "args.putDouble(\"double1\", source.double1);\n" +
                "if (source.doubleArray1 == null) {\n" +
                "throw new IllegalStateException(\"doubleArray1 must not be null.\");\n" +
                "}\n" +
                "args.putDoubleArray(\"doubleArray1\", source.doubleArray1);\n" +
                "args.putFloat(\"float1\", source.float1);\n" +
                "if (source.floatArray1 == null) {\n" +
                "throw new IllegalStateException(\"floatArray1 must not be null.\");\n" +
                "}\n" +
                "args.putFloatArray(\"floatArray1\", source.floatArray1);\n" +
                "args.putInt(\"int1\", source.int1);\n" +
                "if (source.intArray1 == null) {\n" +
                "throw new IllegalStateException(\"intArray1 must not be null.\");\n" +
                "}\n" +
                "args.putIntArray(\"intArray1\", source.intArray1);\n" +
                "args.putLong(\"long1\", source.long1);\n" +
                "if (source.longArray1 == null) {\n" +
                "throw new IllegalStateException(\"longArray1 must not be null.\");\n" +
                "}\n" +
                "args.putLongArray(\"longArray1\", source.longArray1);\n" +
                "args.putShort(\"short1\", source.short1);\n" +
                "if (source.shortArray1 == null) {\n" +
                "throw new IllegalStateException(\"shortArray1 must not be null.\");\n" +
                "}\n" +
                "args.putShortArray(\"shortArray1\", source.shortArray1);\n" +
                "if (source.string1 == null) {\n" +
                "throw new IllegalStateException(\"string1 must not be null.\");\n" +
                "}\n" +
                "args.putString(\"string1\", source.string1);\n" +
                "if (source.stringArray1 == null) {\n" +
                "throw new IllegalStateException(\"stringArray1 must not be null.\");\n" +
                "}\n" +
                "args.putStringArray(\"stringArray1\", source.stringArray1);\n" +
                "if (source.parcelable == null) {\n" +
                "throw new IllegalStateException(\"parcelable must not be null.\");\n" +
                "}\n" +
                "args.putParcelable(\"parcelable\", source.parcelable);\n" +
                "if (source.parcelableArray == null) {\n" +
                "throw new IllegalStateException(\"parcelableArray must not be null.\");\n" +
                "}\n" +
                "args.putParcelableArray(\"parcelableArray\", source.parcelableArray);\n" +
                "if (source.serializable1 == null) {\n" +
                "throw new IllegalStateException(\"serializable1 must not be null.\");\n" +
                "}\n" +
                "args.putSerializable(\"serializable1\", source.serializable1);\n" +
                "if (source.bundle1 == null) {\n" +
                "throw new IllegalStateException(\"bundle1 must not be null.\");\n" +
                "}\n" +
                "args.putBundle(\"bundle1\", source.bundle1);\n" +
                "if (source.integerArrayList1 == null) {\n" +
                "throw new IllegalStateException(\"integerArrayList1 must not be null.\");\n" +
                "}\n" +
                "args.putIntegerArrayList(\"integerArrayList1\", source.integerArrayList1);\n" +
                "if (source.stringArrayList1 == null) {\n" +
                "throw new IllegalStateException(\"stringArrayList1 must not be null.\");\n" +
                "}\n" +
                "args.putStringArrayList(\"stringArrayList1\", source.stringArrayList1);\n" +
                "if (source.charSequenceArrayList1 == null) {\n" +
                "throw new IllegalStateException(\"charSequenceArrayList1 must not be null.\");\n" +
                "}\n" +
                "args.putCharSequenceArrayList(\"charSequenceArrayList1\", source.charSequenceArrayList1);\n" +
                "if (source.parcelableArrayList1 == null) {\n" +
                "throw new IllegalStateException(\"parcelableArrayList1 must not be null.\");\n" +
                "}\n" +
                "args.putParcelableArrayList(\"parcelableArrayList1\", source.parcelableArrayList1);\n" +
                "if (source.parcelableSparseArray1 == null) {\n" +
                "throw new IllegalStateException(\"parcelableSparseArray1 must not be null.\");\n" +
                "}\n" +
                "args.putSparseParcelableArray(\"parcelableSparseArray1\", source.parcelableSparseArray1);\n" +
                "}\n" +
                "\n" +
                "public static final class IntentBuilder {\n" +
                "final Bundle args;\n" +
                "\n" +
                "public IntentBuilder(boolean booelean1, boolean[] booeleanArray1, byte byte1, byte[] byteArray1, char char1, char[] charArray1, CharSequence charSequence1, CharSequence[] charSequenceArray1, double double1, double[] doubleArray1, float float1, float[] floatArray1, int int1, int[] intArray1, long long1, long[] longArray1, short short1, short[] shortArray1, String string1, String[] stringArray1, Parcelable parcelable, Parcelable[] parcelableArray, Serializable serializable1, Bundle bundle1, ArrayList<Integer> integerArrayList1, ArrayList<String> stringArrayList1, ArrayList<CharSequence> charSequenceArrayList1, ArrayList<Parcelable> parcelableArrayList1, SparseArray<Parcelable> parcelableSparseArray1) {\n" +
                "this.args = new Bundle();\n" +
                "this.args.putBoolean(\"booelean1\", booelean1);\n" +
                "this.args.putBooleanArray(\"booeleanArray1\", booeleanArray1);\n" +
                "this.args.putByte(\"byte1\", byte1);\n" +
                "this.args.putByteArray(\"byteArray1\", byteArray1);\n" +
                "this.args.putChar(\"char1\", char1);\n" +
                "this.args.putCharArray(\"charArray1\", charArray1);\n" +
                "this.args.putCharSequence(\"charSequence1\", charSequence1);\n" +
                "this.args.putCharSequenceArray(\"charSequenceArray1\", charSequenceArray1);\n" +
                "this.args.putDouble(\"double1\", double1);\n" +
                "this.args.putDoubleArray(\"doubleArray1\", doubleArray1);\n" +
                "this.args.putFloat(\"float1\", float1);\n" +
                "this.args.putFloatArray(\"floatArray1\", floatArray1);\n" +
                "this.args.putInt(\"int1\", int1);\n" +
                "this.args.putIntArray(\"intArray1\", intArray1);\n" +
                "this.args.putLong(\"long1\", long1);\n" +
                "this.args.putLongArray(\"longArray1\", longArray1);\n" +
                "this.args.putShort(\"short1\", short1);\n" +
                "this.args.putShortArray(\"shortArray1\", shortArray1);\n" +
                "this.args.putString(\"string1\", string1);\n" +
                "this.args.putStringArray(\"stringArray1\", stringArray1);\n" +
                "this.args.putParcelable(\"parcelable\", parcelable);\n" +
                "this.args.putParcelableArray(\"parcelableArray\", parcelableArray);\n" +
                "this.args.putSerializable(\"serializable1\", serializable1);\n" +
                "this.args.putBundle(\"bundle1\", bundle1);\n" +
                "this.args.putIntegerArrayList(\"integerArrayList1\", integerArrayList1);\n" +
                "this.args.putStringArrayList(\"stringArrayList1\", stringArrayList1);\n" +
                "this.args.putCharSequenceArrayList(\"charSequenceArrayList1\", charSequenceArrayList1);\n" +
                "this.args.putParcelableArrayList(\"parcelableArrayList1\", parcelableArrayList1);\n" +
                "this.args.putSparseParcelableArray(\"parcelableSparseArray1\", parcelableSparseArray1);\n" +
                "}\n" +
                "\n" +
                "public Intent build(Context context) {\n" +
                "Intent intent = new Intent(context, ExampleActivity.class);\n" +
                "intent.putExtras(args);\n" +
                "return intent;\n" +
                "}\n" +
                "\n" +
                "public Intent build(Intent intent) {\n" +
                "intent.putExtras(args);\n" +
                "return intent;\n" +
                "}\n" +
                "}\n" +
                "}";
    }
}
