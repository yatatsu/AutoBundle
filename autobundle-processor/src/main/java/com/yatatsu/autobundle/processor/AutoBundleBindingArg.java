package com.yatatsu.autobundle.processor;

import com.squareup.javapoet.TypeName;
import com.yatatsu.autobundle.AutoBundle;

import javax.lang.model.element.VariableElement;


public class AutoBundleBindingArg {

    private final String fieldName;
    private final String argKey;
    private final boolean required;
    private final TypeName argType;

    public AutoBundleBindingArg(VariableElement element, AutoBundle annotation) {
        this.fieldName = element.toString();
        this.argKey = annotation.key().length() > 0 ? annotation.key() : this.fieldName;
        this.required = annotation.required();
        this.argType = TypeName.get(element.asType());
        // TODO validation
    }

    public TypeName getArgType() {
        return argType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getArgKey() {
        return argKey;
    }

    public boolean isRequired() {
        return required;
    }
}
