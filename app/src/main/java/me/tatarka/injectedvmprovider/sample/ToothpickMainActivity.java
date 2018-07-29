package me.tatarka.injectedvmprovider.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;
import javax.inject.Provider;

import me.tatarka.injectedvmprovider.InjectedViewModelProviders;
import me.tatarka.injectedvmprovider.sample.toothpick.MainModule;
import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.config.Module;

public class ToothpickMainActivity extends AppCompatActivity {

    @Inject
    Provider<MainViewModel> vmProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scope scope = Toothpick.openScope("ToothpickMainActivity");
        scope.installModules(new MainModule());
        Toothpick.inject(this, scope);
        MainViewModel vm = InjectedViewModelProviders.of(this).get(vmProvider);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text);
        textView.setText(vm.getText());
    }
}
