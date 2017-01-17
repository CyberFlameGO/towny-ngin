package net.wesjd.towny.ngin.command.framework.annotation;

import java.lang.annotation.*;

/**
 * Represents a sub command
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {

    /**
     * What the command is a subcommand of
     */
    String of();

    /**
     * The name of the command
     */
    String name();

    /**
     * The description of the command
     */
    String desc();

    /**
     * The command usage
     */
    String usage() default "";

    /**
     * The minimum amount of arguments required, otherwise shows the usage
     */
    int min() default 0;

}
