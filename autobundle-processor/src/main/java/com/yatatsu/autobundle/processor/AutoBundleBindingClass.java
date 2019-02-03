package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;


class AutoBundleBindingClass {
    private static final String FRAGMENT = "android.app.Fragment";
    private static final String SUPPORT_FRAGMENT = "androidx.fragment.app.Fragment";
    private static final String ACTIVITY = "android.app.Activity";
    private static final String BROADCAST_RECEIVER = "android.content.BroadcastReceiver";
    private static final String SERVICE = "android.app.Service";

    enum BuilderType {
        Intent, Fragment, Any;

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
            return Any;
        }
    }

    private final ClassName targetType;
    private final String packageName;
    private final String className;
    private final BuilderType builderType;
    private final List<AutoBundleBindingField> requiredArgs;
    private final List<AutoBundleBindingField> notRequiredArgs;

    AutoBundleBindingClass(TypeElement typeElement,
                           Elements elementsUtils,
                           Types typeUtils) {
        this.targetType = ClassName.get(typeElement);
        Validator.checkAutoBundleTargetModifier(typeElement);
        this.packageName = BindingDetector.getPackageName(elementsUtils, typeElement);
        this.className = BindingDetector.getClassName(typeElement, this.packageName);
        this.builderType = BuilderType.byTypeName(typeElement, elementsUtils, typeUtils);
        this.requiredArgs = BindingDetector
                .findArgFields(typeElement, true, elementsUtils, typeUtils);
        this.notRequiredArgs = BindingDetector
                .findArgFields(typeElement, false, elementsUtils, typeUtils);
        List<AutoBundleBindingField> args = new ArrayList<>();
        args.addAll(requiredArgs);
        args.addAll(notRequiredArgs);
        Validator.checkDuplicatedArgsKey(args);
    }

    ClassName getTargetType() {
        return targetType;
    }

    String getPackageName() {
        return packageName;
    }

    BuilderType getBuilderType() {
        return builderType;
    }

    List<AutoBundleBindingField> getRequiredArgs() {
        return requiredArgs;
    }

    List<AutoBundleBindingField> getNotRequiredArgs() {
        return notRequiredArgs;
    }

    String getHelperClassName() {
        return className + "AutoBundle";
    }
}
