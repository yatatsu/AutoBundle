package com.yatatsu.autobundle.processor.data;


public class NotSupportedFieldType implements SourceBase {

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
                        "\n" +
                        "public class ExampleActivity extends Activity {\n" +
                        "\n" +
                        "@AutoBundleField\n" +
                        "Object data;\n" +
                        "\n" +
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
