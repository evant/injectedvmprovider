package me.tatarka.injectedvmprovider.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Provider;

import me.tatarka.injectedvmprovider.InjectedViewModelProviders;
import me.tatarka.injectedvmprovider.sample.dagger.DaggerMainActivityComponent;

public class DaggerMainActivity extends AppCompatActivity {

    @Inject
    Provider<MainViewModel> vmProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMainActivityComponent.create().inject(this);
        MainViewModel vm = InjectedViewModelProviders.of(this).get(vmProvider);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text);
        textView.setText(vm.getText());
    }
}
