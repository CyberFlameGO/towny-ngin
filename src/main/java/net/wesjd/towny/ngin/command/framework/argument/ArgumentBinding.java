package net.wesjd.towny.ngin.command.framework.argument;

import net.wesjd.towny.ngin.command.framework.argument.provider.ArgumentProvider;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Stores data on an argument binding
 */
public class ArgumentBinding<T> {

    /**
     * The argument's provider
     */
    private final ArgumentProvider<T> _argumentProvider;
    /**
     * The arguments specific annotation
     */
    private final Class<? extends Annotation> _annotation;

    /**
     * Store some data on an argument binding
     *
     * @param argumentProvider The {@link ArgumentProvider} for this binding
     * @param annotation The optional annotation for this binding
     */
    public ArgumentBinding(ArgumentProvider<T> argumentProvider, Class<? extends Annotation> annotation) {
        _argumentProvider = argumentProvider;
        _annotation = annotation;
    }

    public ArgumentProvider<T> getArgumentProvider() {
        return _argumentProvider;
    }

    public Optional<Class<? extends Annotation>> getAnnotation() {
        return Optional.ofNullable(_annotation);
    }

    /**
     * A builder to create an {@link ArgumentBinding}
     */
    public static class Builder {

        /**
         * The callback after the builder finishes
         */
        private final Consumer<ArgumentBinding<?>> _callback;

        /**
         * The argument's annotation
         */
        private Class<? extends Annotation> _annotation;

        /**
         * Starts a builder
         *
         * @param callback The callback with the result
         */
        public Builder(Consumer<ArgumentBinding<?>> callback) {
            _callback = callback;
        }

        /**
         * Set the annotation for this argument
         *
         * @param annotation The annotation type
         * @return The builder for later use
         */
        public Builder annotatedWith(Class<? extends Annotation> annotation) {
            _annotation = annotation;
            return this;
        }

        /**
         * Set the provider for this argument
         *
         * @param argumentProvider The provider
         */
        public <I> void toProvider(ArgumentProvider<I> argumentProvider) {
            _callback.accept(new ArgumentBinding<>(argumentProvider, _annotation));
        }

        /**
         * Bind this argument to a single instance
         *
         * @param object The object instance
         */
        public <I> void toInstance(I object) {
            toProvider((parameter, arguments) -> object);
        }

    }

}
