package me.tatarka.injectedvmprovider

import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(noinline factory: (handle: SavedStateHandle) -> VM): Lazy<VM> {
    return viewModels({ arguments }, factory)
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    key: String,
    noinline factory: (handle: SavedStateHandle) -> VM
): Lazy<VM> {
    return viewModels(key, { arguments }, factory)
}
