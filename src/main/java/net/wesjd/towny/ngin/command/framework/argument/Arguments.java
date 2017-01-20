package net.wesjd.towny.ngin.command.framework.argument;

import java.util.Optional;

/**
 * Contains the arguments for a command
 */
public class Arguments {

    /**
     * The command's raw arguments
     */
    private final String[] _arguments;
    /**
     * The current position of the iteration
     */
    private int _position = 0;

    /**
     * Creates a new instance
     *
     * @param arguments The command's arguments
     */
    public Arguments(String[] arguments) {
        _arguments = arguments;
    }

    /**
     * Get the next argument in the array
     *
     * @return The next argument
     */
    public String next() {
        return _arguments[_position++];
    }

    public Optional<String> safeNext() {
        return _position == _arguments.length ? Optional.empty() : Optional.of(next());
    }

    /**
     * Get the position of the iteration
     *
     * @return The iteration's position
     */
    public int position() {
        return _position;
    }

    /**
     * Tell weather the iteration has any more
     *
     * @return The value
     */
    public boolean hasNext() {
        return _position < _arguments.length;
    }

}
