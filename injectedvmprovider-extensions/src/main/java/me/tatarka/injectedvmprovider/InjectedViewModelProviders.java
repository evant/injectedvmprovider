package me.tatarka.injectedvmprovider;

import android.arch.lifecycle.ViewModelStores;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;


/**
 * Utilities methods for {@link android.arch.lifecycle.ViewModelStore} class.
 */
public class InjectedViewModelProviders {

    /**
     * Creates an {@link InjectedViewModelProvider}, which retains ViewModels while a scope of given
     * {@code fragment} is alive. More detailed explanation is in {@link android.arch.lifecycle.ViewModel}.
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
     * is alive. More detailed explanation is in {@link android.arch.lifecycle.ViewModel}.
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
