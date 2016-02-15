package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;


public class AutoBundleWriter {

    private final AutoBundleBindingClass bindingClass;

    private static final String FIELD_BUNDLE_NAME = "args";
    private static final ClassName CLASS_BUNDLE = ClassName.get("android.os", "Bundle");

    public AutoBundleWriter(AutoBundleBindingClass target) {
        this.bindingClass = target;
    }

    public void write(Filer filer) throws IOException {
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
        return ClassName.get(target.getHelperClassName(), target.getBuilderClassName());
    }

    private static MethodSpec createCallBuilderMethod(AutoBundleBindingClass target) {
        ClassName builderClass = getBuilderClassName(target);
        MethodSpec.Builder builder =
                MethodSpec.methodBuilder("create" + target.getBuilderType().name() + "Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(builderClass)
                .addCode("return new $T(", builderClass);
        for (int i = 0, count = target.getRequiredArgs().size(); i < count; i++) {
            if (i > 0) {
                builder.addCode(",");
            }
            AutoBundleBindingField arg = target.getRequiredArgs().get(i);
            builder.addParameter(arg.getArgType(), arg.getArgKey())
                    .addCode("$N", arg.getArgKey());
        }
        return builder.addCode(");\n").build();
    }

    private static TypeSpec createBuilderClass(AutoBundleBindingClass target) {
        return TypeSpec.classBuilder(target.getBuilderClassName())
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
            String operationName = arg.getOperationName("put");
            builder.addParameter(type, key);
            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                        .addStatement("this.$N.$N($S, $NConverter.convert($N))",
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

            MethodSpec.Builder builder = MethodSpec.methodBuilder(argKey)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(argType, argKey)
                    .returns(getBuilderClassName(target));

            final boolean checkNull = !arg.getArgType().isPrimitive();
            if (checkNull) {
                builder.beginControlFlow("if ($N != null)", argKey);
            }
            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                builder.addStatement("$T $NConverter = new $T()", converter, argKey, converter)
                        .addStatement("$N.$N($S, $NConverter.convert($N))",
                                fieldName, operationName, argKey, argKey, argKey);
            } else {
                builder.addStatement("$N.$N($S, $N)", fieldName, operationName, argKey, argKey);
            }
            if (checkNull) {
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
                .returns(targetClass)
                .addStatement("$T fragment = new $T()", targetClass, targetClass)
                .addStatement("fragment.setArguments($N)", fieldName)
                .addStatement("return fragment")
                .build();
        MethodSpec buildWithFragment = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(targetClass, "fragment")
                .returns(targetClass)
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
        ClassName contextClass = ClassName.get("android.content", "Context");
        ClassName intentClass = ClassName.get("android.content", "Intent");
        MethodSpec buildWithContext = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClass, "context")
                .returns(intentClass)
                .addStatement("$T intent = new $T(context, $T.class)",
                        intentClass, intentClass, target.getTargetType())
                .addStatement("intent.putExtras($N)", fieldName)
                .addStatement("return intent")
                .build();
        MethodSpec buildWithIntent = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(intentClass, "intent")
                .returns(intentClass)
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
                .addParameter(target.getTargetType(), "target")
                .addParameter(CLASS_BUNDLE, "source");

        for (AutoBundleBindingField arg : args) {
            String key = arg.getArgKey();
            String fieldName = arg.getFieldName();
            TypeName argType = arg.getArgType();
            String operationName = arg.getOperationName("get");
            builder.beginControlFlow("if (source.containsKey($S))", key);

            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                        .addStatement("target.$N = ($T) $NConverter.original(source.$N($S))",
                                fieldName, argType, key, operationName, key);
            } else {
                if (arg.noCast()) {
                    builder.addStatement("target.$N = source.$N($S)",
                            fieldName, operationName, key);
                } else {
                    builder.addStatement("target.$N = ($T) source.$N($S)",
                            fieldName, argType, operationName, key);
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
                .addParameter(target.getTargetType(), "target");

        switch (target.getBuilderType()) {
            case Fragment:
                builder.addStatement("bind(target, target.getArguments())");
                break;
            case Intent:
                builder.addParameter(ClassName.get("android.content", "Intent"), "intent");
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
                .addParameter(target.getTargetType(), "source")
                .addParameter(CLASS_BUNDLE, "args");

        for (AutoBundleBindingField arg : args) {
            String fieldName = arg.getFieldName();
            if (!arg.getArgType().isPrimitive()) {
                builder.beginControlFlow("if (source.$N != null)", fieldName);
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
        if (arg.hasCustomConverter()) {
            TypeName converter = arg.getConverter();
            builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                    .addStatement("args.$N($S, $NConverter.convert(source.$N))",
                            operationName, key, key, fieldName);
        } else {
            builder.addStatement("args.$N($S, source.$N)", operationName, key, fieldName);
        }
    }
}
