package com.remember.app.di

import android.content.Context
import android.content.SharedPreferences
import com.remember.app.data.dataFlow.DataManager
import com.remember.app.data.dataFlow.network.ApiService
import com.remember.app.ui.chat.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelsModule: Module = module {
  //  viewModel { AuthViewModel(get(), get()) }
  //  viewModel { UKViewModel(get(), get()) }
    viewModel { ChatViewModel(get()) }
  //  viewModel { ProfileViewModel(get(), get()) }
  //  viewModel { CounterViewModel(get(), get()) }
}
val sharedPreferences = module {
    single {
        getSharedPrefs(get())
    }
}
val apiModule = module {
    single { ApiService.getApi() }
}

private val repositoryModule: Module = module {
    single { DataManager(get()) }
}

//private val fireBaseAuth: Module = module {
//    single { FirebaseAuth.getInstance() }
//}

val DI_MODULES =
    listOf(viewModelsModule, apiModule, sharedPreferences, repositoryModule)

private fun getSharedPrefs(context: Context): SharedPreferences {
    return context.getSharedPreferences("default", Context.MODE_PRIVATE)
}