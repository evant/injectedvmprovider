package me.tatarka.injectedvmprovider;

public interface FactoryCreator<F, T> {
    T create(F factory);
}
