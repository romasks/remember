package com.remember.app.di.component;

import android.content.Context;

import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.di.ApplicationContext;
import com.remember.app.di.module.ApiModule;
import com.remember.app.di.module.ApplicationModule;
import com.remember.app.di.module.ScreenModule;
import com.remember.app.ui.cabinet.events.EventsPresenter;
import com.remember.app.ui.cabinet.memory_pages.PagePresenter;
import com.remember.app.ui.cabinet.memory_pages.add_page.AddPagePresenter;
import com.remember.app.ui.cabinet.memory_pages.place.PlacePresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ScreenModule.class, ApiModule.class})
public interface ApplicationComponent {

    void inject(Remember application);

    @ApplicationContext
    Context context();

    ServiceNetwork serviceNewrok();

    void inject(PagePresenter presenter);

    void inject(EventsPresenter presenter);

    void inject(PlacePresenter presenter);

    void inject(AddPagePresenter presenter);
}

