package com.rutershok.phrases.model

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rutershok.phrases.utils.Key
import java.util.*

object Storage {
    private var mPreferences: SharedPreferences? = null
    private val mGson = Gson()
    private fun getPreferences(context: Context): SharedPreferences? {
        if (mPreferences == null) {
            mPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        }
        return mPreferences
    }

    @JvmStatic
    fun addFavoritePhrase(context: Context, phrase: String) {
        val favoritePhrases = getFavoritePhrases(context)
        if (!favoritePhrases.contains(phrase)) {
            favoritePhrases.add(phrase)
            setFavoritePhrases(favoritePhrases)
        }
    }

    @JvmStatic
    fun removeFavoritePhrase(context: Context, phrase: String?) {
        val favoritePhrases: MutableList<String> = getFavoritePhrases(context)
        favoritePhrases.remove(phrase)
        setFavoritePhrases(favoritePhrases)
    }

    @JvmStatic
    fun getFavoritePhrases(context: Context): MutableList<String> {
        val favoritePhrases = mGson.fromJson<MutableList<String>>(
            getPreferences(context)!!
                .getString(Key.FAVORITE_PHRASES, ""),  //Get string
            object : TypeToken<ArrayList<String?>?>() {}.type
        )
        return favoritePhrases ?: ArrayList()
    }

    private fun setFavoritePhrases(favoritePhrases: List<String>) {
        mPreferences!!.edit().putString(Key.FAVORITE_PHRASES, mGson.toJson(favoritePhrases)).apply()
    }

    @JvmStatic
    fun setPremium(context: Context, isPremium: Boolean) {
        getPreferences(context)!!
            .edit().putBoolean(Key.PREMIUM_VERSION, isPremium).apply()
    }

    @JvmStatic
    fun getPremium(context: Context): Boolean {
        return getPreferences(context)!!
            .getBoolean(Key.PREMIUM_VERSION, false)
    }

    @JvmStatic
    fun setLanguage(context: Context, language: String?) {
        getPreferences(context)!!
            .edit().putString(Key.LANGUAGE, language).apply()
    }

    @JvmStatic
    fun getLanguage(context: Context): String? {
        return getPreferences(context)!!
            .getString(Key.LANGUAGE, Locale.getDefault().language)
    }

    @JvmStatic
    fun getNotificationsEnabled(context: Context): Boolean {
        return getPreferences(context)!!
            .getBoolean(Key.PREF_NOTIFICATIONS, true)
    }

    fun setNotificationsEnabled(context: Context, isEnabled: Boolean) {
        getPreferences(context)!!
            .edit().putBoolean(Key.PREF_NOTIFICATIONS, isEnabled).apply()
    }

    fun setAnimationsState(context: Context, isEnabled: Boolean) {
        getPreferences(context)!!
            .edit().putBoolean(Key.ANIMATION, isEnabled).apply()
    }

    @JvmStatic
    fun getAnimationsState(context: Context): Boolean {
        return getPreferences(context)!!
            .getBoolean(Key.ANIMATION, true)
    }

    private fun setInterstitialCount(context: Context, count: Int) {
        getPreferences(context)!!
            .edit().putInt(Key.INTERSTITIAL_COUNT, count).apply()
    }

    @JvmStatic
    fun getInterstitialCount(context: Context): Int {
        return getPreferences(context)!!
            .getInt(Key.INTERSTITIAL_COUNT, 1)
    }

    @JvmStatic
    fun updateInterstitialCount(activity: Activity) {
        setInterstitialCount(activity, getInterstitialCount(activity) + 1)
    }

    @JvmStatic
    fun getIsShowed(activity: Activity, key: String?): Boolean {
        return getPreferences(activity)!!.getBoolean(key, false)
    }

    @JvmStatic
    fun setIsShowed(activity: Activity, key: String?, isShowed: Boolean) {
        getPreferences(activity)!!.edit().putBoolean(key, isShowed).apply()
    }
}