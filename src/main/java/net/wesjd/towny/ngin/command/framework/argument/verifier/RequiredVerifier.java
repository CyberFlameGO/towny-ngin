package net.wesjd.towny.ngin.command.framework.argument.verifier;

import net.wesjd.towny.ngin.command.framework.annotation.parameter.Required;

import java.lang.reflect.Parameter;

public class RequiredVerifier implements ArgumentVerifier<Object> {

    @Override
    public String verify(Parameter parameter, Object object) {
        if(parameter.isAnnotationPresent(Required.class) && object == null) return parameter.getAnnotation(Required.class).fail();
        return null;
    }

}
