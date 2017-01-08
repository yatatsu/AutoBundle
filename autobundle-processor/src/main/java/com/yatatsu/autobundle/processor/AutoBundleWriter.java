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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;


class AutoBundleWriter {

    private final AutoBundleBindingClass bindingClass;

    private static final String FIELD_BUNDLE_NAME = "args";
    private static final ClassName CLASS_BUNDLE = ClassName.get("android.os", "Bundle");
    private static final ClassName CLASS_CONTEXT = ClassName.get("android.content", "Context");
    private static final ClassName CLASS_INTENT = ClassName.get("android.content", "Intent");

    private static final AnnotationSpec ANNOTATION_NONNULL
            = AnnotationSpec.builder(ClassName.get("android.support.annotation", "NonNull")).build();
    private static final AnnotationSpec ANNOTATION_NULLABLE
            = AnnotationSpec.builder(ClassName.get("android.support.annotation", "Nullable")).build();

    private static final String CLASS_NAME_BUILDER = "Builder";

    AutoBundleWriter(AutoBundleBindingClass target) {
        this.bindingClass = target;
    }

    void write(Filer filer) throws IOException {
        TypeSpec clazz = TypeSpec.classBuilder(bindingClass.getHelperClassName())
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addType(createBuilderClass(bindingClass))
                .addMethod(createCallBuilderMethod(bindingClass))
                .addMethod(createBindMethod(bindingClass))
                .addMethod(createBindWithSourceMethod(bindingClass))
                .addMethod(createPackMethod(bindingClass))
                .build();
        JavaFile.builder(bindingClass.getPackageName(), clazz)
                .build()
                .writeTo(filer);
    }

    private static ClassName getBuilderClassName(AutoBundleBindingClass target) {
        return ClassName.get(target.getHelperClassName(), CLASS_NAME_BUILDER);
    }

    private static MethodSpec createCallBuilderMethod(AutoBundleBindingClass target) {
        ClassName builderClass = getBuilderClassName(target);
        MethodSpec.Builder builder =
                MethodSpec.methodBuilder("builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(builderClass.annotated(ANNOTATION_NONNULL))
                .addCode("return new $T(", builderClass);
        for (int i = 0, count = target.getRequiredArgs().size(); i < count; i++) {
            AutoBundleBindingField arg = target.getRequiredArgs().get(i);
            ParameterSpec.Builder paramBuilder = ParameterSpec.builder(arg.getArgType(),
                    arg.getArgKey());
            // annotations
            if (arg.hasAnnotations()) {
                arg.getAnnotations().forEach(paramBuilder::addAnnotation);
            }
            // nonnull
            if (!arg.getArgType().isPrimitive()) {
                paramBuilder.addAnnotation(ANNOTATION_NONNULL);
            }
            // statement
            if (i > 0) {
                builder.addCode(", ");
            }
            builder.addParameter(paramBuilder.build())
                    .addCode("$N", arg.getArgKey());
        }
        return builder.addCode(");\n").build();
    }

    private static TypeSpec createBuilderClass(AutoBundleBindingClass target) {
        return TypeSpec.classBuilder(CLASS_NAME_BUILDER)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .addField(createField(FIELD_BUNDLE_NAME))
                .addMethod(createBuilderConstructor(target, FIELD_BUNDLE_NAME))
                .addMethods(createBuilderMethods(target, FIELD_BUNDLE_NAME))
                .addMethods(createBuildMethods(target, FIELD_BUNDLE_NAME))
                .build();
    }

    private static MethodSpec createBuilderConstructor(AutoBundleBindingClass target,
                                                       String fieldName) {
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("this.$N = new $T()", fieldName, CLASS_BUNDLE);

        for (AutoBundleBindingField arg : target.getRequiredArgs()) {
            String key = arg.getArgKey();
            TypeName type = arg.getArgType();
            // parameter for constructor
            // support annotation
            ParameterSpec.Builder paramBuilder = ParameterSpec.builder(type, key);
            if (arg.hasAnnotations()) {
                arg.getAnnotations().forEach(paramBuilder::addAnnotation);
            }
            // nonnull
            if (!type.isPrimitive()) {
                paramBuilder.addAnnotation(ANNOTATION_NONNULL);
            }
            builder.addParameter(paramBuilder.build());

            // statement
            String operationName = arg.getOperationName("put");
            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                        .addStatement("this.$N.$N($S, $NConverter.convert($N) )",
                                fieldName, operationName, key, key, key);
            } else {
                builder.addStatement("this.$N.$N($S, $N)", fieldName, operationName, key, key);
            }
        }

        return builder.build();
    }

