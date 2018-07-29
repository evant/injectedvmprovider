package me.tatarka.injectedvmprovider;

import androidx.lifecycle.ViewModelStores;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


/**
 * Utilities methods for {@link androidx.lifecycle.ViewModelStore} class.
 */
public class InjectedViewModelProviders {

    /**
     * Creates an {@link InjectedViewModelProvider}, which retains ViewModels while a scope of given
     * {@code fragment} is alive. More detailed explanation is in {@link androidx.lifecycle.ViewModel}.
     *
     * @param fragment a fragment, in whose scope ViewModels should be retained
     * @return a ViewModelProvider instance
     */
    @MainThread
    public static InjectedViewModelProvider of(@NonNull Fragment fragment) {
        return new InjectedViewModelProvider(ViewModelStores.of(fragment));
    }

    /**
     * Creates an {@link InjectedViewModelProvider}, which retains ViewModels while a scope of given Activity
     * is alive. More detailed explanation is in {@link androidx.lifecycle.ViewModel}.
     *
     * @param activity an activity, in whose scope ViewModels should be retained
     * @return a ViewModelProvider instance
     */
    @NonNull
    @MainThread
    public static InjectedViewModelProvider of(@NonNull FragmentActivity activity) {
        return new InjectedViewModelProvider(ViewModelStores.of(activity));
    }
}
