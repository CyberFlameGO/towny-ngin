package net.wesjd.towny.ngin.command.framework;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import net.wesjd.towny.ngin.Towny;
import net.wesjd.towny.ngin.command.framework.annotation.Command;
import net.wesjd.towny.ngin.command.framework.annotation.Requires;
import net.wesjd.towny.ngin.command.framework.annotation.SubCommand;
import net.wesjd.towny.ngin.command.framework.argument.ArgumentBinding;
import net.wesjd.towny.ngin.command.framework.argument.Arguments;
import net.wesjd.towny.ngin.command.framework.argument.verifier.ArgumentVerifier;
import net.wesjd.towny.ngin.player.PlayerManager;
import net.wesjd.towny.ngin.player.Rank;
import net.wesjd.towny.ngin.player.TownyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.bukkit.ChatColor.*;

/**
 * Stores and handles all commands
 */
public class CommandManager {

    /**
     * A main class instance, provided in the constrcutor
     */
    private final Towny main;

    /**
     * A store of command names to their command data to save on lookup time
     */
    private final Map<String, CommandData> commands = new HashMap<>();
    /**
     * A store of argument types to their bindings
     */
    private final Map<Class<?>, ArgumentBinding<?>> bindings = new HashMap<>();
    /**
     * A store of argument types to verifiers
     */
    private final Multimap<Class<?>, ArgumentVerifier> verifiers = ArrayListMultimap.create();

