package com.yatatsu.autobundle.processor;

import com.google.testing.compile.JavaFileObjects;
import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.processor.data.AllValidType;
import com.yatatsu.autobundle.processor.data.BinderDispatcherSource;
import com.yatatsu.autobundle.processor.data.DuplicateKey;
import com.yatatsu.autobundle.processor.data.NotEmptyConstructorConverter;
import com.yatatsu.autobundle.processor.data.NotPublicConstructorConverter;
import com.yatatsu.autobundle.processor.data.NotSupportedFieldType;
import com.yatatsu.autobundle.processor.data.SourceBase;
import com.yatatsu.autobundle.processor.data.ValidFragment;
import com.yatatsu.autobundle.processor.data.WrongSuperClass;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

/**
 * {@link AutoBundleProcessor}
 */
public class AutoBundleProcessorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testProcessorAllValidType() {
        assertGenerateCode(new AllValidType());
    }

    @Test
    public void testProcessorFragment() {
        assertGenerateCode(new ValidFragment());
    }

    @Test
    public void testDuplicateKey() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(" is duplicated in " + AutoBundleField.class);
        assertGenerateCode(new DuplicateKey());
    }

    @Test
    public void testNotPublicConstructorConverter() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(" must have public empty constructor.");
        assertGenerateCode(new NotPublicConstructorConverter());
    }

    @Test
    public void testNotEmptyConstructorConverter() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(" must have public empty constructor.");
        assertGenerateCode(new NotEmptyConstructorConverter());
    }

    @Test
    public void testNotSupportedFieldType() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage(" is not supported type.");
        assertGenerateCode(new NotSupportedFieldType());
    }

    @Test
    public void testWrongSuperClass() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("AutoBundle target class must be subtype of" +
                " 'Fragment', 'Activity', 'Receiver' or 'Service'.");
        assertGenerateCode(new WrongSuperClass());
    }

    @Test
    public void testBindingDispatcher() {
        List<JavaFileObject> sources = new ArrayList<>();
        BinderDispatcherSource base = new BinderDispatcherSource();
        sources.add(createJavaFileObject(base.getTargetClassName(), base.getTargetSource()));
        sources.add(createJavaFileObject(base.getTargetClassName2(), base.getTargetSource2()));
        JavaFileObject expect = createJavaFileObject(
                base.getExpectClassName(), base.getExpectSource());
        assertJavaSources(sources, expect);
    }

    private void assertGenerateCode(SourceBase sourceBase) {
        JavaFileObject source = createJavaFileObject(
                sourceBase.getTargetClassName(), sourceBase.getTargetSource());
        JavaFileObject expect = createJavaFileObject(
                sourceBase.getExpectClassName(), sourceBase.getExpectSource());
        assertJavaSource(source, expect);
    }

    private JavaFileObject createJavaFileObject(String className, String source) {
        return JavaFileObjects.forSourceString(className, source);
    }

    private void assertJavaSource(JavaFileObject source, JavaFileObject expected) {
        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected);
    }

    private void assertJavaSources(List<JavaFileObject> sources,
                                   JavaFileObject expected,
                                   JavaFileObject... rest) {
        assert_().about(javaSources())
                .that(sources)
                .processedWith(new AutoBundleProcessor())
                .compilesWithoutError()
                .and()
                .generatesSources(expected, rest);
    }

}