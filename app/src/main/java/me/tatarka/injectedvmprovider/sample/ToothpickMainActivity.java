package me.tatarka.injectedvmprovider.sample;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;
import javax.inject.Provider;

import me.tatarka.injectedvmprovider.InjectedViewModelProvider;
import me.tatarka.injectedvmprovider.sample.toothpick.MainModule;
import toothpick.Scope;
import toothpick.Toothpick;

public class ToothpickMainActivity extends AppCompatActivity {

    @Inject
    Provider<MainViewModel> vmProvider;
    @Inject
    ViewModelWithFactory.Factory vmFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scope scope = Toothpick.openScope("ToothpickMainActivity");
        scope.installModules(new MainModule());
        Toothpick.inject(this, scope);
        InjectedViewModelProvider provider = new InjectedViewModelProvider(this, getIntent().getExtras());
        MainViewModel vm1 = provider.get(vmProvider);
        ViewModelWithFactory vm2 = provider.get(vmFactory, ViewModelWithFactory.Factory::create);
        setContentView(R.layout.activity_main);
        TextView textView1 = findViewById(R.id.text1);
        textView1.setText(vm1.getText());
        TextView textView2 = findViewById(R.id.text2);
        textView2.setText(vm2.getText());
    }
}
