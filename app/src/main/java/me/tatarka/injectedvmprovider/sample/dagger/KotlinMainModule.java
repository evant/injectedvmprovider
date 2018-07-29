package me.tatarka.injectedvmprovider.sample.dagger;

import dagger.Module;
import dagger.Provides;
import me.tatarka.injectedvmprovider.sample.Source;

@Module
public abstract class KotlinMainModule {
    @Provides
    static Source provideSource() {
        return Source.DaggerKotlin;
    }
}
