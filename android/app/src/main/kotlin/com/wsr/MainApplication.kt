package com.wsr

import android.app.Application
import com.wsr.setting.store.SettingStore
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(module)
        }
    }

    private val module = module {
        // TODO
        single { MemoController(::producePath) }

        // Assistant
        single { SettingStore(::producePath) }
    }

    private fun producePath(name: String, initialize: Boolean = false) = filesDir
        .resolve(name)
        .apply { if (initialize) delete() }
        .absolutePath
}
