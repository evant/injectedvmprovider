import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import me.tatarka.injectedvmprovider.InjectedViewModelProvider
import javax.inject.Provider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertSame

class InjectedViewModelProviderTest {

    private val viewModelProvider = InjectedViewModelProvider(ViewModelStore())

    @Test
    fun `two ViewModels with the same key causes a ClassCastException`() {
        val key = "the key"
        viewModelProvider[key, ViewModel1Provider]

        val error = assertFails {
            val vm2 = viewModelProvider[key, ViewModel2Provider]
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
        val model1 = viewModelProvider[ViewModel1Provider]
        val model2 = viewModelProvider[ViewModel2Provider]
        assertSame(model1, viewModelProvider[ViewModel1Provider])
        assertSame(model2, viewModelProvider[ViewModel2Provider])
    }

    @Test
    fun `ViewModels work when passing in a ViewModelStore owner`() {
        val store = ViewModelStore()
        val owner = ViewModelStoreOwner { store }
        val provider = InjectedViewModelProvider(owner)
        val vm = provider[ViewModel1Provider]

        assertSame(provider[ViewModel1Provider], vm)
    }

    @Test
    fun `ViewModel is constructed with provider arg`() {
        val vm = viewModelProvider[ViewModel1Provider]

        assertEquals("arg1", vm.arg)
    }

    @Test
    fun `ViewModel is constructed with first lambda arg`() {
        viewModelProvider[ViewModel1::class.java, Provider { ViewModel1("arg2") }]
        val vm = viewModelProvider[ViewModel1::class.java, Provider { ViewModel1("ignored") }]

        assertEquals("arg2", vm.arg)
    }

    @Test
    fun `ViewModel is constructed with first FactoryCreator arg`() {
        viewModelProvider[ViewModel1Factory, { it.create("arg2") }]
        val vm = viewModelProvider[ViewModel1Factory, { it.create("ignored") }]

        assertEquals("arg2", vm.arg)

    }

    open class ViewModel1(val arg: String = "", var cleared: Boolean = false) : ViewModel() {
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
}