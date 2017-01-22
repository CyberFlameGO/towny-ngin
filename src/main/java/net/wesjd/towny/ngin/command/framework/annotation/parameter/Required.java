package net.wesjd.towny.ngin.command.framework.annotation.parameter;

import java.lang.annotation.*;

/**
 * Requires a parameter
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Required {

    /**
     * Display this message when the parameter isn't supplied
     */
    String fail();

}
