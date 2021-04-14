import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import androidx.savedstate.SavedStateRegistryOwner
import me.tatarka.injectedvmprovider.InjectedViewModelProvider
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Provider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertSame

@Config(manifest = Config.NONE)
@RunWith(RobolectricTestRunner::class)
class InjectedViewModelProviderTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    private val viewModelProvider = InjectedViewModelProvider(ViewModelStore())

    @Test
    fun `two ViewModels with the same key causes a ClassCastException`() {
        val key = "the key"
        viewModelProvider.get(key, ViewModel1Provider)

        val error = assertFails {
            @Suppress("UNUSED_VARIABLE") // required for the cast
            val vm2 = viewModelProvider.get(key, ViewModel2Provider)
        }
        assertEquals(ClassCastException::class, error::class)
    }

    @Test
    fun `local ViewModel throws IllegalArgumentException`() {
        class VM : ViewModel1()

        val provider = Provider { VM() }

        val error = assertFails {
            viewModelProvider.get(provider)
        }
        assertEquals(IllegalArgumentException::class, error::class)
    }

    @Test
    fun `two ViewModels with different types can both be constructed`() {
        val model1 = viewModelProvider.get(ViewModel1Provider)
        val model2 = viewModelProvider.get(ViewModel2Provider)
        assertSame(model1, viewModelProvider.get(ViewModel1Provider))
        assertSame(model2, viewModelProvider.get(ViewModel2Provider))
    }

    @Test
    fun `ViewModels work when passing in a ViewModelStore owner`() {
        val store = ViewModelStore()
        val owner = ViewModelStoreOwner { store }
        val provider = InjectedViewModelProvider(owner)
        val vm = provider.get(ViewModel1Provider)

        assertSame(provider.get(ViewModel1Provider), vm)
    }

    @Test
    fun `ViewModel is constructed with provider arg`() {
        val vm = viewModelProvider.get(ViewModel1Provider)

        assertEquals("arg1", vm.arg)
    }

    @Test
    fun `ViewModel is constructed with first lambda arg`() {
        viewModelProvider.get(ViewModel1::class.java, Provider { ViewModel1("arg2") })
        val vm = viewModelProvider.get(ViewModel1::class.java, Provider { ViewModel1("ignored") })

        assertEquals("arg2", vm.arg)
    }

    @Test
    fun `ViewModel is constructed with first FactoryCreator arg`() {
        viewModelProvider.get(ViewModel1Factory) { factory: ViewModel1Factory ->
            factory.create("arg2")
        }
        val vm = viewModelProvider.get(ViewModel1Factory) { factory: ViewModel1Factory ->
            factory.create("ignored")
        }

        assertEquals("arg2", vm.arg)
    }

    @Test
    fun `ViewModel is constructed with defaultArg for the SavedStateHandle`() {
        val args = Bundle().apply { putString("key", "value") }
        val viewModelProvider =
            InjectedViewModelProvider(ViewModelStore(), SavedState().savedStateRegistry, args)
        val vm = viewModelProvider.get(ViewModel3Factory) { factory, handle ->
            factory.create(handle)
        }

        assertEquals("value", vm.handle["key"])
    }

    @Test
    fun `ViewModels is constructed with restored SavedStateHandle`() {
        val state = run {
            val savedState = SavedState()
            val viewModelProvider =
                InjectedViewModelProvider(ViewModelStore(), savedState.savedStateRegistry, null)
            val vm = viewModelProvider.get(ViewModel3Factory) { factory, handle ->
                factory.create(handle)
            }
            vm.handle["key"] = "value"
            savedState.save()
        }

        val viewModelProvider =
            InjectedViewModelProvider(ViewModelStore(), SavedState(state).savedStateRegistry, null)
        val vm = viewModelProvider.get(ViewModel3Factory) { factory, handle ->
            factory.create(handle)
        }

        assertEquals("value", vm.handle["key"])
    }

    open class ViewModel1(val arg: String = "") : ViewModel() {

        var cleared: Boolean = false

        override fun onCleared() {
            cleared = true
        }
    }

    object ViewModel1Provider : Provider<ViewModel1> {
        override fun get() = ViewModel1("arg1")
    }

    object ViewModel1Factory {
        fun create(arg: String) = ViewModel1(arg)
    }

    class ViewModel2 : ViewModel()

    object ViewModel2Provider : Provider<ViewModel2> {
        override fun get() = ViewModel2()
    }

    class ViewModel3(val handle: SavedStateHandle) : ViewModel()

    object ViewModel3Factory {
        fun create(handle: SavedStateHandle) = ViewModel3(handle)
    }

    class SavedState(savedState: Bundle? = null) : SavedStateRegistryOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)
        private val controller = SavedStateRegistryController.create(this)

        init {
            controller.performRestore(savedState)
        }

        fun save() = Bundle().apply {
            controller.performSave(this)
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        override fun getSavedStateRegistry(): SavedStateRegistry {
            return controller.savedStateRegistry
        }
    }

}