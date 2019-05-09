package me.tatarka.injectedvmprovider.ktx

import androidx.lifecycle.ViewModelStoreOwner
import me.tatarka.injectedvmprovider.InjectedViewModelProvider

@Deprecated(message = "Use InjectedViewModelProvider.of() or by viewModel() instead", replaceWith = ReplaceWith("InjectedViewModelProvider.of(this)"))
inline val ViewModelStoreOwner.injectedViewModelProvider: InjectedViewModelProvider
    get() = InjectedViewModelProvider(this)

