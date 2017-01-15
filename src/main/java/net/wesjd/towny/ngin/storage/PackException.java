package net.wesjd.towny.ngin.storage;

/**
 * Thrown when an exception occurs in during packing and unboxing
 */
public class PackException extends RuntimeException {

    public PackException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public PackException(String reason) {
        super(reason);
    }

}
