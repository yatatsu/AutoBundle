package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.TypeName;
import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.processor.exceptions.DuplicatedArgKeyException;
import com.yatatsu.autobundle.processor.exceptions.UnsupportedClassException;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Validator
 */
final class Validator {

    static void checkAutoBundleTargetClass(AutoBundleBindingClass.BuilderType type) {
        if (type == AutoBundleBindingClass.BuilderType.None) {
            throw new UnsupportedClassException(
                    "AutoBundle target class must be subtype of" +
                            " 'Fragment', 'Activity', 'Receiver' or 'Service'.");
        }
    }

    static void checkAutoBundleTargetModifier(TypeElement typeElement) {
        Set<Modifier> modifiers = typeElement.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new UnsupportedClassException(
                    "AutoBundle must be with concrete class.");
        } else if (typeElement.getModifiers().contains(Modifier.PRIVATE) ||
                modifiers.contains(Modifier.PROTECTED)) {
            throw new UnsupportedClassException(
                    "AutoBundle must not be with private/protected class.");
        }
    }

    static void checkDuplicatedArgsKey(List<AutoBundleBindingField> args) {
        Set<String> keySet = new HashSet<>();
        for (AutoBundleBindingField arg : args) {
            String key = arg.getArgKey();
            if (keySet.contains(key)) {
                throw new DuplicatedArgKeyException(
                        "key " + key  + " is duplicated in " + AutoBundleField.class);
            }
            keySet.add(key);
        }
    }

    static void checkAutoBundleFieldModifier(VariableElement element) {
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) ||
                modifiers.contains(Modifier.PROTECTED)) {
            throw new IllegalStateException(
                    AutoBundleField.class + " must not be with private/protected class.");
        }
    }

    static void checkConverterClass(Class clazz) {
        // check public
        if (!java.lang.reflect.Modifier.isPublic(clazz.getModifiers())) {
            throw new IllegalStateException(clazz + " must be public.");
        }
        // check empty public constructor
        boolean existEmptyConstructor = false;
        for (Constructor constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == 0 &&
                    java.lang.reflect.Modifier.isPublic(constructor.getModifiers())) {
                existEmptyConstructor = true;
                break;
            }
        }
        if (!existEmptyConstructor) {
            throw new IllegalStateException(clazz + " must have public empty constructor.");
        }
    }

    static void checkConverterClass(TypeElement element) {
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            throw new IllegalStateException(element.toString() + " must be public.");
        }

        boolean existEmptyConstructor = false;
        for (Element e : element.getEnclosedElements()) {
            if (e.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement executableElement = (ExecutableElement) e;
                if ((executableElement.getParameters() == null ||
                        executableElement.getParameters().isEmpty()) &&
                        executableElement.getModifiers().contains(Modifier.PUBLIC)) {
                    existEmptyConstructor = true;
                    break;
                }
            }
        }
        if (!existEmptyConstructor) {
            throw new IllegalStateException(element + " must have public empty constructor.");
        }
    }

    static void checkNotSupportedOperation(String operation, TypeName typeName) {
        if (operation == null || operation.isEmpty()) {
            throw new UnsupportedClassException(typeName + " is not supported type.");
        }
    }

    Validator() {
        throw new AssertionError("no instances");
    }
}
