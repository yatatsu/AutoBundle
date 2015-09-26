package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


public class AutoBundleBindingClass {
    private static final String FRAGMENT = "android.app.Fragment";
    private static final String SUPPORT_FRAGMENT = "android.support.v4.app.Fragment";
    private static final String ACTIVITY = "android.app.Activity";
    private static final String BROADCAST_RECEIVER = "android.content.BroadcastReceiver";
    private static final String SERVICE = "android.app.Service";

    public enum BuilderType {
        Intent, Fragment, None;

        public static BuilderType byTypeName(Element element,
                                             Elements elementUtils,
                                             Types typeUtils) {
            TypeMirror targetType = element.asType();
            TypeMirror fragmentType = elementUtils.getTypeElement(FRAGMENT).asType();
            TypeElement supportFragmentTypeElem = elementUtils.getTypeElement(SUPPORT_FRAGMENT);
            TypeMirror activityType = elementUtils.getTypeElement(ACTIVITY).asType();
            TypeMirror broadcastReceiverType =
                    elementUtils.getTypeElement(BROADCAST_RECEIVER).asType();
            TypeMirror serviceType = elementUtils.getTypeElement(SERVICE).asType();
            if (typeUtils.isSubtype(targetType, fragmentType)) {
                return Fragment;
            }
            if (supportFragmentTypeElem != null &&
                    typeUtils.isSubtype(targetType, supportFragmentTypeElem.asType())) {
                return Fragment;
            }
            if (typeUtils.isSubtype(targetType, activityType) ||
                    typeUtils.isSubtype(targetType, broadcastReceiverType) ||
                    typeUtils.isSubtype(targetType, serviceType)) {
                return Intent;
            }
            return None;
        }
    }

    private final ClassName targetType;
    private final String packageName;
    private final String className;
    private final BuilderType builderType;
    private final List<AutoBundleBindingField> requiredArgs;
    private final List<AutoBundleBindingField> notRequiredArgs;

    public AutoBundleBindingClass(TypeElement typeElement,
                                  Elements elementsUtils,
                                  Types typeUtils) {
        this.targetType = ClassName.get(typeElement);
        Validator.checkAutoBundleTargetModifier(typeElement);
        this.packageName = BindingDetector.getPackageName(elementsUtils, typeElement);
        this.className = BindingDetector.getClassName(typeElement, this.packageName);
        this.builderType = BuilderType.byTypeName(typeElement, elementsUtils, typeUtils);
        Validator.checkAutoBundleTargetClass(builderType);
        this.requiredArgs = BindingDetector.findArgFields(typeElement, true);
        this.notRequiredArgs = BindingDetector.findArgFields(typeElement, false);
        List<AutoBundleBindingField> args = new ArrayList<>();
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

    public List<AutoBundleBindingField> getRequiredArgs() {
        return requiredArgs;
    }

    public List<AutoBundleBindingField> getNotRequiredArgs() {
        return notRequiredArgs;
    }

    public String getBuilderClassName() {
        return builderType.name() + "Builder";
    }

    public String getHelperClassName() {
        return className + "AutoBundle";
    }
}
