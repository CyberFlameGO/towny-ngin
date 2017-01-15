package net.wesjd.towny.ngin.storage;

public class PackException extends RuntimeException {

    public PackException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public PackException(String reason) {
        super(reason);
    }
}
