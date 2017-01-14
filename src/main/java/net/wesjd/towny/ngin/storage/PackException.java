package net.wesjd.towny.ngin.storage;

public class PackException extends Exception {

    public PackException(String reason, Throwable cause) {
        super(reason, cause);
    }

    public PackException(String reason) {
        super(reason);
    }
}
