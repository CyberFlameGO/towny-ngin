package net.wesjd.towny.ngin.command.framework.argument.verifier;

import net.wesjd.towny.ngin.command.framework.annotation.parameter.Regex;

import java.lang.reflect.Parameter;

public class RegexVerifier implements ArgumentVerifier<String> {

    @Override
    public String verify(Parameter parameter, String object) {
        if(parameter.isAnnotationPresent(Regex.class)) {
            final Regex annotation = parameter.getAnnotation(Regex.class);
            if(object == null || !object.matches(annotation.exp())) return annotation.fail();
        }
        return null;
    }

}
