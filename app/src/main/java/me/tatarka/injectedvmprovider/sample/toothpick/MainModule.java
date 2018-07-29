package me.tatarka.injectedvmprovider.sample.toothpick;

import me.tatarka.injectedvmprovider.sample.Source;
import toothpick.config.Module;

public class MainModule extends Module {
    public MainModule() {
        bind(Source.class).toInstance(Source.ToothpickJava);
    }
}
