package me.tatarka.injectedvmprovider;

/**
 * Creates an instance of a factory, allowing it to pass additional params.
 *
 * @param <F> The factory type.
 * @param <T> The instance to create.
 */
public interface FactoryCreator<F, T> {
    T create(F factory);
}
