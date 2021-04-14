package me.tatarka.injectedvmprovider.sample.dagger;

import dagger.Component;
import me.tatarka.injectedvmprovider.sample.DaggerMainActivity;
import me.tatarka.injectedvmprovider.sample.KotlinDaggerFragment;
import me.tatarka.injectedvmprovider.sample.KotlinDaggerMainActivity;

@Component(modules = KotlinMainModule.class)
public interface KotlinMainActivityComponent {
    void inject(KotlinDaggerMainActivity activity);

    void inject(KotlinDaggerFragment fragment);
}
