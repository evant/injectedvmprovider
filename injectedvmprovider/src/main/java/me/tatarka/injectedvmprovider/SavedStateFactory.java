package me.tatarka.injectedvmprovider;

import androidx.lifecycle.SavedStateHandle;

/**
 * Creates an instance of a type using the given {@link SavedStateHandle}.
 *
 * @param <T>
 */
public interface SavedStateFactory<T> {
    T create(SavedStateHandle handle);
}
