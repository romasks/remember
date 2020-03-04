package com.remember.app.di.module;

import android.content.Context;

import com.remember.app.Remember;
import com.remember.app.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private final Remember application;

    public ApplicationModule(Remember application) {
        this.application = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Remember provideApplication() {
        return application;
    }

}

