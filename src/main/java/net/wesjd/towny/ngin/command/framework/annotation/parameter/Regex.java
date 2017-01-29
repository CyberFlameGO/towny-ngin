package net.wesjd.towny.ngin.command.framework.annotation.parameter;

import java.lang.annotation.*;

/**
 * Checks a string parameter for weather it matches a regex expression
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Regex {

    /**
     * The regex expression
     */
    String exp();

    /**
     * The message to show on matching failure
     */
    String fail();

}
