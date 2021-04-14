package me.tatarka.injectedvmprovider.sample

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.tatarka.injectedvmprovider.sample.dagger.DaggerKotlinMainActivityComponent
import me.tatarka.injectedvmprovider.viewModels
import javax.inject.Inject
import javax.inject.Provider

class KotlinDaggerMainActivity : AppCompatActivity() {

    @Inject
    lateinit var vmProvider: Provider<MainViewModel>

    @Inject
    lateinit var vmFactory: ViewModelWithFactory.Factory

    val vm1 by viewModels(vmProvider)
    val vm2 by viewModels(vmFactory::create)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerKotlinMainActivityComponent.create().inject(this)
        setContentView(R.layout.activity_main)
        val textView1: TextView = findViewById(R.id.text1)
        textView1.text = vm1.text
        val textView2: TextView = findViewById(R.id.text2)
        textView2.text = vm2.text
    }
}

class KotlinDaggerFragment : Fragment(R.layout.activity_main) {
    @Inject
    lateinit var vmProvider: Provider<MainViewModel>

    @Inject
    lateinit var vmFactory: ViewModelWithFactory.Factory

    val vm1 by viewModels(vmProvider)
    val vm2 by viewModels(vmFactory::create)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        DaggerKotlinMainActivityComponent.create().inject(this)
        val textView1: TextView = view.findViewById(R.id.text1)
        textView1.text = vm1.text
        val textView2: TextView = view.findViewById(R.id.text2)
        textView2.text = vm2.text
    }
}