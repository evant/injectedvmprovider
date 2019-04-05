package me.tatarka.injectedvmprovider.sample;

import android.util.Log;

import javax.inject.Inject;

import androidx.lifecycle.ViewModel;

public class ViewModelWithFactory extends ViewModel {
    private static final String TAG = "ViewModelWithFactory";
    private final Source source;
    private final String arg;

    ViewModelWithFactory(Source source, String arg) {
        this.source = source;
        this.arg = arg;
        Log.d(TAG, "created");
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "cleared");
    }

    public String getText() {
        return "Hello " + source + " " + arg + "!";
    }

    public static class Factory {
        private final Source source;

        @Inject
        public Factory(Source source) {
            this.source = source;
        }

        public ViewModelWithFactory create(String arg) {
            return new ViewModelWithFactory(source, arg);
        }
    }
}
