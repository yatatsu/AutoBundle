package com.yatatsu.autobundle.processor;

import com.yatatsu.autobundle.AutoBundleField;
import com.yatatsu.autobundle.processor.exceptions.ProcessingException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

public class AutoBundleProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(AutoBundleField.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> options = new HashSet<>();
        options.add("autoBundleAsLibrary");
        options.add("autoBundleSubDispatchers");//TODO
        return options;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            String packageAsLibrary = processingEnv.getOptions().get("autoBundleAsLibrary");

            String dispatcher = processingEnv.getOptions().get("autoBundleSubDispatchers");

            messager.printMessage(Diagnostic.Kind.NOTE, "dispatcher ===> " + dispatcher);

            List<AutoBundleBindingClass> classes =
                    new ArrayList<>(BindingDetector.bindingClasses(roundEnv, elementUtils, typeUtils));
            for (AutoBundleBindingClass clazz : classes) {
                AutoBundleWriter writer = new AutoBundleWriter(clazz);
                writer.write(filer);
            }
            if (!classes.isEmpty()) {
                AutoBundleBinderWriter binderWriter =
                        new AutoBundleBinderWriter(classes, packageAsLibrary);
                binderWriter.write(filer);
            }
        } catch (ProcessingException | IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }
        return false;
    }
}
