package com.yatatsu.autobundle.processor.data;


public class ValidFragment implements SourceBase {

        @Override
        public String getTargetClassName() {
                return "com.yatatsu.autobundle.example.ExampleFragment";
        }

        @Override
        public String getTargetSource() {
                return "package com.yatatsu.autobundle.example;\n" +
                        "\n" +
                        "import android.app.Fragment;\n" +
                        "import android.os.Parcelable;\n" +
                        "import android.util.SparseArray;\n" +
                        "\n" +
                        "import com.yatatsu.autobundle.Arg;\n" +
                        "import com.yatatsu.autobundle.Converter;\n" +
                        "\n" +
                        "import java.util.ArrayList;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "public class ExampleFragment extends Fragment {\n" +
                        "\n" +
                        "@Arg\n" +
                        "int ids[];\n" +
                        "\n" +
                        "@Arg(required = false)\n" +
                        "ArrayList<CharSequence> messages;\n" +
                        "\n" +
                        "@Arg(key = \"models\")\n" +
                        "SparseArray<Parcelable> sparseArray;\n" +
                        "\n" +
                        "@Arg(converter = DateArgConverter.class)\n" +
                        "Date date;\n" +
                        "\n" +
                        "public static class DateArgConverter implements Converter<Date, Long> {\n" +
                        "@Override\n" +
                        "public Long convert(Date o) {\n" +
                        "return o.getTime();\n" +
                        "}\n" +
                        "\n" +
                        "@Override\n" +
                        "public Date original(Long s) {\n" +
                        "return new Date(s);\n" +
                        "}\n" +
                        "}\n" +
                        "}";
        }

        @Override
        public String getExpectClassName() {
                return "com.yatatsu.autobundle.example.ExampleFragmentAutoBundle";
        }

        @Override
        public String getExpectSource() {
                return "package com.yatatsu.autobundle.example;\n" +
                        "\n" +
                        "import android.os.Bundle;\n" +
                        "import android.os.Parcelable;\n" +
                        "import android.util.SparseArray;\n" +
                        "import java.lang.CharSequence;\n" +
                        "import java.util.ArrayList;\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "public final class ExampleFragmentAutoBundle {\n" +
                        "public static FragmentBuilder createFragmentBuilder(int[] ids, SparseArray<Parcelable> models, Date date) {\n" +
                        "return new FragmentBuilder(ids,models,date);\n" +
                        "}\n" +
                        "\n" +
                        "public static void bind(ExampleFragment target) {\n" +
                        "bind(target, target.getArguments());\n" +
                        "}\n" +
                        "\n" +
                        "public static void bind(ExampleFragment target, Bundle source) {\n" +
                        "if (source.containsKey(\"ids\")) {\n" +
                        "target.ids = source.getIntArray(\"ids\");\n" +
                        "} else {\n" +
                        "throw new IllegalStateException(\"ids is required, but not found in the bundle.\");\n" +
                        "}\n" +
                        "if (source.containsKey(\"models\")) {\n" +
                        "target.sparseArray = source.getSparseParcelableArray(\"models\");\n" +
                        "} else {\n" +
                        "throw new IllegalStateException(\"models is required, but not found in the bundle.\");\n" +
                        "}\n" +
                        "if (source.containsKey(\"date\")) {\n" +
                        "ExampleFragment.DateArgConverter dateConverter = new ExampleFragment.DateArgConverter();\n" +
                        "target.date = dateConverter.original(source.getLong(\"date\"));\n" +
                        "} else {\n" +
                        "throw new IllegalStateException(\"date is required, but not found in the bundle.\");\n" +
                        "}\n" +
                        "if (source.containsKey(\"messages\")) {\n" +
                        "target.messages = source.getCharSequenceArrayList(\"messages\");\n" +
                        "}\n" +
                        "}\n" +
                        "\n" +
                        "public static void pack(ExampleFragment source, Bundle args) {\n" +
                        "if (source.ids == null) {\n" +
                        "throw new IllegalStateException(\"ids must not be null.\");\n" +
                        "}\n" +
                        "args.putIntArray(\"ids\", source.ids);\n" +
                        "if (source.sparseArray == null) {\n" +
                        "throw new IllegalStateException(\"sparseArray must not be null.\");\n" +
                        "}\n" +
                        "args.putSparseParcelableArray(\"models\", source.sparseArray);\n" +
                        "if (source.date == null) {\n" +
                        "throw new IllegalStateException(\"date must not be null.\");\n" +
                        "}\n" +
                        "ExampleFragment.DateArgConverter dateConverter = new ExampleFragment.DateArgConverter();\n" +
                        "args.putLong(\"date\", dateConverter.convert(source.date));\n" +
                        "if (source.messages == null) {\n" +
                        "throw new IllegalStateException(\"messages must not be null.\");\n" +
                        "}\n" +
                        "args.putCharSequenceArrayList(\"messages\", source.messages);\n" +
                        "}\n" +
                        "\n" +
                        "public static final class FragmentBuilder {\n" +
                        "final Bundle args;\n" +
                        "\n" +
                        "public FragmentBuilder(int[] ids, SparseArray<Parcelable> models, Date date) {\n" +
                        "this.args = new Bundle();\n" +
                        "this.args.putIntArray(\"ids\", ids);\n" +
                        "this.args.putSparseParcelableArray(\"models\", models);\n" +
                        "ExampleFragment.DateArgConverter dateConverter = new ExampleFragment.DateArgConverter();\n" +
                        "this.args.putLong(\"date\", dateConverter.convert(date));\n" +
                        "}\n" +
                        "\n" +
                        "public FragmentBuilder messages(ArrayList<CharSequence> messages) {\n" +
                        "args.putCharSequenceArrayList(\"messages\", messages);\n" +
                        "return this;\n" +
                        "}\n" +
                        "\n" +
                        "public ExampleFragment build() {\n" +
                        "ExampleFragment fragment = new ExampleFragment();\n" +
                        "fragment.setArguments(args);\n" +
                        "return fragment;\n" +
                        "}\n" +
                        "\n" +
                        "public ExampleFragment build(ExampleFragment fragment) {\n" +
                        "fragment.setArguments(args);\n" +
                        "return fragment;\n" +
                        "}\n" +
                        "}\n" +
                        "}";
        }
}
