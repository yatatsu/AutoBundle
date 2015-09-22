package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
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

    private static MethodSpec createCallBuilderMethod(AutoBundleBindingClass target) {
        ClassName builderClass =
                ClassName.get(target.getPackageName(), target.getBuilderClassName());
        MethodSpec.Builder builder =
                MethodSpec.methodBuilder("create" + target.getBuilderType().name() + "Builder")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(builderClass)
                .addCode("return new $T(", builderClass);
        for (int i = 0, count = target.getRequiredArgs().size(); i < count; i++) {
            if (i > 0) {
                builder.addCode(",");
            }
            AutoBundleBindingArg arg = target.getRequiredArgs().get(i);
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

        for (AutoBundleBindingArg arg : target.getRequiredArgs()) {
            String key = arg.getArgKey();
            TypeName type = arg.getArgType();
            builder.addParameter(type, key);
            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                TypeName converted = arg.getConvertedType();
                String operationName = createOperationMethodName("put", converted);
                builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                        .addStatement("this.$N.$N($S, $NConverter.convert($N))",
                                fieldName, operationName, key, key, key);
            } else {
                String operationName = createOperationMethodName("put", type);
                builder.addStatement("this.$N.$N($S, $N)", fieldName, operationName, key, key);
            }
        }

        return builder.build();
    }

    private static String createOperationMethodName(String operation, TypeName t) {
        StringBuilder builder = new StringBuilder(operation);
        ClassName sparseArrayClass = ClassName.get("android.util", "SparseArray");
        ClassName parcelableClass = ClassName.get("android.os", "Parcelable");
        ClassName arrayListClass = ClassName.get("java.util", "ArrayList");
        if (t.equals(ParameterizedTypeName.get(ArrayList.class, String.class))) {
            builder.append("StringArrayList");
        } else if (t.equals(ParameterizedTypeName.get(ArrayList.class, Integer.class))) {
            builder.append("IntegerArrayList");
        } else if (t.equals(ParameterizedTypeName.get(ArrayList.class, CharSequence.class))){
            builder.append("CharSequenceArrayList");
        } else if (t.equals(ParameterizedTypeName.get(arrayListClass, parcelableClass))) {
            builder.append("ParcelableArrayList");
        } else if (t.equals(ParameterizedTypeName.get(sparseArrayClass, parcelableClass))) {
            builder.append("SparseParcelableArray");
        } else if (t.equals(ClassName.get("android.os", "IBinder"))) {
            builder.append("Binder");
        } else {
            String clazzName = t.toString().substring(t.toString().lastIndexOf(".") + 1);
            clazzName = Character.toUpperCase(clazzName.charAt(0)) + clazzName.substring(1);
            if (clazzName.endsWith("[]")) {
                clazzName = clazzName.substring(0, clazzName.length() - 2);
                builder.append(clazzName);
                builder.append("Array");
            } else {
                builder.append(clazzName);
            }
        }
        return builder.toString();
    }

    private static FieldSpec createField(String fieldName) {
        return FieldSpec.builder(CLASS_BUNDLE, fieldName, Modifier.FINAL).build();
    }

    private static List<MethodSpec> createBuilderMethods(AutoBundleBindingClass target,
                                                         String fieldName) {
        List<MethodSpec> methodSpecs = new ArrayList<>();
        for (AutoBundleBindingArg arg : target.getNotRequiredArgs()) {
            String argKey = arg.getArgKey();
            TypeName argType = arg.getArgType();

            MethodSpec.Builder builder = MethodSpec.methodBuilder(argKey)
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(argType, argKey)
                    .returns(ClassName.get(target.getPackageName(), target.getBuilderClassName()));

            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                TypeName converted = arg.getArgType();
                String operationName = createOperationMethodName("put", converted);
                builder.addStatement("$T $NConverter = new $T()", converter, argKey, converter)
                        .addStatement("$N.$N($S, $NConverter.convert($N))",
                                fieldName, operationName, argKey, argKey, argKey);
            } else {
                String operationName = createOperationMethodName("put", argType);
                builder.addStatement("$N.$N($S, $N)", fieldName, operationName, argKey, argKey);
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
        ClassName targetClass = ClassName.get(target.getTypeElement());
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
        ClassName targetClass = ClassName.get(target.getTypeElement());
        ClassName contextClass = ClassName.get("android.content", "Context");
        ClassName intentClass = ClassName.get("android.content", "Intent");
        MethodSpec buildWithContext = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(contextClass, "context")
                .returns(intentClass)
                .addStatement("$T intent = new $T(context, $T.class)",
                        intentClass, intentClass, targetClass)
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
        List<AutoBundleBindingArg> args = new ArrayList<>();
        args.addAll(target.getRequiredArgs());
        args.addAll(target.getNotRequiredArgs());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("bind")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(ClassName.get(target.getTypeElement()), "target")
                .addParameter(CLASS_BUNDLE, "source");

        for (AutoBundleBindingArg arg : args) {
            String key = arg.getArgKey();
            String fieldName = arg.getFieldName();
            builder.beginControlFlow("if (source.containsKey($S))", key);

            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                TypeName converted = arg.getConvertedType();
                String operationName = createOperationMethodName("get", converted);
                builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                        .addStatement("target.$N = $NConverter.original(source.$N($S))",
                                fieldName, key, operationName, key);
            } else {
                String operationName = createOperationMethodName("get", arg.getArgType());
                builder.addStatement("target.$N = source.$N($S)", fieldName, operationName, key);
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
                .addParameter(ClassName.get(target.getTypeElement()), "target");

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
        List<AutoBundleBindingArg> args = new ArrayList<>();
        args.addAll(target.getRequiredArgs());
        args.addAll(target.getNotRequiredArgs());

        MethodSpec.Builder builder = MethodSpec.methodBuilder("pack")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(ClassName.get(target.getTypeElement()), "source")
                .addParameter(CLASS_BUNDLE, "args");

        for (AutoBundleBindingArg arg : args) {
            String key = arg.getArgKey();
            String fieldName = arg.getFieldName();
            TypeName argType = arg.getArgType();

            if (!argType.isPrimitive()) {
                String exceptionMessage = String.format("%s must not be null.", fieldName);
                builder.beginControlFlow("if (source.$N == null)", fieldName)
                        .addStatement("throw new IllegalStateException($S)", exceptionMessage)
                        .endControlFlow();
            }
            if (arg.hasCustomConverter()) {
                TypeName converter = arg.getConverter();
                TypeName converted = arg.getConvertedType();
                String operationName = createOperationMethodName("put", converted);
                builder.addStatement("$T $NConverter = new $T()", converter, key, converter)
                        .addStatement("args.$N($S, $NConverter.convert(source.$N))",
                                operationName, key, key, fieldName);
            } else {
                String operationName = createOperationMethodName("put", argType);
                builder.addStatement("args.$N($S, source.$N)", operationName, key, fieldName);
            }
        }
        return builder.build();
    }
}
