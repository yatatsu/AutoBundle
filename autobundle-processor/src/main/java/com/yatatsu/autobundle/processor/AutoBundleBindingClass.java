package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;
import com.yatatsu.autobundle.processor.exceptions.UnsupportedClassException;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;


public class AutoBundleBindingClass {

    public enum BuilderType {
        Intent, Fragment,;

        public static BuilderType byName(String clazzName) {
            if (clazzName.endsWith("Fragment")) {
                return Fragment;
            } else if (clazzName.endsWith("Activity") ||
                    clazzName.endsWith("Receiver") ||
                    clazzName.endsWith("Service")) {
                return Intent;
            }
            throw new UnsupportedClassException(
                    "AutoBundle must be end with 'Fragment', 'Activity', 'Receiver' or 'Service'."
                            + clazzName);
        }
    }

    private final ClassName targetType;
    private final String packageName;
    private final String className;
    private final BuilderType builderType;
    private final List<AutoBundleBindingArg> requiredArgs;
    private final List<AutoBundleBindingArg> notRequiredArgs;

    public AutoBundleBindingClass(TypeElement typeElement,
                                  Elements elementsUtils) {
        this.targetType = ClassName.get(typeElement);
        Validator.checkAutoBundleTargetModifier(typeElement);
        this.packageName = BindingDetector.getPackageName(elementsUtils, typeElement);
        this.className = BindingDetector.getClassName(typeElement, this.packageName);
        this.builderType = BuilderType.byName(className);

        this.requiredArgs = BindingDetector.findArgFields(typeElement, true);
        this.notRequiredArgs = BindingDetector.findArgFields(typeElement, false);
        List<AutoBundleBindingArg> args = new ArrayList<>();
        args.addAll(requiredArgs);
        args.addAll(notRequiredArgs);
        Validator.checkDuplicatedArgsKey(args);
    }

    public ClassName getTargetType() {
        return targetType;
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
