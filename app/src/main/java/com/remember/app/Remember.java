package com.remember.app;

import android.app.Application;
import android.content.ContextWrapper;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.di.component.ApplicationComponent;
import com.remember.app.di.component.DaggerApplicationComponent;
import com.remember.app.di.module.ApplicationModule;
import com.vk.sdk.VKSdk;

import io.fabric.sdk.android.Fabric;

public class Remember extends Application {

    private static ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);
        VKSdk.initialize(this);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        applicationComponent.inject(this);

    }

    public static ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
