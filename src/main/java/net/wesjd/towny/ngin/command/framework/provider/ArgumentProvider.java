package net.wesjd.towny.ngin.command.framework.provider;

import net.wesjd.towny.ngin.command.framework.Arguments;

public interface ArgumentProvider<T> {

    T get(Arguments arguments);

}
