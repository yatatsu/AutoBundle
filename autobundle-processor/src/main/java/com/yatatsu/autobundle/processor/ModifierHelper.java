package com.yatatsu.autobundle.processor;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;


class ModifierHelper {
    static final int PRIVATE   = -1;
    static final int DEFAULT   = 0;
    static final int PROTECTED = 1;
    static final int PUBLIC    = 2;

    static int getModifierLevel(Element element) {
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PUBLIC)) {
            return PUBLIC;
        }
        if (modifiers.contains(Modifier.PROTECTED)) {
            return PROTECTED;
        }
        if (modifiers.contains(Modifier.PRIVATE)) {
            return PRIVATE;
        }
        return DEFAULT;
    }
}
