package net.wesjd.towny.ngin.command.framework.argument.provider;

import net.wesjd.towny.ngin.command.framework.argument.Arguments;

import java.lang.reflect.Parameter;

public class EnumProvider<E extends Enum<E>> implements ArgumentProvider<Enum<E>> {

    @Override
    public Enum<E> get(Parameter parameter, Arguments arguments) {
        try {
            return Enum.valueOf((Class<? extends Enum>) parameter.getType(), arguments.next().toUpperCase());
        } catch (Exception ex) {
            return null;
        }
    }

}
