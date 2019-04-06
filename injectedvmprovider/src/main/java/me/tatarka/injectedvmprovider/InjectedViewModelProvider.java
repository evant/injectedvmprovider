package me.tatarka.injectedvmprovider;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreBridge;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import javax.inject.Provider;

/**
 * An utility class that provides {@code ViewModels} for a scope. Instead of using a
 * {@link androidx.lifecycle.ViewModelProvider.Factory}, instances of obtains from injected
 * {@link Provider}'s.
 * <p>
 * Default {@code ViewModelProvider} for an {@code Activity} or a {@code Fragment} can be obtained
 * from {@link InjectedViewModelProvider} class.
 * <p>
 * Usage:
 * <pre>{@code
 * @Inject
 * Provider<MyViewModel> vmProvider;
 *
 * @Override
 * public void onCreate(@Nullable Bundle savedInstanceState) {
 *     ...
 *     MyViewModel vm = InjectedViewModelProviders.of(this).get(vmProvider);
 * }
 * }</pre>
 */
public class InjectedViewModelProvider {

    private static final String DEFAULT_KEY =
            "android.arch.lifecycle.ViewModelProvider.DefaultKey";
    @NonNull
    private final ViewModelStore store;

    /**
     * Creates {@code ViewModelProvider}, which will create {@code ViewModels} and retain them in a
     * store of the given {@code ViewModelStoreOwner}.
     *
     * @param owner a {@code ViewModelStoreOwner} whose {@link ViewModelStore} will be used to
     *              retain {@code ViewModels}
     */
    public InjectedViewModelProvider(@NonNull ViewModelStoreOwner owner) {
        this(owner.getViewModelStore());
    }

    /**
     * Creates {@code ViewModelProvider}, which will create {@code ViewModels} and retain them in
     * the given {@code store}.
     *
     * @param store {@code ViewModelStore} where ViewModels will be stored.
     */
    public InjectedViewModelProvider(@NonNull ViewModelStore store) {
        this.store = store;
    }

    /**
     * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this {@code ViewModelProvider}.
     * <p>
     * The created ViewModel is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param provider The provider of the ViewModel to create an instance of it if it is not
     *                 present.
     * @param <T>      The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     */
    @NonNull
    @MainThread
    public <T extends ViewModel> T get(@NonNull Provider<T> provider) {
        String canonicalName = provider.getClass().getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, provider);
    }

    /**
     * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this {@code ViewModelProvider}.
     * <p>
     * The created ViewModel is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param key      The key to use to identify the ViewModel.
     * @param provider The provider of the ViewModel to create an instance of it if it is not
     *                 present.
     * @param <T>      The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     */
    @NonNull
    @MainThread
    public <T extends ViewModel> T get(@NonNull String key, @NonNull Provider<T> provider) {
        ViewModel viewModel = ViewModelStoreBridge.get(store, key);
        if (viewModel == null) {
            viewModel = provider.get();
            ViewModelStoreBridge.put(store, key, viewModel);
        }
        //noinspection unchecked
        return (T) viewModel;
    }

    /**
     * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this {@code ViewModelProvider}.
     * <p>
     * The created ViewModel is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param factory The factory of the ViewModel.
     * @param creator The factory creator of the ViewModel to create an instance of it if it is not
     *                present.
     * @param <F>     The type parameter for the factory.
     * @param <T>     The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     */
    @NonNull
    @MainThread
    public <F, T extends ViewModel> T get(@NonNull F factory, FactoryCreator<F, T> creator) {
        String canonicalName = factory.getClass().getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, factory, creator);
    }

    /**
     * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this {@code ViewModelProvider}.
     * <p>
     * The created ViewModel is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param key      The key to use to identify the ViewModel.
     * @param factory The factory of the ViewModel.
     * @param creator The factory creator of the ViewModel to create an instance of it if it is not
     *                present.
     * @param <F>     The type parameter for the factory.
     * @param <T>     The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     */
    @NonNull
    @MainThread
    public <F, T extends ViewModel> T get(@NonNull String key, @NonNull F factory, FactoryCreator<F, T> creator) {
        ViewModel viewModel = ViewModelStoreBridge.get(store, key);
        if (viewModel == null) {
            viewModel = creator.create(factory);
            ViewModelStoreBridge.put(store, key, viewModel);
        }
        //noinspection unchecked
        return (T) viewModel;
    }
}
