package me.tatarka.injectedvmprovider;

import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleBridge;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreBridge;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;

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
 *     MyViewModel vm = new InjectedViewModelProvider(this).get(vmProvider);
 * }
 * }</pre>
 */
public class InjectedViewModelProvider {

    private static final String DEFAULT_KEY =
            "androidx.lifecycle.ViewModelProvider.DefaultKey";

    /**
     * Creates an {@link InjectedViewModelProvider}, which retains ViewModels while a scope of given
     * {@code owner} is alive. More detailed explanation is in {@link androidx.lifecycle.ViewModel}.
     *
     * @param owner a {@code ViewModelStoreOwner} whose {@link ViewModelStore} will be used to
     *              retain {@code ViewModels}
     * @return an InjectedViewModelProvider instance
     * @deprecated Use the constructor
     * {@link InjectedViewModelProvider#InjectedViewModelProvider(ViewModelStoreOwner)} instead.
     */
    @NonNull
    @MainThread
    @Deprecated
    public static InjectedViewModelProvider of(@NonNull ViewModelStoreOwner owner) {
        return new InjectedViewModelProvider(owner.getViewModelStore());
    }

    @NonNull
    private final ViewModelStore store;
    @Nullable
    private final SavedStateRegistry registry;
    @Nullable
    private final Bundle defaultArgs;

    /**
     * Creates {@code ViewModelProvider}, which will create {@code ViewModels} and retain them in a
     * store of the given {@code ViewModelStoreOwner}.
     *
     * @param owner a {@code ViewModelStoreOwner} whose {@link ViewModelStore} will be used to
     *              retain {@code ViewModels}
     */
    @MainThread
    public InjectedViewModelProvider(@NonNull ViewModelStoreOwner owner) {
        this(owner.getViewModelStore());
    }

    /**
     * Creates {@code ViewModelProvider}, which will create {@code ViewModels} and retain them in a
     * store of the given {@code ViewModelStoreOwner}. This version of the constructor allows you to
     * call the methods that provide a {@link SavedStateHandle}.
     *
     * @param owner       a {@code ViewModelStoreOwner} whose {@link ViewModelStore} will be used to
     *                    retain {@code ViewModels}
     * @param defaultArgs the default arguments to pass when creating a {@link SavedStateHandle}
     */
    public <O extends ViewModelStoreOwner & SavedStateRegistryOwner> InjectedViewModelProvider(@NonNull O owner, @Nullable Bundle defaultArgs) {
        this(owner.getViewModelStore(), owner.getSavedStateRegistry(), defaultArgs);
    }

    /**
     * Creates {@code ViewModelProvider}, which will create {@code ViewModels} and retain them in
     * the given {@code store}.
     *
     * @param store {@code ViewModelStore} where ViewModels will be stored.
     */
    @MainThread
    public InjectedViewModelProvider(@NonNull ViewModelStore store) {
        this.store = store;
        this.registry = null;
        this.defaultArgs = null;
    }

