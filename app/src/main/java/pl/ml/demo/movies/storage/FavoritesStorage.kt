package pl.ml.demo.movies.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoritesStorage @Inject constructor(
    @ApplicationContext application: Context
) {

    private val SHARED_PREFS_NAME = "favorites"
    private val SHARED_PREFS_KEY_FAVORITES = "favorites_json"

    private val sharedPreferences =
        application.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    fun setFavorites(set: Set<Int>) {
        val json = Json.encodeToString(set)
        sharedPreferences.edit().putString(SHARED_PREFS_KEY_FAVORITES, json).apply()
    }

    fun getFavorites(): Set<Int> {
        val jsonStr = sharedPreferences.getString(SHARED_PREFS_KEY_FAVORITES, null)
            ?: return emptySet()
        return try {
            Json.decodeFromString(jsonStr)
        } catch (e: Exception) {
            // In case of malformed data
            return emptySet()
        }
    }


}