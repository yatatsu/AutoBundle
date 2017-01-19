package com.yatatsu.autobundle.processor;


import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;

class AutoBundleBinderWriter {

  private final List<AutoBundleBindingClass> bindingClasses;
  private final String packageName;
  private final LinkedHashMap<String, SubDispatcherHolder> subDispatcherHolderMap;
  private static final String TARGET_CLASS_NAME = "AutoBundleBindingDispatcher";
  private static final String TARGET_PACKAGE_NAME = "com.yatatsu.autobundle";
  private static final ClassName CLASS_BUNDLE = ClassName.get("android.os", "Bundle");
  private static final ClassName CLASS_DISPATCHER = ClassName.get(TARGET_PACKAGE_NAME, "AutoBundleDispatcher");

  private static final AnnotationSpec ANNOTATION_NONNULL
          = AnnotationSpec.builder(ClassName.get("android.support.annotation", "NonNull")).build();

  AutoBundleBinderWriter(List<AutoBundleBindingClass> bindingClasses,
                         List<SubDispatcherHolder> subDispatcherHolders,
                         String packageAsLibrary) {
    this.bindingClasses = bindingClasses;
    this.subDispatcherHolderMap = subDispatcherHolders.stream()
            .collect(LinkedHashMap::new,
                    (map, holder) -> map.put("subDispatcher" + map.size(), holder),
                    HashMap::putAll);
    boolean subDispatcher = packageAsLibrary != null;
    this.packageName = subDispatcher ? packageAsLibrary : TARGET_PACKAGE_NAME;
  }

  void write(Filer filer) throws IOException {
    TypeSpec clazz = TypeSpec.classBuilder(TARGET_CLASS_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(CLASS_DISPATCHER)
            .addFields(createSubDispatcherField(subDispatcherHolderMap))
            .addMethod(createBindWithArgsMethod(bindingClasses, subDispatcherHolderMap))
            .addMethod(createBindFragmentMethod(bindingClasses, subDispatcherHolderMap))
            .addMethod(createPackMethod(bindingClasses, subDispatcherHolderMap))
            .build();
    JavaFile.builder(packageName, clazz)
            .build()
            .writeTo(filer);
  }

  private static List<FieldSpec> createSubDispatcherField(Map<String, SubDispatcherHolder> map) {
    return map.entrySet().stream()
            .map(entry -> FieldSpec.builder(CLASS_DISPATCHER, entry.getKey(), Modifier.PRIVATE, Modifier.FINAL)
            .initializer("new $T()", entry.getValue().getName())
            .build())
            .collect(Collectors.toList());
  }

  private static MethodSpec createBindWithArgsMethod(List<AutoBundleBindingClass> classes,
                                                     Map<String, SubDispatcherHolder> subDispatcherHolderMap) {
    MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(ParameterSpec.builder(Object.class, "target")
                    .addAnnotation(ANNOTATION_NONNULL).build())
            .addParameter(ParameterSpec.builder(CLASS_BUNDLE, "args")
                    .addAnnotation(ANNOTATION_NONNULL).build())
            .returns(boolean.class);

    for (AutoBundleBindingClass bindingClass : classes) {
      TypeName type = bindingClass.getTargetType();
      ClassName bindClassType =
              ClassName.get(bindingClass.getPackageName(), bindingClass.getHelperClassName());
      builder.beginControlFlow("if (target instanceof $T)", type)
              .addStatement("$T.bind(($T)target, args)", bindClassType, type)
              .addStatement("return true")
              .endControlFlow();
    }
    subDispatcherHolderMap.entrySet().stream()
            .forEach(entry -> builder.beginControlFlow("if ($N.bind(target, args))", entry.getKey())
                    .addStatement("return true")
                    .endControlFlow());
    return builder.addStatement("return false").build();
  }

  private static MethodSpec createBindFragmentMethod(List<AutoBundleBindingClass> classes,
                                                     Map<String, SubDispatcherHolder> subDispatcherHolderMap) {
    MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(ParameterSpec.builder(Object.class, "target")
                    .addAnnotation(ANNOTATION_NONNULL).build())
            .returns(boolean.class);

    for (AutoBundleBindingClass bindingClass : classes) {
      if (bindingClass.getBuilderType() != AutoBundleBindingClass.BuilderType.Fragment) {
        continue;
      }
      TypeName type = bindingClass.getTargetType();
      ClassName bindClassType =
              ClassName.get(bindingClass.getPackageName(), bindingClass.getHelperClassName());
      builder.beginControlFlow("if (target instanceof $T)", type)
              .addStatement("$T.bind(($T)target)", bindClassType, type)
              .addStatement("return true")
              .endControlFlow();
    }
    subDispatcherHolderMap.entrySet().stream()
            .forEach(entry -> builder.beginControlFlow("if ($N.bind(target))", entry.getKey())
                    .addStatement("return true")
                    .endControlFlow());
    return builder.addStatement("return false").build();
  }

  private static MethodSpec createPackMethod(List<AutoBundleBindingClass> classes,
                                             Map<String, SubDispatcherHolder> subDispatcherHolderMap) {
    MethodSpec.Builder builder = MethodSpec.methodBuilder("pack")
            .addModifiers(Modifier.PUBLIC)
            .addAnnotation(Override.class)
            .addParameter(ParameterSpec.builder(Object.class, "target")
                    .addAnnotation(ANNOTATION_NONNULL).build())
            .addParameter(ParameterSpec.builder(CLASS_BUNDLE, "args")
                    .addAnnotation(ANNOTATION_NONNULL).build())
            .returns(boolean.class);

    for (AutoBundleBindingClass bindingClass : classes) {
      TypeName type = bindingClass.getTargetType();
      ClassName bindClassType =
              ClassName.get(bindingClass.getPackageName(), bindingClass.getHelperClassName());
      builder.beginControlFlow("if (target instanceof $T)", type)
              .addStatement("$T.pack(($T)target, args)", bindClassType, type)
              .addStatement("return true")
              .endControlFlow();
    }
    subDispatcherHolderMap.entrySet().stream()
            .forEach(entry -> builder.beginControlFlow("if ($N.pack(target, args))", entry.getKey())
                    .addStatement("return true")
                    .endControlFlow());
    return builder.addStatement("return false").build();
  }
}