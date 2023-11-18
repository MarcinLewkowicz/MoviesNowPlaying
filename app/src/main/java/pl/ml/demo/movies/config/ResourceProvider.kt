package pl.ml.demo.movies.config

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val application: Context
) {

    fun getString(@StringRes resId: Int) =
        application.getString(resId)

}