package net.wesjd.towny.ngin.command.framework.argument.provider;

import net.wesjd.towny.ngin.command.framework.argument.Arguments;

import java.lang.annotation.Annotation;
import java.util.Optional;

public interface ArgumentProvider<T> {

    T get(Optional<Annotation> annotation, Arguments arguments);

}
