package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.TypeName;
import com.yatatsu.autobundle.processor.exceptions.ProcessingException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * test {@link Validator}
 */
public class ValidatorTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void invalidSupportedClass() {
        expectedException.expect(ProcessingException.class);
        expectedException.expectMessage(" is not supported type.");
        Validator.checkNotSupportedOperation(null, TypeName.BOOLEAN);
    }
}