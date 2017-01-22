package net.wesjd.towny.ngin.command.framework.annotation;

import java.lang.annotation.*;

/**
 * Represents a command
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * The name of the command
     */
    String name();

    /**
     * The description of the command
     */
    String desc();

}
