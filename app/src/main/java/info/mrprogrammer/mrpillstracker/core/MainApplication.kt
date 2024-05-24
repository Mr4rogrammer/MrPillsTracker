package info.mrprogrammer.mrpillstracker.core

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import info.mrprogrammer.mrpillstracker.R
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class MainApplication: Application() {
    override fun onCreate() {
        initModules()
        super.onCreate()
    }

    private fun initModules() {
        realmModule()
    }

    private fun realmModule() {
        Realm.init(this@MainApplication)
        val configuration = RealmConfiguration.Builder()
            .name(getString(R.string.app_name)+".realm")
            .allowWritesOnUiThread(false)
            .allowQueriesOnUiThread(false)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)
    }
}