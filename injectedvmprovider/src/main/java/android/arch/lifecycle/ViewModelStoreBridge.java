package android.arch.lifecycle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Helper to access {@link ViewModelStore} {@code get} and {@code put} methods which are
 * package-private.
 */
public class ViewModelStoreBridge {

    public static void put(@NonNull ViewModelStore store, @NonNull String key, @NonNull ViewModel viewModel) {
        store.put(key, viewModel);
    }

    @Nullable
    public static ViewModel get(@NonNull ViewModelStore store, @NonNull String key) {
        return store.get(key);
    }
}
