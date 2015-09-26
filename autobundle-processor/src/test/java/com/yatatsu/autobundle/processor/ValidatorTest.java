package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.yatatsu.autobundle.processor.exceptions.UnsupportedClassException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Date;

/**
 * test {@link Validator}
 */
public class ValidatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void invalidBuilderType() {
        expectedException.expect(UnsupportedClassException.class);
        expectedException.expectMessage("AutoBundle target class must be subtype of" +
                " 'Fragment', 'Activity', 'Receiver' or 'Service'.");
        Validator.checkAutoBundleTargetClass(AutoBundleBindingClass.BuilderType.None);
    }

    @Test
    public void validConvertedClass() {
        Validator.checkNotSupportedConvertClass(TypeName.get(int.class));
        Validator.checkNotSupportedConvertClass(TypeName.get(int[].class));
        Validator.checkNotSupportedConvertClass(TypeName.get(String.class));
        Validator.checkNotSupportedConvertClass(TypeName.get(Long.class));
        Validator.checkNotSupportedConvertClass(
                ParameterizedTypeName.get(ArrayList.class, Integer.class));
        Validator.checkNotSupportedConvertClass(
                ParameterizedTypeName.get(ArrayList.class, CharSequence.class));
        Validator.checkNotSupportedConvertClass(ClassName.get("android.os", "Parcelable"));
    }

    @Test
    public void invalidConvertedClassForNotAllowedClass() {
        expectedException.expect(UnsupportedClassException.class);
        expectedException.expectMessage(" is not supported type.");
        Validator.checkNotSupportedConvertClass(TypeName.get(Date.class));
    }

    @Test
    public void invalidConvertedClassForNotAllowedArray() {
        expectedException.expect(UnsupportedClassException.class);
        expectedException.expectMessage(" is not supported type.");
        Validator.checkNotSupportedConvertClass(TypeName.get(Boolean[].class));
    }
}