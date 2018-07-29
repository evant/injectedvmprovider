package me.tatarka.injectedvmprovider.ktx

import android.arch.lifecycle.ViewModelStoreOwner
import me.tatarka.injectedvmprovider.InjectedViewModelProvider

inline val ViewModelStoreOwner.injectedViewModelProvider: InjectedViewModelProvider
    get() = InjectedViewModelProvider(this)