@file:Suppress("NOTHING_TO_INLINE")

package me.tatarka.injectedvmprovider

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
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
inline fun <VM : ViewModel> ViewModelStoreOwner.viewModel(provider: Provider<VM>): Lazy<VM> {
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        InjectedViewModelProvider.of(this).get(provider)
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
 *     val myViewModel1 by viewModel { provider.get() }
 *     val myViewModel2 by viewModel { factory.create("arg") }
 * }
 * ```
 */
@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModel(noinline factory: () -> VM): Lazy<VM> {
    return lazy(mode = LazyThreadSafetyMode.NONE) {
        InjectedViewModelProvider.of(this).get(VM::class.java, factory)
    }
}
