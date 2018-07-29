package me.tatarka.injectedvmprovider.sample;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import javax.inject.Inject;

public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";
    private final Source source;

    @Inject
    public MainViewModel(Source source) {
        this.source = source;
        Log.d(TAG, "created");
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "cleared");
    }

    public String getText() {
        return "Hello " + source + "!";
    }
}
