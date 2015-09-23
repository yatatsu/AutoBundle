package com.yatatsu.autobundle.processor;

import com.google.testing.compile.JavaFileObjects;
import com.yatatsu.autobundle.Arg;
import com.yatatsu.autobundle.processor.data.AllValidType;
import com.yatatsu.autobundle.processor.data.DuplicateKey;
import com.yatatsu.autobundle.processor.data.NotEmptyConstructorConverter;
import com.yatatsu.autobundle.processor.data.NotPublicConstructorConverter;
import com.yatatsu.autobundle.processor.data.NotSupportedFieldType;
import com.yatatsu.autobundle.processor.data.SourceBase;
import com.yatatsu.autobundle.processor.data.ValidFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

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
        expectedException.expectMessage(" is duplicated in " + Arg.class);
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

}