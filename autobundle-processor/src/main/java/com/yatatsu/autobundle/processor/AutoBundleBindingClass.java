package com.yatatsu.autobundle.processor;

import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


public class AutoBundleBindingClass {

    public enum BuilderType {
        Intent, Fragment, None,
        ;

        public static BuilderType byName(String clazzName) {
            if (clazzName.endsWith("Fragment")) {
                return Fragment;
            } else if (clazzName.endsWith("Activity") ||
                    clazzName.endsWith("Receiver") ||
                    clazzName.endsWith("Service")) {
                return Intent;
            }
            return None;
        }
    }

    private final TypeElement typeElement;
    private final String packageName;
    private final String className;
    private final List<AutoBundleBindingArg> requiredArgs;
    private final List<AutoBundleBindingArg> notRequiredArgs;
    private final BuilderType builderType;

    public AutoBundleBindingClass(TypeElement typeElement, Elements elementsUtils) {
        this.typeElement = typeElement;
        this.packageName = AutoBundleDetector.getPackageName(elementsUtils, typeElement);
        this.className = AutoBundleDetector.getClassName(typeElement, this.packageName);
        this.requiredArgs = AutoBundleDetector.findArgFields(typeElement, true);
        this.notRequiredArgs = AutoBundleDetector.findArgFields(typeElement, false);
        this.builderType = BuilderType.byName(className);
        // TODO: add validations.
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public BuilderType getBuilderType() {
        return builderType;
    }

    public List<AutoBundleBindingArg> getRequiredArgs() {
        return requiredArgs;
    }

    public List<AutoBundleBindingArg> getNotRequiredArgs() {
        return notRequiredArgs;
    }

    public String getBuilderClassName() {
        return builderType.name() + "Builder";
    }

    public String getHelperClassName() {
        return className + "AutoBundle";
    }
}
