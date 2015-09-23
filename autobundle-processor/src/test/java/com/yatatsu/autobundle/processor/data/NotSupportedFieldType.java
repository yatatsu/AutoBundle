package com.yatatsu.autobundle.processor.data;

/**
* Created by kitagawatatsuya on 2015/09/23.
*/
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
                        "import com.yatatsu.autobundle.AutoBundle;\n" +
                        "import com.yatatsu.autobundle.AutoBundleTarget;\n" +
                        "\n" +
                        "import java.util.Date;\n" +
                        "\n" +
                        "@AutoBundleTarget\n" +
                        "public class ExampleActivity extends Activity {\n" +
                        "\n" +
                        "@AutoBundle\n" +
                        "Date date;\n" +
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
