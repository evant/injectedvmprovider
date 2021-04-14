package me.tatarka.injectedvmprovider

import android.content.Intent
import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import javax.inject.Provider
import kotlin.test.Test
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
class FragmentInjectedViewModelLazyTest {

    @get:Rule
    val instantTaskExecutor = InstantTaskExecutorRule()

    @Test
    fun vmInitialization() {
        val scenario: ActivityScenario<TestActivity> = ActivityScenario.launch(
            Intent(ApplicationProvider.getApplicationContext(), TestActivity::class.java)
                .putExtra("key", "arg3")
        )
        scenario.moveToState(Lifecycle.State.CREATED)

        scenario.onActivity { activity ->
            val fragment = activity.fragment

            assertEquals("arg1", fragment.vm.arg)
            assertEquals("arg1", fragment.lazyVM.arg)
            assertEquals("arg2", fragment.factoryVM.arg)
            assertEquals("arg2", fragment.lazyFactoryVM.arg)
            assertEquals("arg3", fragment.factoryVM2.handle["key"])
            assertEquals("arg3", fragment.lazyFactoryVM2.handle["key"])
        }
    }
}

class TestActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(TestFragment().apply {
                arguments = intent?.extras
            }, "tag")
            .commitNow()
    }

    val fragment: TestFragment
        get() = supportFragmentManager.findFragmentByTag("tag") as TestFragment
}

class TestFragment : Fragment() {
    lateinit var lazyVMProvider: TestViewModelProvider
    lateinit var lazyVMFactory: TestViewModelFactory
    lateinit var lazyVMFactory2: TestViewModelFactory2

    val vm by viewModels(TestViewModelProvider())
    val factoryVM by viewModels("factoryVM") { TestViewModelFactory().create("arg2") }
    val lazyVM by viewModels("lazyVM") { lazyVMProvider.get() }
    val lazyFactoryVM by viewModels("lazyFactoryVM") { lazyVMFactory.create("arg2") }
    val factoryVM2 by viewModels("factoryVM2", TestViewModelFactory2()::create)
    val lazyFactoryVM2 by viewModels("lazyFactoryVM2") { handle -> lazyVMFactory2.create(handle) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lazyVMProvider = TestViewModelProvider()
        lazyVMFactory = TestViewModelFactory()
        lazyVMFactory2 = TestViewModelFactory2()
    }
}

class TestViewModel(val arg: String) : ViewModel()

class TestViewModel2(val handle: SavedStateHandle) : ViewModel()

class TestViewModelProvider : Provider<TestViewModel> {
    override fun get() = TestViewModel("arg1")
}

class TestViewModelFactory {
    fun create(arg: String) = TestViewModel(arg)
}

class TestViewModelFactory2 {
    fun create(handle: SavedStateHandle) = TestViewModel2(handle)
}