package com.remember.app.di.component;

import android.content.Context;

import com.remember.app.Remember;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.di.ApplicationContext;
import com.remember.app.di.module.ApiModule;
import com.remember.app.di.module.ApplicationModule;
import com.remember.app.di.module.ScreenModule;
import com.remember.app.ui.auth.AuthPresenter;
import com.remember.app.ui.auth.RegisterPresenter;
import com.remember.app.ui.cabinet.epitaphs.EpitaphsPresenter;
import com.remember.app.ui.cabinet.events.EventsPresenter;
import com.remember.app.ui.cabinet.memory_pages.PagePresenter;
import com.remember.app.ui.cabinet.memory_pages.add_page.AddPagePresenter;
import com.remember.app.ui.cabinet.memory_pages.events.add_new_event.AddNewEventPresenter;
import com.remember.app.ui.cabinet.memory_pages.place.PlacePresenter;
import com.remember.app.ui.grid.GridPresenter;
import com.remember.app.ui.settings.data.PersonalDataFragmentPresenter;
import com.remember.app.ui.settings.notification.NotificationFragmentPresenter;
import com.remember.app.ui.splash.SplashPresenter;

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

    void inject(EpitaphsPresenter presenter);

    void inject(com.remember.app.ui.cabinet.memory_pages.events.EventsPresenter presenter);

    void inject(AddNewEventPresenter presenter);

    void inject(AuthPresenter presenter);

    void inject(RegisterPresenter presenter);

    void inject(PersonalDataFragmentPresenter presenter);

    void inject(NotificationFragmentPresenter presenter);

    void inject(GridPresenter presenter);

    void inject(SplashPresenter presenter);
}