    /**
     * Creates {@code ViewModelProvider}, which will create {@code ViewModels} and retain them in
     * the given {@code store}. This version of the constructor allows you to call the methods that
     * provide a {@link SavedStateHandle}.
     *
     * @param store       {@code ViewModelStore} where ViewModels will be stored.
     * @param registry    the saved state registry to use when creating a {@code SavedStateHandle}
     * @param defaultArgs the default arguments to pass when creating a {@code SavedStateHandle}
     */
    @MainThread
    public InjectedViewModelProvider(@NonNull ViewModelStore store, @NonNull SavedStateRegistry registry, @Nullable Bundle defaultArgs) {
        this.store = store;
        this.registry = registry;
        this.defaultArgs = defaultArgs;
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
     * @throws IllegalArgumentException If the given provider is a local or anonymous class. If this
     *                                  is the case, you must use {@link #get(String, Provider)} or {@link #get(Class, Provider)}
     *                                  instead so a unique key can be derived.
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
     * @param viewModelClass The view model class, used as a unique key.
     * @param provider       The provider of the ViewModel to create an instance of it if it is not
     *                       present.
     * @param <T>            The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     * @throws IllegalArgumentException If the given viewModelClass is a local or anonymous class.
     */
    @NonNull
    @MainThread
    public <T extends ViewModel> T get(@NonNull Class<T> viewModelClass, Provider<T> provider) {
        String canonicalName = viewModelClass.getCanonicalName();
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
    public <F, T extends ViewModel> T get(@NonNull F factory, @NonNull FactoryCreator<F, T> creator) {
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
     * @param key     The key to use to identify the ViewModel.
     * @param factory The factory of the ViewModel.
     * @param creator The factory creator of the ViewModel to create an instance of it if it is not
     *                present.
     * @param <F>     The type parameter for the factory.
     * @param <T>     The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     */
    @NonNull
    @MainThread
    public <F, T extends ViewModel> T get(@NonNull String key, @NonNull F factory, @NonNull FactoryCreator<F, T> creator) {
        ViewModel viewModel = ViewModelStoreBridge.get(store, key);
        if (viewModel == null) {
            viewModel = creator.create(factory);
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
     * @param viewModelClass The view model class, used as a unique key.
     * @param factory        The factory to create an instance of the ViewModel of it is not already
     *                       present.
     * @param <T>            The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     * @throws IllegalArgumentException If the given viewModelClass is a local or anonymous class.
     * @throws IllegalArgumentException If a {@link SavedStateRegistry} wasn't provided to the constructor.
     */
    @NonNull
    @MainThread
    public <T extends ViewModel> T get(@NonNull Class<T> viewModelClass, SavedStateFactory<T> factory) {
        String canonicalName = viewModelClass.getCanonicalName();
        if (canonicalName == null) {
            throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
        }
        return get(DEFAULT_KEY + ":" + canonicalName, factory);
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
     * @throws IllegalArgumentException If a {@link SavedStateRegistry} wasn't provided to the constructor.
     */
    @NonNull
    @MainThread
    public <F, T extends ViewModel> T get(@NonNull F factory, @NonNull SavedStateFactoryCreator<F, T> creator) {
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
     * @param key     The key to use to identify the ViewModel.
     * @param factory The factory of the ViewModel.
     * @param creator The factory creator of the ViewModel to create an instance of it if it is not
     *                present.
     * @param <F>     The type parameter for the factory.
     * @param <T>     The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     * @throws IllegalArgumentException If a {@link SavedStateRegistry} wasn't provided to the constructor.
     */
    @NonNull
    @MainThread
    public <F, T extends ViewModel> T get(@NonNull String key, @NonNull final F factory, @NonNull final SavedStateFactoryCreator<F, T> creator) {
        return get(key, new SavedStateFactory<T>() {
            @Override
            public T create(SavedStateHandle handle) {
                return creator.create(factory, handle);
            }
        });
    }

    /**
     * Returns an existing ViewModel or creates a new one in the scope (usually, a fragment or
     * an activity), associated with this {@code ViewModelProvider}.
     * <p>
     * The created ViewModel is associated with the given scope and will be retained
     * as long as the scope is alive (e.g. if it is an activity, until it is
     * finished or process is killed).
     *
     * @param key     The key to use to identify the ViewModel.
     * @param factory The factory to create an instance of the ViewModel of it is not already
     *                present.
     * @param <T>     The type parameter for the ViewModel.
     * @return A ViewModel that is an instance of the given type {@code T}.
     * @throws IllegalArgumentException If a {@link SavedStateRegistry} wasn't provided to the constructor.
     */
    @NonNull
    @MainThread
    public <T extends ViewModel> T get(@NonNull String key, @NonNull SavedStateFactory<T> factory) {
        ViewModel viewModel = ViewModelStoreBridge.get(store, key);
        if (viewModel == null) {
            if (registry == null) {
                throw new IllegalArgumentException("must provide a SavedStateRegistry to obtain a SavedStateHandle");
            }
            Bundle restoredState = registry.consumeRestoredStateForKey(key);
            SavedStateHandle handle = SavedStateHandleBridge.createHandle(restoredState, defaultArgs);
            registry.registerSavedStateProvider(key, SavedStateHandleBridge.savedStateProvider(handle));

            viewModel = factory.create(handle);
            ViewModelStoreBridge.put(store, key, viewModel);
        }
        //noinspection unchecked
        return (T) viewModel;
    }
}
