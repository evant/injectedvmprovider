package me.tatarka.injectedvmprovider.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Provider;

import me.tatarka.injectedvmprovider.FactoryCreator;
import me.tatarka.injectedvmprovider.InjectedViewModelProviders;
import me.tatarka.injectedvmprovider.sample.dagger.DaggerMainActivityComponent;

public class DaggerMainActivity extends AppCompatActivity {

    @Inject
    Provider<MainViewModel> vmProvider;
    @Inject
    ViewModelWithFactory.Factory vmFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMainActivityComponent.create().inject(this);
        MainViewModel vm1 = InjectedViewModelProviders.of(this).get(vmProvider);
        ViewModelWithFactory vm2 = InjectedViewModelProviders.of(this).get(vmFactory, factory -> factory.create("arg"));
        setContentView(R.layout.activity_main);
        TextView textView1 = findViewById(R.id.text1);
        textView1.setText(vm1.getText());
        TextView textView2 = findViewById(R.id.text2);
        textView2.setText(vm2.getText());
    }
}
