package com.yatatsu.autobundle.processor.data;


public class PrivateAnnotatedSetter implements SourceBase {
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
                "import com.yatatsu.autobundle.AutoBundleSetter;\n" +
                "\n" +
                "\n" +
                "public class ExampleActivity extends Activity {\n" +
                "\n" +
                "@AutoBundleField(key = \"itemName\")\n" +
                "private String name;\n" +
                "\n" +
                "@AutoBundleSetter(key = \"itemName\")\n" +
                "private void setName(String name) {\n" +
                "this.name = name; \n" +
                "}\n" +
                "}\n";
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
