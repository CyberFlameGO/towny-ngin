package net.wesjd.towny.ngin.command.framework;

public class Arguments {

    private final String[] _arguments;
    private int _position = 0;

    public Arguments(String[] arguments) {
        _arguments = arguments;
    }

    public String next() {
        return _arguments[_position++];
    }

    public int position() {
        return _position;
    }

}
