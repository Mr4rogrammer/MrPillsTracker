package info.mrprogrammer.mrpillstracker.core

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp
import info.mrprogrammer.mrpillstracker.R
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class MainApplication: Application(), ImageLoaderFactory {
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
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(configuration)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.20)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(5 * 1024 * 1024)
                    .build()
            }
            .logger(DebugLogger())
            .respectCacheHeaders(false)
            .build()
    }
}