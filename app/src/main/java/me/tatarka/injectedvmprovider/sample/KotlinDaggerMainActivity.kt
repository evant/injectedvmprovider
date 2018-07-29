package me.tatarka.injectedvmprovider.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import me.tatarka.injectedvmprovider.ktx.injectedViewModelProvider
import me.tatarka.injectedvmprovider.sample.dagger.DaggerKotlinMainActivityComponent
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class KotlinDaggerMainActivity : AppCompatActivity() {

    @Inject
    @Named("DaggerKotlin")
    lateinit var vmProvider: Provider<MainViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerKotlinMainActivityComponent.create().inject(this)
        val vm = injectedViewModelProvider[vmProvider]
        setContentView(R.layout.activity_main)
        val textView: TextView = findViewById(R.id.text)
        textView.text = vm.text
    }
}