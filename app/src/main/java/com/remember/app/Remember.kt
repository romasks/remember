package com.remember.app

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.google.android.play.core.splitcompat.SplitCompat
import com.pixplicity.easyprefs.library.Prefs
import com.remember.app.di.DI_MODULES
import com.remember.app.di.component.ApplicationComponent
import com.remember.app.di.component.DaggerApplicationComponent
import com.remember.app.di.module.ApplicationModule
import com.vk.sdk.VKSdk
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import ru.mail.auth.sdk.MailRuAuthSdk
import java.util.*


class Remember : Application() {

    companion object {
        lateinit var applicationComponent: ApplicationComponent
        @JvmField
        var active = false
    }

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        initKoin()
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

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(base)
        LanguageHelper.init(base!!)
        val ctx = LanguageHelper.getLanguageConfigurationContext(base)
        SplitCompat.install(ctx)
        super.attachBaseContext(ctx)

    }

    private fun initKoin() {
        startKoin {
            androidContext(this@Remember)
            modules(DI_MODULES)
        }
    }
}
    internal const val LANG_RU = "ru"
    internal const val LANG_PL = "pl"

    private const val PREFS_LANG = "language"

    /**
     * A singleton helper for storing and retrieving the user selected language in a
     * SharedPreferences instance. It is required for persisting the user language choice between
     * application restarts.
     */
    object LanguageHelper {
        lateinit var prefs: SharedPreferences
        var language: String
            get() {
                return prefs.getString(PREFS_LANG, LANG_RU)!!
            }
            set(value) {
                prefs.edit().putString(PREFS_LANG, value).apply()
            }

        fun init(ctx: Context){
            prefs = ctx.getSharedPreferences(PREFS_LANG, Context.MODE_PRIVATE)
        }

        /**
         * Get a Context that overrides the language selection in the Configuration instance used by
         * getResources() and getAssets() by one that is stored in the LanguageHelper preferences.
         *
         * @param ctx a base context to base the new context on
         */
        fun getLanguageConfigurationContext(ctx: Context): Context {
            val conf = getLanguageConfiguration()
            return ctx.createConfigurationContext(conf)
        }

        /**
         * Get an empty Configuration instance that only sets the language that is
         * stored in the LanguageHelper preferences.
         * For use with Context#createConfigurationContext or Activity#applyOverrideConfiguration().
         */
        fun getLanguageConfiguration(): Configuration {
            val conf = Configuration()
            conf.setLocale(Locale.forLanguageTag(language))
            return conf
        }


    }

