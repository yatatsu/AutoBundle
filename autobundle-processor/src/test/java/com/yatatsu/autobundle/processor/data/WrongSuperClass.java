package com.yatatsu.autobundle.processor.data;


public class WrongSuperClass implements SourceBase {
    @Override
    public String getTargetClassName() {
        return "com.yatatsu.autobundle.example.ExampleActivity";
    }

    @Override
    public String getTargetSource() {
        return "package com.yatatsu.autobundle.example;\n" +
                "\n" +
                "import com.yatatsu.autobundle.Arg;\n" +
                "\n" +
                "\n" +
                "public class ExampleActivity {\n" +
                "\n" +
                "@Arg\n" +
                "String name;\n" +
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
