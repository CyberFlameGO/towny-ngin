package net.wesjd.towny.ngin.command.framework.argument.verifier;

import java.lang.reflect.Parameter;

/**
 * Verifies an argument to pass certain checks
 * @param <T> The type of argument
 */
public interface ArgumentVerifier<T> {

    /**
     * Verifies an object
     *
     * @param parameter The method parameter, used to get annotations, etc
     * @param object The object that has been supplied for that type
     * @return A string containing the error message, or null if none
     */
    String verify(Parameter parameter, T object);

}
