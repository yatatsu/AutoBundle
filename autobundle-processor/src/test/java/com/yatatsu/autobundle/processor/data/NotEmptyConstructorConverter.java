package com.yatatsu.autobundle.processor.data;


public class NotEmptyConstructorConverter implements SourceBase {

        @Override
        public String getTargetClassName() {
                return "com.yatatsu.autobundle.example.ExampleActivity";
        }

        @Override
        public String getTargetSource() {
                return "package com.yatatsu.autobundle.example;\n" +
                        "\n" +
                        "import android.app.Activity;\n" +
                        "\n" +
                        "import com.yatatsu.autobundle.AutoBundleField;\n" +
                        "import com.yatatsu.autobundle.AutoBundleConverter;\n" +
                        "\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "public class ExampleActivity extends Activity {\n" +
                        "\n" +
                        "@AutoBundleField(converter = DateArgConverter.class)\n" +
                        "Date date;\n" +
                        "\n" +
                        "public static class DateArgConverter implements AutoBundleConverter<Date, Long> {\n" +
                        "\n" +
                        "public DateArgConverter(int a) {}\n" +
                        "\n" +
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
                return "";
        }

        @Override
        public String getExpectSource() {
                return "";
        }
}
