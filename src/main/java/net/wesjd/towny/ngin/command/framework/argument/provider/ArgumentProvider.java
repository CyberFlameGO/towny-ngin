package net.wesjd.towny.ngin.command.framework.argument.provider;

import net.wesjd.towny.ngin.command.framework.argument.Arguments;

public interface ArgumentProvider<T> {

    T get(Arguments arguments);

}
