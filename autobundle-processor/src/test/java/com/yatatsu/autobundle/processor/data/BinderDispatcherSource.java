package com.yatatsu.autobundle.processor.data;


public class BinderDispatcherSource implements SourceBase {

    @Override
    public String getTargetClassName() {
        return "com.yatatsu.autobundle.example.ExampleActivity";
    }

    public String getTargetClassName2() {
        return "com.yatatsu.autobundle.example.ExampleFragment";
    }

    @Override
    public String getTargetSource() {
        return "package com.yatatsu.autobundle.example;\n" +
                "\n" +
                "\n" +
                "import android.app.Activity;\n" +
                "\n" +
                "import com.yatatsu.autobundle.Arg;\n" +
                "\n" +
                "public class ExampleActivity extends Activity {\n" +
                "@Arg\n" +
                "int exampleId;\n" +
                "}\n";
    }

    public String getTargetSource2() {
        return "package com.yatatsu.autobundle.example;\n" +
                "\n" +
                "import android.app.Fragment;\n" +
                "\n" +
                "import com.yatatsu.autobundle.Arg;\n" +
                "\n" +
                "\n" +
                "public class ExampleFragment extends Fragment {\n" +
                "@Arg\n" +
                "String name;\n" +
                "}\n";
    }

    @Override
    public String getExpectClassName() {
        return "com.yatatsu.autobundle.AutoBundleBindingDispatcher";
    }

    @Override
    public String getExpectSource() {
        return "package com.yatatsu.autobundle;\n" +
                "\n" +
                "import android.content.Intent;\n" +
                "import android.os.Bundle;\n" +
                "import android.util.Log;\n" +
                "import com.yatatsu.autobundle.example.ExampleActivity;\n" +
                "import com.yatatsu.autobundle.example.ExampleActivityAutoBundle;\n" +
                "import com.yatatsu.autobundle.example.ExampleFragment;\n" +
                "import com.yatatsu.autobundle.example.ExampleFragmentAutoBundle;\n" +
                "import java.lang.Object;\n" +
                "import java.lang.Override;\n" +
                "\n" +
                "public final class AutoBundleBindingDispatcher implements AutoBundleBinder {\n" +
                "@Override\n" +
                "public void bind(Object target, Bundle args) {\n" +
                "if (target.getClass().getCanonicalName().equals(ExampleActivity.class.getName())) {\n" +
                "ExampleActivityAutoBundle.bind((ExampleActivity)target, args);\n" +
                "return;\n" +
                "}\n" +
                "if (target.getClass().getCanonicalName().equals(ExampleFragment.class.getName())) {\n" +
                "ExampleFragmentAutoBundle.bind((ExampleFragment)target, args);\n" +
                "return;\n" +
                "}\n" +
                "Log.w(\"AutoBundle\", \"not found binding with \" + target.getClass());\n" +
                "}\n" +
                "\n" +
                "@Override\n" +
                "public void bind(Object target, Intent intent) {\n" +
                "if (target.getClass().getCanonicalName().equals(ExampleActivity.class.getName())) {\n" +
                "ExampleActivityAutoBundle.bind((ExampleActivity)target, intent);\n" +
                "return;\n" +
                "}\n" +
                "Log.w(\"AutoBundle\", \"not found binding with \" + target.getClass());\n" +
                "}\n" +
                "\n" +
                "@Override\n" +
                "public void bind(Object target) {\n" +
                "if (target.getClass().getCanonicalName().equals(ExampleFragment.class.getName())) {\n" +
                "ExampleFragmentAutoBundle.bind((ExampleFragment)target);\n" +
                "return;\n" +
                "}\n" +
                "Log.w(\"AutoBundle\", \"not found binding with \" + target.getClass());\n" +
                "}\n" +
                "\n" +
                "@Override\n" +
                "public void pack(Object target, Bundle args) {\n" +
                "if (target.getClass().getCanonicalName().equals(ExampleActivity.class.getName())) {\n" +
                "ExampleActivityAutoBundle.pack((ExampleActivity)target, args);\n" +
                "return;\n" +
                "}\n" +
                "if (target.getClass().getCanonicalName().equals(ExampleFragment.class.getName())) {\n" +
                "ExampleFragmentAutoBundle.pack((ExampleFragment)target, args);\n" +
                "return;\n" +
                "}\n" +
                "Log.w(\"AutoBundle\", \"not found binding with \" + target.getClass());\n" +
                "}\n" +
                "}\n";
    }
}
