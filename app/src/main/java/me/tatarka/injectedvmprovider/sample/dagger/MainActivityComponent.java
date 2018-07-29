package me.tatarka.injectedvmprovider.sample.dagger;

import dagger.Component;
import me.tatarka.injectedvmprovider.sample.DaggerMainActivity;
import me.tatarka.injectedvmprovider.sample.KotlinDaggerMainActivity;

@Component(modules = MainModule.class)
public interface MainActivityComponent {
    void inject(DaggerMainActivity activity);
}
