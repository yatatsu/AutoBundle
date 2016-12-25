package com.yatatsu.autobundle.processor;

import com.google.testing.compile.JavaFileObjects;

import org.junit.Test;

import javax.tools.JavaFileObject;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

/**
 * {@link AutoBundleProcessor}
 */
public class AutoBundleProcessorTest {

    @Test
    public void testProcessorAllValidType() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("AllType.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .compilesWithoutError();
    }

    @Test
    public void testProcessorFragment() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("ValidFragment.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .compilesWithoutError();
    }

    @Test
    public void testDuplicateKey() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("DuplicateKey.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("is duplicated in interface com.yatatsu.autobundle.AutoBundleField");
    }

    @Test
    public void testNotPublicConstructorConverter() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("NotPublicConstructorConverter.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining(" must have public empty constructor.");
    }

    @Test
    public void testNotEmptyConstructorConverter() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("NotEmptyConstructorConverter.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining(" must have public empty constructor.");
    }

    @Test
    public void testNotSupportedFieldType() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("UnsupportedTypeField.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining(" is not supported type.");
    }

    @Test
    public void testWrongSuperClass() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("UnsupportedSubtype.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("AutoBundle target class must be subtype of" +
                        " 'Fragment', 'Activity', 'Receiver' or 'Service'.");
    }

    @Test
    public void testPrivateAnnotatedGetter() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("PrivateAnnotatedGetter.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("@AutoBundleGetter must not be private");
    }

    @Test
    public void testPrivateAnnotatedSetter() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("PrivateAnnotatedSetter.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining("@AutoBundleSetter must not be private");
    }

    @Test
    public void testPrivateFieldWithoutGetterAndSetter() throws Exception {
        JavaFileObject source = JavaFileObjects.forResource("PrivateFieldWithoutGetterSetter.java");

        assert_().about(javaSource())
                .that(source)
                .processedWith(new AutoBundleProcessor())
                .failsToCompile()
                .withErrorCount(1)
                .withErrorContaining(" does not support private field without setter/getter.");
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