    /**
     * Loads all of the commandables
     */
    @Inject
    public CommandManager(Towny main, PlayerManager playerManager) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onCommand(PlayerCommandPreprocessEvent e) {
                final String[] parsed = e.getMessage().split(" ");
                final String commandName = parsed[0].substring(1);
                final String[] args = new String[parsed.length-1];
                System.arraycopy(parsed, 1, args, 0, args.length);
                if(callCommand(playerManager.getPlayer(e.getPlayer()), commandName, args))
                    e.setCancelled(true);
            }
        }, main);
    }

    /**
     * Register all {@link Commandable} classes under the provided package
     *
     * @param pkg The package to register
     */
    public void registerClassesOf(String pkg) {
        new Reflections(pkg)
                .getSubTypesOf(Commandable.class)
                .forEach(this::buildCommands);
    }

    /**
     * Bind an object to it's verifier
     *
     * @param type The type the verifier is bound to
     * @param verifier The verifier itself
     */
    public <T> void addVerifier(Class<T> type, ArgumentVerifier<T> verifier) {
        verifiers.put(type, verifier);
    }

    /**
     * Start an argument binding
     *
     * @param type The argument type
     * @return A binding builder
     */
    public ArgumentBinding.Builder bind(Class<?> type) {
        return new ArgumentBinding.Builder((binding) -> bindings.put(type, binding));
    }

    /**
     * Calls a command
     *
     * @param calledCommand The command that should be called
     * @param providedArguments The command arguments
     * @return Weather the command successfully executed
     */
    public boolean callCommand(TownyPlayer caller, String calledCommand, String[] providedArguments) {
        final CommandData command = commands.get(calledCommand);
        if(command != null) {
            final Arguments arguments = new Arguments(providedArguments);
            final SubCommandData topSubcommand = getTopSubcommand(calledCommand, arguments, null, command.getSubcommands());
            if(topSubcommand != null) invokeCommandMethod(command.getClassInstance(), topSubcommand.getMethod(), caller, arguments);
            else invokeCommandMethod(command.getClassInstance(), command.getMethod(), caller, arguments);
            return true;
        } else return false;
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
        if(arguments.hasNext()) {
            final String next = arguments.next();
            for(SubCommandData subcommand : subcommands) {
                final SubCommand annotation = subcommand.getAnnotation();
                if(annotation.of().equals(base) && annotation.name().equals(next)) {
                    final SubCommandData nextResult = getTopSubcommand(base + " " + next, arguments, subcommand, subcommand.getSubcommands());
                    if(nextResult == null) return subcommand;
                    else return nextResult;
                }
            }
            arguments.unshift();
        }
        return lastTop;
    }

    /**
     * Invoke a command method
     *
     * @param object The object ({@link Commandable}) to invoke upon
     * @param method The method to invoke
     * @param arguments The command arguments
     */
    private void invokeCommandMethod(Object object, Method method, TownyPlayer caller, Arguments arguments) {
        try {
            if(method.isAnnotationPresent(Requires.class)) {
                final Rank required = method.getAnnotation(Requires.class).value();
                if(!caller.hasRank(required)) {
                    caller.getWrapped().sendMessage(RED + "You need the rank " + BLUE + required + RED + " to use this command!");
                    return;
                }
            }
            final Parameter[] parameters = method.getParameters();

            final Object[] suppliedParameters = new Object[parameters.length];
            if(parameters[0].getType().equals(TownyPlayer.class)) suppliedParameters[0] = caller;
            else throw new RuntimeException("Command must take sender as first parameter.");
            IntStream.range(1, parameters.length)
                    .forEach(i -> {
                        Object supply = null;
                        final Parameter parameter = parameters[i];
                        final Class<?> type = parameter.getType();

                        boolean supplied = false;
                        final ArgumentBinding<?> binding = bindings.get(type);
                        if(binding != null) {
                            final Optional<Class<? extends Annotation>> annotation = binding.getAnnotation();
                            if(!annotation.isPresent() || parameter.isAnnotationPresent(annotation.get())) {
                                if(arguments.hasNext()) supply = binding.getArgumentProvider().get(parameter, arguments);
                                supplied = true;
                            }
                        }

                        if(supply == null && !supplied) {
                            if(type.isAssignableFrom(String.class)) supply = arguments.safeNext().orElse(null);
                            else throw new RuntimeException("Cannot convert " + parameter.getType().getSimpleName() + " to required type.");
                        }
                        suppliedParameters[i] = supply;
                    });

            final AtomicInteger index = new AtomicInteger();
            final Optional<String> failure = Arrays.stream(parameters)
                    .flatMap(parameter -> {
                        final int currentIndex = index.getAndIncrement();
                        return getVerifiersFor(parameter.getType()).map(verifier -> verifier.verify(parameter, suppliedParameters[currentIndex]));
                    })
                    .filter(Objects::nonNull)
                    .map(Optional::of)
                    .findFirst().flatMap(Function.identity());
            if(failure.isPresent()) caller.getWrapped().sendMessage(RED + failure.get());
            else method.invoke(object, suppliedParameters);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the verifiers for a type, including object verifiers
     *
     * @param type The object type
     * @return A stream of the found verifiers
     */
    private Stream<ArgumentVerifier> getVerifiersFor(Class<?> type) {
        final Collection<ArgumentVerifier> verifiers = this.verifiers.get(type);
        verifiers.addAll(this.verifiers.get(Object.class));
        return verifiers.stream();
    }

    /**
     * Builds all of the commands for a class
     *
     * @param clazz The class to build the commands for
     */
    private void buildCommands(Class<? extends Commandable> clazz) {
        final Object commandObject = main.getInjector().getInstance(clazz);
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
            this.commands.put(command.name(), new CommandData(commandObject, command, method,
                    getSubCommands(command.name(), subcommands)));
        });
    }

    /**
     * Recursively search through to find subcommands
     *
     * @param of What command the subcommands should be under
     * @param subcommands All of the possible subcommands
     * @return All of the generated subcommands
     */
    private Set<SubCommandData> getSubCommands(String of, Set<Method> subcommands) {
        final Set<SubCommandData> ret = new HashSet<>();

        for(Method method : subcommands) {
            final SubCommand subcommand = method.getAnnotation(SubCommand.class);
            if (subcommand.of().equals(of))
                ret.add(new SubCommandData(subcommand, method, getSubCommands(of + " " + subcommand.name(), subcommands)));
        }

        return ret;
    }

    /**
     * Represents a base command, containing subcommands
     */
    private static class CommandData {

        /**
         * The actual command class
         */
        private final Object object;
        /**
         * The command annoatation (and data) itself
         */
        private final Command command;
        /**
         * The method to call the command upon
         */
        private final Method method;
        /**
         * All of the subcommands for this command
         */
        private final Set<SubCommandData> subcommands;

        /**
         * Stores some command data
         *
         * @param object The actual actual command class instance
         * @param command The command annoation
         * @param method The annotated method
         * @param subcommands All of the subcommands
         */
        public CommandData(Object object, Command command, Method method, Set<SubCommandData> subcommands) {
            this.object = object;
            this.command = command;
            this.method = method;
            this.subcommands = subcommands;
        }

        public Object getClassInstance() {
            return object;
        }

        public Command getAnnotation() {
            return command;
        }

        public Method getMethod() {
            return method;
        }

        public Set<SubCommandData> getSubcommands() {
            return Collections.unmodifiableSet(subcommands);
        }

    }

    /**
     * Represents a subcommand, containing all of it's subcommands
     */
    private static class SubCommandData {

        /**
         * The subcommand's annoation (and data)
         */
        private final SubCommand subcommand;
        /**
         * The method to call the command upon
         */
        private final Method method;
        /**
         * All of the subcommands for this subcommand
         */
        private final Set<SubCommandData> subcommands;

        /**
         * Stores some subcommand data
         *
         * @param subcommand The subcommand annotation
         * @param method The annotated method
         * @param subcommands All of the subcommands
         */
        public SubCommandData(SubCommand subcommand, Method method, Set<SubCommandData> subcommands) {
            this.subcommand = subcommand;
            this.method = method;
            this.subcommands = subcommands;
        }

        public SubCommand getAnnotation() {
            return subcommand;
        }

        public Method getMethod() {
            return method;
        }

        public Set<SubCommandData> getSubcommands() {
            return Collections.unmodifiableSet(subcommands);
        }

        @Override
        public String toString() {
            return subcommand.name();
        }
    }

}