    private static FieldSpec createField(String fieldName) {
        return FieldSpec.builder(CLASS_BUNDLE, fieldName, Modifier.FINAL).build();
    }

    private static List<MethodSpec> createBuilderMethods(AutoBundleBindingClass target,
                                                         String fieldName) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (AutoBundleBindingField arg : target.getNotRequiredArgs()) {
            String argKey = arg.getArgKey();
            TypeName argType = arg.getArgType();
            String operationName = arg.getOperationName("put");

            ParameterSpec.Builder paramBuilder = ParameterSpec.builder(argType, argKey);
            if (arg.hasAnnotations()) {
                arg.getAnnotations().forEach(paramBuilder::addAnnotation);
            }
            final boolean nullable = !argType.isPrimitive();
            if (nullable) {
                paramBuilder.addAnnotation(ANNOTATION_NULLABLE);
            }
            MethodSpec.Builder builder = MethodSpec.methodBuilder(argKey)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(paramBuilder.build())
                    .returns(getBuilderClassName(target).annotated(ANNOTATION_NONNULL));

            if (nullable) {
                builder.beginControlFlow("if ($N != null)", argKey);
            }
            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                builder.addStatement("$T $NConverter = new $T()", converter, argKey, converter)
                        .addStatement("$N.$N($S, $NConverter.convert($N) )",
                                fieldName, operationName, argKey, argKey, argKey);
            } else {
                builder.addStatement("$N.$N($S, $N)", fieldName, operationName, argKey, argKey);
            }
            if (nullable) {
                builder.endControlFlow();
            }

