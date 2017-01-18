package net.wesjd.towny.ngin.command.framework.argument.provider;

import net.wesjd.towny.ngin.command.framework.argument.Arguments;

import java.lang.reflect.Parameter;

/**
 * Provides a value for a command argument
 */
public interface ArgumentProvider<T> {

    /**
     * Called to get the parameter value for the argument
     *
     * @param parameter The parameter of the method
     * @param arguments The command's arguments
     * @return The generated object
     */
    T get(Parameter parameter, Arguments arguments);

}
