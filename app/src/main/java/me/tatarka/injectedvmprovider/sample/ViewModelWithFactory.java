package me.tatarka.injectedvmprovider.sample;

import android.util.Log;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;

public class ViewModelWithFactory extends ViewModel {
    private static final String TAG = "ViewModelWithFactory";
    private final Source source;
    private final String arg;

    @AssistedInject
    ViewModelWithFactory(Source source, @Assisted SavedStateHandle handle) {
        this.source = source;
        this.arg = handle.get("arg");
        Log.d(TAG, "created");
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "cleared");
    }

    public String getText() {
        return "Hello " + source + " " + arg + "!";
    }

    @AssistedFactory
    public interface Factory {
        @NonNull
        ViewModelWithFactory create(@NonNull SavedStateHandle handle);
    }
}