            builder.addStatement("return this");
            methodSpecs.add(builder.build());
        }
        return methodSpecs;
    }

    private static List<MethodSpec> createBuildMethods(AutoBundleBindingClass target,
                                                       String fieldName) {
        if (target.getBuilderType() == AutoBundleBindingClass.BuilderType.Fragment) {
            return createFragmentBuildMethods(target, fieldName);
        } else {
            return createIntentBuildMethods(target, fieldName);
        }
    }

    private static List<MethodSpec> createFragmentBuildMethods(AutoBundleBindingClass target,
                                                               String fieldName) {
        List<MethodSpec> methodSpecs = new ArrayList<>(2);
        ClassName targetClass = target.getTargetType();
        MethodSpec buildWithNoParam = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(targetClass.annotated(ANNOTATION_NONNULL))
                .addStatement("$T fragment = new $T()", targetClass, targetClass)
                .addStatement("fragment.setArguments($N)", fieldName)
                .addStatement("return fragment")
                .build();
        MethodSpec buildWithFragment = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(targetClass, "fragment")
                        .addAnnotation(ANNOTATION_NONNULL).build())
                .returns(targetClass.annotated(ANNOTATION_NONNULL))
                .addStatement("fragment.setArguments($N)", fieldName)
                .addStatement("return fragment")
                .build();
        methodSpecs.add(buildWithNoParam);
        methodSpecs.add(buildWithFragment);
        return methodSpecs;
    }

    private static List<MethodSpec> createIntentBuildMethods(AutoBundleBindingClass target,
                                                             String fieldName) {
        List<MethodSpec> methodSpecs = new ArrayList<>(2);
        MethodSpec buildWithContext = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(CLASS_CONTEXT, "context")
                        .addAnnotation(ANNOTATION_NONNULL).build())
                .returns(CLASS_INTENT.annotated(ANNOTATION_NONNULL))
                .addStatement("$T intent = new $T(context, $T.class)",
                        CLASS_INTENT, CLASS_INTENT, target.getTargetType())
                .addStatement("intent.putExtras($N)", fieldName)
                .addStatement("return intent")
                .build();
        MethodSpec buildWithIntent = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterSpec.builder(CLASS_INTENT, "intent")
                        .addAnnotation(ANNOTATION_NONNULL).build())
                .returns(CLASS_INTENT.annotated(ANNOTATION_NONNULL))
                .addStatement("intent.putExtras($N)", fieldName)
                .addStatement("return intent")
                .build();
        methodSpecs.add(buildWithContext);
        methodSpecs.add(buildWithIntent);
        return methodSpecs;
    }

    private static MethodSpec createBindWithSourceMethod(AutoBundleBindingClass target) {
        List<AutoBundleBindingField> args = new ArrayList<>();
        args.addAll(target.getRequiredArgs());
        args.addAll(target.getNotRequiredArgs());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(
                        ParameterSpec.builder(target.getTargetType(), "target")
                                .addAnnotation(ANNOTATION_NONNULL).build())
                .addParameter(
                        ParameterSpec.builder(CLASS_BUNDLE, "source")
                                .addAnnotation(ANNOTATION_NONNULL).build());

        for (AutoBundleBindingField arg : args) {
            String key = arg.getArgKey();
            String fieldName = arg.getFieldName();
            TypeName argType = arg.getArgType();
            String setterName = arg.getSetterName();
            String opName = arg.getOperationName("get");
            builder.beginControlFlow("if (source.containsKey($S))", key);

            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                builder.addStatement("$T $NConverter = new $T()", converter, key, converter);
                if (arg.hasSetter()) {
                    builder.addStatement("target.$N( ($T) $NConverter.original( source.$N($S) ) )",
                            setterName, argType, key, opName, key);
                } else {
                    builder.addStatement("target.$N = ($T) $NConverter.original( source.$N($S) )",
                            fieldName, argType, key, opName, key);
                }

            } else {
                if (arg.noCast()) {
                    if (arg.hasSetter()) {
                        builder.addStatement("target.$N( source.$N($S) )", setterName, opName, key);
                    } else {
                        builder.addStatement("target.$N = source.$N($S)", fieldName, opName, key);
                    }
                } else {
                    if (arg.hasSetter()) {
                        builder.addStatement("target.$N( ($T) source.$N($S) )",
                                setterName, argType, opName, key);
                    } else {
                        builder.addStatement("target.$N = ($T) source.$N($S)",
                                fieldName, argType, opName, key);
                    }
                }
            }

            if (arg.isRequired()) {
                String exceptionMessage
                        = String.format("%s is required, but not found in the bundle.", key);
                builder.nextControlFlow("else")
                        .addStatement("throw new IllegalStateException($S)", exceptionMessage);
            }
            builder.endControlFlow();
        }
        return builder.build();
    }

    private static MethodSpec createBindMethod(AutoBundleBindingClass target) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(
                        ParameterSpec.builder(target.getTargetType(), "target")
                                .addAnnotation(ANNOTATION_NONNULL).build());

        switch (target.getBuilderType()) {
            case Fragment:
                builder.beginControlFlow("if (target.getArguments() != null)")
                        .addStatement("bind(target, target.getArguments())")
                        .endControlFlow();
                break;
            case Intent:
                builder.addParameter(
                        ParameterSpec.builder(CLASS_INTENT, "intent")
                                .addAnnotation(ANNOTATION_NONNULL).build());
                builder.beginControlFlow("if (intent.getExtras() != null)")
                        .addStatement("bind(target, intent.getExtras())")
                        .endControlFlow();
                break;
        }
        return builder.build();
    }

    private static MethodSpec createPackMethod(AutoBundleBindingClass target) {
        List<AutoBundleBindingField> args = new ArrayList<>();
        args.addAll(target.getRequiredArgs());
        args.addAll(target.getNotRequiredArgs());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("pack")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(
                        ParameterSpec.builder(target.getTargetType(), "source")
                                .addAnnotation(ANNOTATION_NONNULL).build())
                .addParameter(
                        ParameterSpec.builder(CLASS_BUNDLE, "args")
                                .addAnnotation(ANNOTATION_NONNULL).build());

        for (AutoBundleBindingField arg : args) {
            String fieldName = arg.getFieldName();
            if (!arg.getArgType().isPrimitive()) {

                String getter = arg.hasGetter() ? arg.getGetterName() + "()" : fieldName;
                builder.beginControlFlow("if (source.$N != null)", getter);
                addPackOperationStatement(builder, arg);
                if (arg.isRequired()) {
                    String exceptionMessage = String.format("%s must not be null.", fieldName);
                    builder.nextControlFlow("else")
                            .addStatement("throw new IllegalStateException($S)", exceptionMessage)
                            .endControlFlow();
                } else {
                    builder.endControlFlow();
                }
            } else {
                addPackOperationStatement(builder, arg);
            }
        }
        return builder.build();
    }

    private static void addPackOperationStatement(MethodSpec.Builder builder,
                                                  AutoBundleBindingField arg) {
        String key = arg.getArgKey();
        String fieldName = arg.getFieldName();
        String operationName = arg.getOperationName("put");
        String getter = arg.hasGetter() ? arg.getGetterName() + "()" : fieldName;
        if (arg.hasCustomConverter()) {
            TypeName converter = arg.getConverter();
            builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                    .addStatement("args.$N($S, $NConverter.convert(source.$N) )",
                            operationName, key, key, getter);
        } else {
            builder.addStatement("args.$N($S, source.$N)", operationName, key, getter);
        }
    }
}
