package net.wesjd.towny.ngin.command.framework.annotation;

import net.wesjd.towny.ngin.player.Rank;

import java.lang.annotation.*;

/**
 * Makes a command require a rank
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requires {

    /**
     * The required rank
     */
    Rank value();

}
