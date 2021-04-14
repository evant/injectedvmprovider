package androidx.lifecycle;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.savedstate.SavedStateRegistry;

/**
 * Helper to access {@link SavedStateHandle} {@code createHandle} and {@code savedStateProvider}
 * methods which are package-private.
 */
public class SavedStateHandleBridge {

    @NonNull
    public static SavedStateHandle createHandle(@Nullable Bundle restoredState, @Nullable Bundle defaultState) {
        return SavedStateHandle.createHandle(restoredState, defaultState);
    }

    @NonNull
    public static SavedStateRegistry.SavedStateProvider savedStateProvider(@NonNull SavedStateHandle handle) {
        return handle.savedStateProvider();
    }
}
