package me.tatarka.injectedvmprovider.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import me.tatarka.injectedvmprovider.ktx.injectedViewModelProvider
import me.tatarka.injectedvmprovider.sample.dagger.DaggerKotlinMainActivityComponent
import javax.inject.Inject
import javax.inject.Provider

class KotlinDaggerMainActivity : AppCompatActivity() {

    @Inject
    lateinit var vmProvider: Provider<MainViewModel>
    @Inject
    lateinit var vmFactory: ViewModelWithFactory.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerKotlinMainActivityComponent.create().inject(this)
        val vm1 = injectedViewModelProvider.get(vmProvider)
        val vm2 = injectedViewModelProvider.get(vmFactory) { it.create("arg") }
        setContentView(R.layout.activity_main)
        val textView1: TextView = findViewById(R.id.text1)
        textView1.text = vm1.text
        val textView2: TextView = findViewById(R.id.text2)
        textView2.text = vm2.text
    }
}