package net.wesjd.towny.ngin.command.framework;

import net.wesjd.towny.ngin.command.framework.annotation.Command;
import net.wesjd.towny.ngin.command.framework.annotation.SubCommand;
import net.wesjd.towny.ngin.command.framework.argument.ArgumentBinding;
import net.wesjd.towny.ngin.command.framework.argument.Arguments;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores and handles all commands
 */
public class CommandManager {

    /**
     * A store of command names to their command data to save on lookup time
     */
    private final Map<String, CommandData> _commands = new HashMap<>();
    /**
     * A store of argument types to their bindings
     */
    private final Map<Class<?>, ArgumentBinding<?>> _bindings = new HashMap<>();

    /**
     * Loads all of the commandables
     */
    public CommandManager() {
        new Reflections("net.wesjd.towny.ngin.command").getSubTypesOf(Commandable.class).forEach(this::buildCommands);
    }

    /**
     * Start an argument binding
     *
     * @param type The argument type
     * @return A binding builder
     */
    public ArgumentBinding.Builder bind(Class<?> type) {
        return new ArgumentBinding.Builder((binding) -> _bindings.put(type, binding));
    }

    /**
     * Calls a command
     *
     * @param calledCommand The command that should be called
     * @param providedArguments The command arguments
     */
    public void callCommand(String calledCommand, String[] providedArguments) {
        final CommandData command = _commands.get(calledCommand);
        final Arguments arguments = new Arguments(providedArguments);
        final SubCommandData topSubcommand = getTopSubcommand(calledCommand, arguments, null, command.getSubcommands());
        if(topSubcommand != null) invokeCommandMethod(command.getClassInstance(), command.getMethod(), arguments);
        else invokeCommandMethod(command.getClassInstance(), command.getMethod(), arguments);
    }

    /**
     * Finds the top subcommand for the command
     *
     * @param base The command to find the top subcommand of
     * @param arguments The command arguments
     * @param lastTop The last top subcommad, used in recursion
     * @param subcommands All of the possible subcommands
     * @return The top subcommand
     */
    private SubCommandData getTopSubcommand(String base, Arguments arguments, SubCommandData lastTop, Set<SubCommandData> subcommands) {
        final String subcommandOf = base + arguments.next();
        return subcommands.stream()
                .filter(subcommand -> subcommand.getAnnotation().of().equals(subcommandOf))
                .map(subcommand -> getTopSubcommand(subcommandOf + subcommand.getAnnotation().name(), arguments, subcommand, subcommand.getSubcommands()))
                .findFirst().orElse(lastTop);
    }

    /**
     * Invoke a command method
     *
     * @param object The object ({@link Commandable}) to invoke upon
     * @param method The method to invoke
     * @param arguments The command arguments
     */
    private void invokeCommandMethod(Object object, Method method, Arguments arguments) {
        try {
            method.invoke(object,
                    Arrays.stream(method.getParameters())
                            .map(parameter -> {
                                final Class<?> type = parameter.getType();
                                final ArgumentBinding binding = _bindings.get(type);
                                if(binding != null) {
                                    final Optional<Class<? extends Annotation>> annotation = binding.getAnnotation();
                                    if(!annotation.isPresent() || (annotation.isPresent() && parameter.isAnnotationPresent(annotation.get())))
                                        binding.getArgumentProvider().get(arguments);
                                }
                                if(type.isAssignableFrom(String.class)) return arguments.next();
                                else throw new RuntimeException("Cannot convert " + parameter.getType().getSimpleName() + " to required type.");
                            }));
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Builds all of the commands for a class
     *
     * @param clazz The class to build the commands for
     */
    private void buildCommands(Class<? extends Commandable> clazz) {
        try {
            final Object commandObject = clazz.newInstance();
            final Set<Method> commands = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(Command.class))
                    .peek(method -> method.setAccessible(true))
                    .collect(Collectors.toSet());
            final Set<Method> subcommands = Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(SubCommand.class))
                    .peek(method -> method.setAccessible(true))
                    .collect(Collectors.toSet());
            commands.forEach(method -> {
                final Command command = method.getAnnotation(Command.class);
                _commands.put(command.name(), new CommandData(commandObject, command, method,
                        getSubCommands(command.name(), subcommands));
            });
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recursively search through to find subcommands
     *
     * @param of What command the subcommands should be under
     * @param subcommands All of the possible subcommands
     * @return All of the generated subcommands
     */
    private Set<SubCommandData> getSubCommands(String of, Set<Method> subcommands) {
        return subcommands.stream()
                .map(method -> {
                    final SubCommand subcommand = method.getAnnotation(SubCommand.class);
                    return new SubCommandData(subcommand, method, getSubCommands(of + " " + subcommand.name(), subcommands));
                })
                .collect(Collectors.toSet());
    }

    /**
     * Represents a base command, containing subcommands
     */
    private static class CommandData {

        /**
         * The actual command class
         */
        private final Object _object;
        /**
         * The command annoatation (and data) itself
         */
        private final Command _command;
        /**
         * The method to call the command upon
         */
        private final Method _method;
        /**
         * All of the subcommands for this command
         */
        private final Set<SubCommandData> _subcommands;

        /**
         * Stores some command data
         *
         * @param object The actual actual command class instance
         * @param command The command annoation
         * @param method The annotated method
         * @param subcommands All of the subcommands
         */
        public CommandData(Object object, Command command, Method method, Set<SubCommandData> subcommands) {
            _object = object;
            _command = command;
            _method = method;
            _subcommands = subcommands;
        }

        public Object getClassInstance() {
            return _object;
        }

        public Command getAnnotation() {
            return _command;
        }

        public Method getMethod() {
            return _method;
        }

        public Set<SubCommandData> getSubcommands() {
            return Collections.unmodifiableSet(_subcommands);
        }

    }

    /**
     * Represents a subcommand, containing all of it's subcommands
     */
    private static class SubCommandData {

        /**
         * The subcommand's annoation (and data)
         */
        private final SubCommand _subcommand;
        /**
         * The method to call the command upon
         */
        private final Method _method;
        /**
         * All of the subcommands for this subcommand
         */
        private final Set<SubCommandData> _subcommands;

        /**
         * Stores some subcommand data
         *
         * @param subcommand The subcommand annotation
         * @param method The annotated method
         * @param subcommands All of the subcommands
         */
        public SubCommandData(SubCommand subcommand, Method method, Set<SubCommandData> subcommands) {
            _subcommand = subcommand;
            _method = method;
            _subcommands = subcommands;
        }

        public SubCommand getAnnotation() {
            return _subcommand;
        }

        public Method getMethod() {
            return _method;
        }

        public Set<SubCommandData> getSubcommands() {
            return Collections.unmodifiableSet(_subcommands);
        }

    }

}
