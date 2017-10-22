package net.wesjd.towny.ngin.command.framework.argument;

import java.util.Optional;

/**
 * Contains the arguments for a command
 */
public class Arguments {

    /**
     * The command's raw arguments
     */
    private final String[] arguments;
    /**
     * The current position of the iteration
     */
    private int position = 0;

    /**
     * Creates a new instance
     *
     * @param arguments The command's arguments
     */
    public Arguments(String[] arguments) {
        this.arguments = arguments;
    }

    /**
     * Get the next argument in the array
     *
     * @return The next argument
     */
    public String next() {
        return arguments[position++];
    }

    /**
     * Get the next argument in the array, safely
     *
     * @return The next argument as an optional
     */
    public Optional<String> safeNext() {
        return position == arguments.length ? Optional.empty() : Optional.of(next());
    }

    /**
     * Shifts back an index
     */
    public void unshift() {
        position--;
    }

    /**
     * Get the position of the iteration
     *
     * @return The iteration's position
     */
    public int position() {
        return position;
    }

    /**
     * Tell weather the iteration has any more
     *
     * @return The value
     */
    public boolean hasNext() {
        return position < arguments.length;
    }

}
