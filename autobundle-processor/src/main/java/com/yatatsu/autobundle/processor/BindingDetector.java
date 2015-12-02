package com.yatatsu.autobundle.processor;


import com.yatatsu.autobundle.AutoBundleField;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
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
                AutoBundleBindingField field =
                        new AutoBundleBindingField((VariableElement) enclosedElement,
                                annotation, elementUtils, typeUtils);
                if (!(required ^ field.isRequired())) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    static String getPackageName(Elements elementsUtils, TypeElement type) {
        return elementsUtils.getPackageOf(type).getQualifiedName().toString();
    }

    static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace('.', '$');
    }

    private BindingDetector() {
        throw new AssertionError("no instances");
    }
}
