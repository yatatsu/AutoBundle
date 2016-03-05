package com.yatatsu.autobundle.processor;


import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.AutoBundleGetter;
import com.yatatsu.autobundle.AutoBundleSetter;
import com.yatatsu.autobundle.processor.exceptions.UnsupportedClassException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

final class BindingDetector {

    static List<AutoBundleBindingClass> bindingClasses(RoundEnvironment env,
                                                       Elements elementUtils,
                                                       Types typeUtils) {
        ArrayList<AutoBundleBindingClass> bindingClasses = new ArrayList<>();
        Set<String> keySet = new HashSet<>();
        Set<? extends Element> elements = env.getElementsAnnotatedWith(AutoBundleField.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            if (!keySet.contains(typeElement.getSimpleName().toString())) {
                AutoBundleBindingClass bindingClass =
                        new AutoBundleBindingClass(typeElement, elementUtils, typeUtils);
                bindingClasses.add(bindingClass);
                keySet.add(typeElement.getSimpleName().toString());
            }
        }
        return bindingClasses;
    }

    static List<AutoBundleBindingField> findArgFields(Element element,
                                                      boolean required,
                                                      Elements elementUtils,
                                                      Types typeUtils) {
        List<AutoBundleBindingField> fields = new ArrayList<>();
        for (Element enclosedElement : element.getEnclosedElements()) {
            AutoBundleField annotation = enclosedElement.getAnnotation(AutoBundleField.class);
            if (annotation != null) {
                VariableElement fieldElement = (VariableElement) enclosedElement;
                final String getter = findGetterMethod(element, fieldElement, annotation);
                final String setter = findSetterMethod(element, fieldElement, annotation);
                AutoBundleBindingField field = new AutoBundleBindingField(
                        fieldElement, annotation, elementUtils, typeUtils, getter, setter);
                if (required == field.isRequired()) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    private static String findGetterMethod(Element classElement,
                                   VariableElement fieldElement,
                                   AutoBundleField fieldAnnotation) {
        final String operation = "get";
        final String argKeyName = fieldAnnotation.key().length() > 0
                ? fieldAnnotation.key() : fieldElement.toString();
        for (Element element : classElement.getEnclosedElements()) {
            final String methodName = element.getSimpleName().toString();
            AutoBundleGetter annotation = element.getAnnotation(AutoBundleGetter.class);
            if (annotation != null) {
                // annotated getter
                if (argKeyName.equals(annotation.keyName())) {
                    // check modifier
                    if (element.getModifiers().contains(Modifier.PRIVATE)) {
                        throw new UnsupportedClassException("@AutoBundleGetter must not be private");
                    }
                    return methodName;
                }
            } else {
                // default getter
                if (ElementKind.METHOD == element.getKind() &&
                        !element.getModifiers().contains(Modifier.PRIVATE) &&
                        element.getSimpleName().toString().startsWith(operation)) {
                    if (methodName.equals(
                            createOperationMethod(operation, argKeyName))) {
                        int methodModifierLevel = ModifierHelper.getModifierLevel(element);
                        int fieldModifierLevel = ModifierHelper.getModifierLevel(fieldElement);
                        if (methodModifierLevel - fieldModifierLevel >= 0) {
                            return methodName;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static String findSetterMethod(Element classElement,
                                           VariableElement fieldElement,
                                           AutoBundleField fieldAnnotation) {
        final String operation = "set";
        final String argKeyName = fieldAnnotation.key().length() > 0
                ? fieldAnnotation.key() : fieldElement.toString();
        for (Element element : classElement.getEnclosedElements()) {
            final String methodName = element.getSimpleName().toString();
            AutoBundleSetter annotation = element.getAnnotation(AutoBundleSetter.class);
            if (annotation != null) {
                // annotated setter
                if (argKeyName.equals(annotation.keyName())) {
                    // check modifier
                    if (element.getModifiers().contains(Modifier.PRIVATE)) {
                        throw new UnsupportedClassException("@AutoBundleSetter must not be private");
                    }
                    return methodName;
                }
            } else {
                // default setter
                if (ElementKind.METHOD == element.getKind() &&
                        !element.getModifiers().contains(Modifier.PRIVATE) &&
                        element.getSimpleName().toString().startsWith(operation)) {
                    if (methodName.equals(
                            createOperationMethod(operation, argKeyName))) {
                        int methodModifierLevel = ModifierHelper.getModifierLevel(element);
                        int fieldModifierLevel = ModifierHelper.getModifierLevel(fieldElement);
                        if (methodModifierLevel - fieldModifierLevel >= 0) {
                            return methodName;
                        }
                    }
                }
            }
        }
        return null;
    }

    static String getPackageName(Elements elementsUtils, TypeElement type) {
        return elementsUtils.getPackageOf(type).getQualifiedName().toString();
    }

    static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private static String createOperationMethod(String operation, String keyName) {
        String capitalized = Character.toUpperCase(keyName.charAt(0)) + keyName.substring(1);
        return operation + capitalized;
    }

    private BindingDetector() {
        throw new AssertionError("no instances");
    }
}
