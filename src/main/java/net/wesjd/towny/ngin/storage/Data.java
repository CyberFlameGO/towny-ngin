package net.wesjd.towny.ngin.storage;

import java.lang.annotation.*;

/**
 * Fields annotated with this are to be serialized
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Data {
}
