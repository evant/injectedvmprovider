@file:Suppress("NOTHING_TO_INLINE")

package me.tatarka.injectedvmprovider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistryOwner
import javax.inject.Provider

/**
 * Returns a [Lazy] delegate to access the ViewModelStoreOwner's ViewModel. The given provider will
 * be used to construct the ViewModel the first time it's accessed.
 *
 * ```
 * class MyFragment @Inject constructor(private val provider) : Fragment() {
 *     val myViewModel by viewModel(provider)
 * }
 * ```
 */
@MainThread
@Deprecated(message = "Use viewModels() instead", replaceWith = ReplaceWith("viewModels(provider)"))
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel(provider: Provider<VM>): Lazy<VM> {
    return viewModels(provider)
}

/**
 * Returns a [Lazy] delegate to access the ViewModelStoreOwner's ViewModel. The given provider will
 * be used to construct the ViewModel the first time it's accessed.
 *
 * ```
 * class MyFragment @Inject constructor(private val provider) : Fragment() {
 *     val myViewModel by viewModels(provider)
 * }
 * ```
 */
@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModels(provider: Provider<VM>): Lazy<VM> {
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        InjectedViewModelProvider(this).get(VM::class.java, provider)
    }
}

/**
 * Returns a [Lazy] delegate to access the ViewModelStoreOwner's ViewModel. The given factory will
 * be used to construct the ViewModel the first time it's accessed. This is useful if the
 * construction of the ViewModel takes parameters, or if the provider needs to be lazily provider
 * because this declaration happens before injection.
 *
 * ```
 * class MyComponentActivity : ComponentActivity() {
 *     lateinit var provider: Provider<MyViewModel1>
 *     lateinit var factory: MyViewModel2.Factory
 *
 *     val myViewModel1 by viewModels { provider.get() }
 *     val myViewModel2 by viewModels { factory.create("arg") }
 * }
 * ```
 */
@MainThread
@Deprecated(message = "Use viewModels() instead", replaceWith = ReplaceWith("viewModels(factory)"))
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel(noinline factory: () -> VM): Lazy<VM> {
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        InjectedViewModelProvider(this).get(VM::class.java, factory)
    }
}

/**
 * Returns a [Lazy] delegate to access the ViewModelStoreOwner's ViewModel. The given factory will
 * be used to construct the ViewModel the first time it's accessed. This is useful if the
 * construction of the ViewModel requires a [SavedStateHandle].
 *
 * ```
 * class MyComponentActivity : ComponentActivity() {
 *     lateinit var factory: MyViewModel.Factory
 *
 *     val myViewModel by viewModels { handle -> factory.create(handle) }
 * }
 * ```
 *
 * @param defaultArgs the default arguments to create the [SavedStateHandle] with
 * @param factory the factory to create the [ViewModel] using a [SavedStateHandle]
 */
@MainThread
inline fun <O, reified VM : ViewModel> O.viewModels(
    crossinline defaultArgs: () -> Bundle?,
    noinline factory: (handle: SavedStateHandle) -> VM
): Lazy<VM> where O : ViewModelStoreOwner, O : SavedStateRegistryOwner {
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        InjectedViewModelProvider(this, defaultArgs()).get(VM::class.java, factory)
    }
}

/**
 * Returns a [Lazy] delegate to access the ViewModelStoreOwner's ViewModel. The given factory will
 * be used to construct the ViewModel the first time it's accessed. This is useful if the
 * construction of the ViewModel requires a [SavedStateHandle].
 *
 * ```
 * class MyComponentActivity : ComponentActivity() {
 *     lateinit var factory: MyViewModel.Factory
 *
 *     val myViewModel by viewModels { handle -> factory.create(handle) }
 * }
 * ```
 *
 * @param key a unique key for this [ViewModel]
 * @param defaultArgs the default arguments to create the [SavedStateHandle] with
 * @param factory the factory to create the [ViewModel] using a [SavedStateHandle]
 */
@MainThread
inline fun <O, VM : ViewModel> O.viewModels(
    key: String,
    noinline defaultArgs: () -> Bundle?,
    noinline factory: (handle: SavedStateHandle) -> VM
): Lazy<VM> where O : ViewModelStoreOwner, O : SavedStateRegistryOwner {
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        InjectedViewModelProvider(this, defaultArgs()).get(key, factory)
    }
}

/**
 * Returns a [Lazy] delegate to access the ViewModelStoreOwner's ViewModel. The given factory will
 * be used to construct the ViewModel the first time it's accessed. This is useful if the
 * construction of the ViewModel requires a [SavedStateHandle].
 *
 * ```
 * class MyComponentActivity : ComponentActivity() {
 *     lateinit var factory: MyViewModel.Factory
 *
 *     val myViewModel by viewModels { handle -> factory.create(handle) }
 * }
 * ```
 *
 * @param factory the factory to create the [ViewModel] using a [SavedStateHandle]
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(noinline factory: (handle: SavedStateHandle) -> VM): Lazy<VM> {
    return viewModels({ intent?.extras }, factory)
}

/**
 * Returns a [Lazy] delegate to access the ViewModelStoreOwner's ViewModel. The given factory will
 * be used to construct the ViewModel the first time it's accessed. This is useful if the
 * construction of the ViewModel requires a [SavedStateHandle].
 *
 * ```
 * class MyComponentActivity : ComponentActivity() {
 *     lateinit var factory: MyViewModel.Factory
 *
 *     val myViewModel by viewModels { handle -> factory.create(handle) }
 * }
 * ```
 *
 * @param key a unique key for this [ViewModel]
 * @param factory the factory to create the [ViewModel] using a [SavedStateHandle]
 */
@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    key: String,
    noinline factory: (handle: SavedStateHandle) -> VM
): Lazy<VM> {
    return viewModels(key, { intent?.extras }, factory)
}
