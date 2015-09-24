package com.yatatsu.autobundle.processor;


import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

public class AutoBundleBinderWriter {

    private final List<AutoBundleBindingClass> bindingClasses;
    private static final String TARGET_CLASS_NAME = "AutoBundleBindingDispatcher";
    private static final String TARGET_PACKAGE_NAME = "com.yatatsu.autobundle";
    private static final String TAG_FOR_LOG = "AutoBundle";
    private static final ClassName INTERFACE_NAME =
            ClassName.get("com.yatatsu.autobundle", "AutoBundleBinder");
    private static final ClassName CLASS_BUNDLE = ClassName.get("android.os", "Bundle");
    private static final ClassName CLASS_INTENT = ClassName.get("android.content", "Intent");
    private static final ClassName CLASS_LOG = ClassName.get("android.util", "Log");

    public AutoBundleBinderWriter(List<AutoBundleBindingClass> bindingClasses) {
        this.bindingClasses = bindingClasses;
    }

    public void write(Filer filer) throws IOException {
        TypeSpec clazz = TypeSpec.classBuilder(TARGET_CLASS_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addSuperinterface(INTERFACE_NAME)
                .addMethod(createBindWithArgsMethod(bindingClasses))
                .addMethod(createBindWithIntentMethod(bindingClasses))
                .addMethod(createBindFragmentMethod(bindingClasses))
                .addMethod(createPackMethod(bindingClasses))
                .build();
        JavaFile.builder(TARGET_PACKAGE_NAME, clazz)
                .build()
                .writeTo(filer);
    }

    private static MethodSpec createBindWithArgsMethod(List<AutoBundleBindingClass> classes) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, "target")
                .addParameter(CLASS_BUNDLE, "args")
                .returns(void.class);

        for (AutoBundleBindingClass bindingClass : classes) {
            TypeName type = bindingClass.getTargetType();
            ClassName bindClassType =
                    ClassName.get(bindingClass.getPackageName(), bindingClass.getHelperClassName());
            builder.beginControlFlow("if (target instanceof $T)", type)
                    .addStatement("$T.bind(($T)target, args)", bindClassType, type)
                    .addStatement("return")
                    .endControlFlow();
        }
        builder.addStatement("$T.w($S, $S + target.getClass())",
                CLASS_LOG, TAG_FOR_LOG, "not found binding with");
        return builder.build();
    }

    private static MethodSpec createBindWithIntentMethod(List<AutoBundleBindingClass> classes) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, "target")
                .addParameter(CLASS_INTENT, "intent")
                .returns(void.class);

        for (AutoBundleBindingClass bindingClass : classes) {
            if (bindingClass.getBuilderType() != AutoBundleBindingClass.BuilderType.Intent) {
                continue;
            }
            TypeName type = bindingClass.getTargetType();
            ClassName bindClassType =
                    ClassName.get(bindingClass.getPackageName(), bindingClass.getHelperClassName());
            builder.beginControlFlow("if (target instanceof $T)", type)
                    .addStatement("$T.bind(($T)target, intent)", bindClassType, type)
                    .addStatement("return")
                    .endControlFlow();
        }
        builder.addStatement("$T.w($S, $S + target.getClass())",
                CLASS_LOG, TAG_FOR_LOG, "not found binding with");
        return builder.build();
    }

    private static MethodSpec createBindFragmentMethod(List<AutoBundleBindingClass> classes) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, "target")
                .returns(void.class);

        for (AutoBundleBindingClass bindingClass : classes) {
            if (bindingClass.getBuilderType() != AutoBundleBindingClass.BuilderType.Fragment) {
                continue;
            }
            TypeName type = bindingClass.getTargetType();
            ClassName bindClassType =
                    ClassName.get(bindingClass.getPackageName(), bindingClass.getHelperClassName());
            builder.beginControlFlow("if (target instanceof $T)", type)
                    .addStatement("$T.bind(($T)target)", bindClassType, type)
                    .addStatement("return")
                    .endControlFlow();
        }
        builder.addStatement("$T.w($S, $S + target.getClass())",
                CLASS_LOG, TAG_FOR_LOG, "not found binding with");
        return builder.build();
    }

    private static MethodSpec createPackMethod(List<AutoBundleBindingClass> classes) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("pack")
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Object.class, "target")
                .addParameter(CLASS_BUNDLE, "args")
                .returns(void.class);

        for (AutoBundleBindingClass bindingClass : classes) {
            TypeName type = bindingClass.getTargetType();
            ClassName bindClassType =
                    ClassName.get(bindingClass.getPackageName(), bindingClass.getHelperClassName());
            builder.beginControlFlow("if (target instanceof $T)", type)
                    .addStatement("$T.bind(($T)target, args)", bindClassType, type)
                    .addStatement("return")
                    .endControlFlow();
        }
        builder.addStatement("$T.w($S, $S + target.getClass())",
                CLASS_LOG, TAG_FOR_LOG, "not found binding with");
        return builder.build();
    }
}
