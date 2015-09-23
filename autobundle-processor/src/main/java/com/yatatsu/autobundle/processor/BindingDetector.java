package com.yatatsu.autobundle.processor;


import com.yatatsu.autobundle.Arg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

final class BindingDetector {

    static Map<TypeElement, AutoBundleBindingClass> bindingClasses(RoundEnvironment env,
                                                                   Elements elementUtils) {
        Map<TypeElement, AutoBundleBindingClass> bindingClasses = new HashMap<>();
        Set<? extends Element> elements = env.getElementsAnnotatedWith(Arg.class);
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            if (!bindingClasses.containsKey(typeElement)) {
                AutoBundleBindingClass bindingClass =
                        new AutoBundleBindingClass(typeElement, elementUtils);
                bindingClasses.put(typeElement, bindingClass);
            }
        }
        return bindingClasses;
    }

    static List<AutoBundleBindingArg> findArgFields(Element element,
                                                    boolean required) {
        List<AutoBundleBindingArg> fields = new ArrayList<>();
        for (Element enclosedElement : element.getEnclosedElements()) {
            Arg annotation = enclosedElement.getAnnotation(Arg.class);
            if (annotation != null) {
                AutoBundleBindingArg field =
                        new AutoBundleBindingArg((VariableElement) enclosedElement, annotation);
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
