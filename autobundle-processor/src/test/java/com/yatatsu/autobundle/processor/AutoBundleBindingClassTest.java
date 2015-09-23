package com.yatatsu.autobundle.processor;

import com.yatatsu.autobundle.processor.exceptions.UnsupportedClassException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * {@link AutoBundleBindingClass}
 */
public class AutoBundleBindingClassTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void validBindingClassName() {
        AutoBundleBindingClass.BuilderType builderType1 =
                AutoBundleBindingClass.BuilderType.byName("FooActivity");
        AutoBundleBindingClass.BuilderType builderType2 =
                AutoBundleBindingClass.BuilderType.byName("FooFragment");
        AutoBundleBindingClass.BuilderType builderType3 =
                AutoBundleBindingClass.BuilderType.byName("FooService");
        AutoBundleBindingClass.BuilderType builderType4 =
                AutoBundleBindingClass.BuilderType.byName("FooBroadcastReceiver");
    }

    @Test
    public void invalidBindingClassName1() {
        expectedException.expect(UnsupportedClassException.class);
        expectedException.expectMessage(
                "AutoBundle must be end with 'Fragment', 'Activity', 'Receiver' or 'Service'.");
        AutoBundleBindingClass.BuilderType builderType =
                AutoBundleBindingClass.BuilderType.byName("ActivityFoo");
    }

    @Test
    public void invalidBindingClassName2() {
        expectedException.expect(UnsupportedClassException.class);
        expectedException.expectMessage(
                "AutoBundle must be end with 'Fragment', 'Activity', 'Receiver' or 'Service'.");
        AutoBundleBindingClass.BuilderType builderType =
                AutoBundleBindingClass.BuilderType.byName("FragmentFoo");
    }
}