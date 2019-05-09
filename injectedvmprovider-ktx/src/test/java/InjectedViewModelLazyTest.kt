package me.tatarka.injectedvmprovider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import javax.inject.Provider
import kotlin.test.Test
import kotlin.test.assertEquals

class InjectedViewModelLazyTest {

    @Test
    fun vmInitialization() {
        val owner = TestViewModelStoreOwner()
        owner.onCreate()
        assertEquals("arg1", owner.vm.arg)
        assertEquals("arg1", owner.lazyVM.arg)
        assertEquals("arg2", owner.factoryVM.arg)
        assertEquals("arg2", owner.lazyFactoryVM.arg)
    }

    class TestViewModelStoreOwner : ViewModelStoreOwner {
        lateinit var lazyVMProvider: TestViewModelProvider
        lateinit var lazyVMFactory: TestViewModelFactory

        val vm by viewModel(TestViewModelProvider())
        val factoryVM by viewModel { TestViewModelFactory().create("arg2") }
        val lazyVM by viewModel { lazyVMProvider.get() }
        val lazyFactoryVM by viewModel { lazyVMFactory.create("arg2") }

        override fun getViewModelStore(): ViewModelStore = ViewModelStore()

        fun onCreate() {
            lazyVMProvider = TestViewModelProvider()
            lazyVMFactory = TestViewModelFactory()
        }
    }

    class TestViewModel(val arg: String) : ViewModel()

    class TestViewModelProvider : Provider<TestViewModel> {
        override fun get() = TestViewModel("arg1")
    }

    class TestViewModelFactory {
        fun create(arg: String) = TestViewModel(arg)
    }
}