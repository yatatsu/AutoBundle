package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.yatatsu.autobundle.Arg;
import com.yatatsu.autobundle.AutoBundleTarget;
import com.yatatsu.autobundle.processor.exceptions.DuplicatedArgKeyException;
import com.yatatsu.autobundle.processor.exceptions.UnsupportedClassException;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
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

    static final List<TypeName> allowTypes
            = Collections.unmodifiableList(new ArrayList<TypeName>() {
        {
            add(TypeName.BOOLEAN);
            add(TypeName.BYTE);
            add(TypeName.CHAR);
            add(TypeName.DOUBLE);
            add(TypeName.FLOAT);
            add(TypeName.LONG);
            add(TypeName.INT);
            add(TypeName.SHORT);

            add(ClassName.get("java.lang", "CharSequence"));
            add(ClassName.get("java.lang", "String"));
            add(ClassName.get("java.io", "Serializable"));
            add(ClassName.get("android.os", "Bundle"));
            add(ClassName.get("android.os", "IBinder"));
            add(ClassName.get("android.os", "Parcelable"));

            add(ParameterizedTypeName.get(ArrayList.class, Integer.class));
            add(ParameterizedTypeName.get(ArrayList.class, String.class));
            add(ParameterizedTypeName.get(ArrayList.class, CharSequence.class));
            add(ParameterizedTypeName.get(ClassName.get("java.util", "ArrayList"),
                    ClassName.get("android.os", "Parcelable")));
            add(ParameterizedTypeName.get(ClassName.get("android.util", "SparseArray"),
                    ClassName.get("android.os", "Parcelable")));
        }
    });

    static final List<String> allowConvertedArrayTypes =
            Collections.unmodifiableList(new ArrayList<String>() {
        {
            add("boolean");
            add("byte");
            add("char");
            add("CharSequence");
            add("double");
            add("float");
            add("int");
            add("long");
            add("short");
            add("String");
            add("Parcelable");
        }
    });

    static final List<TypeName> allowConvertedTypes =
            Collections.unmodifiableList(new ArrayList<TypeName>() {
        {
            add(ClassName.get("java.lang", "Boolean"));
            add(ClassName.get("java.lang", "Byte"));
            add(ClassName.get("java.lang", "Char"));
            add(ClassName.get("java.lang", "CharSequence"));
            add(ClassName.get("java.lang", "Double"));
            add(ClassName.get("java.lang", "Float"));
            add(ClassName.get("java.lang", "Integer"));
            add(ClassName.get("java.lang", "Long"));
            add(ClassName.get("java.lang", "Short"));
            add(ClassName.get("java.lang", "String"));

            add(ClassName.get("java.io", "Serializable"));

            add(ClassName.get("android.os", "Bundle"));
            add(ClassName.get("android.os", "IBinder"));
            add(ClassName.get("android.os", "Parcelable"));

            add(ParameterizedTypeName.get(ArrayList.class, Integer.class));
            add(ParameterizedTypeName.get(ArrayList.class, String.class));
            add(ParameterizedTypeName.get(ArrayList.class, CharSequence.class));
            add(ParameterizedTypeName.get(ClassName.get("java.util", "ArrayList"),
                    ClassName.get("android.os", "Parcelable")));
            add(ParameterizedTypeName.get(ClassName.get("android.util", "SparseArray"),
                    ClassName.get("android.os", "Parcelable")));
        }
    });

    static void checkAutoBundleTargetModifier(TypeElement typeElement) {
        Set<Modifier> modifiers = typeElement.getModifiers();
        if (modifiers.contains(Modifier.ABSTRACT)) {
            throw new UnsupportedClassException(
                    AutoBundleTarget.class + " must be with concrete class.");
        } else if (typeElement.getModifiers().contains(Modifier.PRIVATE) ||
                modifiers.contains(Modifier.PROTECTED)) {
            throw new UnsupportedClassException(
                    AutoBundleTarget.class + " must not be with private/protected class.");
        }
    }

    static void checkDuplicatedArgsKey(List<AutoBundleBindingArg> args) {
        Set<String> keySet = new HashSet<>();
        for (AutoBundleBindingArg arg : args) {
            String key = arg.getArgKey();
            if (keySet.contains(key)) {
                throw new DuplicatedArgKeyException(
                        "key " + key  + " is duplicated in " + Arg.class);
            }
            keySet.add(key);
        }
    }

    static void checkAutoBundleFieldModifier(VariableElement element) {
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) ||
                modifiers.contains(Modifier.PROTECTED)) {
            throw new IllegalStateException(
                    Arg.class + " must not be with private/protected class.");
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

    static void checkNotSupportedConvertClass(TypeName type) {
        String className = type.toString().substring(type.toString().lastIndexOf(".") + 1);
        if (className.endsWith("[]") || type.isPrimitive()) {
            if (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
            }
            if (!allowConvertedArrayTypes.contains(className)) {
                throw new UnsupportedClassException(type + " is not supported type.");
            }
        } else if (!allowConvertedTypes.contains(type)) {
            throw new UnsupportedClassException(type + " is not supported type.");
        }
    }

    static void checkNotSupportedClass(TypeName type) {
        String className = type.toString();
        if (className.endsWith("[]")) {
            className = className.substring(0, className.length() - 2);
        }
        for (TypeName t : allowTypes) {
            if (t.toString().equals(className)) {
                return;
            }
        }
        throw new UnsupportedClassException(type + " is not supported type.");
    }

    Validator() {
        throw new AssertionError("no instances");
    }
}
