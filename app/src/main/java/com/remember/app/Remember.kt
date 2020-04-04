package com.remember.app

import android.app.Application
import android.content.ContextWrapper
import com.pixplicity.easyprefs.library.Prefs
import com.remember.app.di.component.ApplicationComponent
import com.remember.app.di.component.DaggerApplicationComponent
import com.remember.app.di.module.ApplicationModule
import com.vk.sdk.VKSdk
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import ru.mail.auth.sdk.MailRuAuthSdk

class Remember : Application() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        MailRuAuthSdk.initialize(this)
        val config = YandexMetricaConfig.newConfigBuilder(getString(R.string.METRICA_API_KEY))
                .withLogs()
                .build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)

        Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(packageName)
                .setUseDefaultSharedPreference(true)
                .build()
        applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule(this))
                .build()
        applicationComponent.inject(this)


    }
}
