package pl.ml.demo.movies

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TheApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setupImageLoader()
    }

    private fun setupImageLoader() {
        val imageLoader = ImageLoader.Builder(this)
            .logger(DebugLogger())
            .build()
        Coil.setImageLoader(imageLoader)
    }

}