package me.tatarka.injectedvmprovider;

import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;

/**
 * Creates an instance of a factory with a {@link SavedStateHandle}, allowing it to pass additional
 * params.
 *
 * @param <F> The factory type.
 * @param <T> The instance to create.
 */
public interface SavedStateFactoryCreator<F, T> {
    T create(F factory, @NonNull SavedStateHandle handle);
}
