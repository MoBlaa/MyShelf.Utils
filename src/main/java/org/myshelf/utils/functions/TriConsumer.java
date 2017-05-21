package org.myshelf.utils.functions;

/**
 * @author MoBlaa
 * @version 0.1
 */
@FunctionalInterface
public interface TriConsumer<T, U, B> {
    void accept(T t, U u, B b);
    default TriConsumer<T, U, B> andThen(final TriConsumer<T, U, B> after) {
        return (T first, U second, B third) ->  {
            this.accept(first, second, third);
            after.accept(first, second, third);
        };
    }
}

