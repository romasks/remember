package com.remember.app.di.component;

import com.remember.app.di.PerScreen;
import com.remember.app.di.module.ScreenModule;

import dagger.Component;

@PerScreen
@Component(modules = ScreenModule.class, dependencies = ApplicationComponent.class)
public interface ScreenComponent {

}
