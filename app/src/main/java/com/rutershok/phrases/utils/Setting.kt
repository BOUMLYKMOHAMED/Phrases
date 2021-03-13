package com.rutershok.phrases.utils

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.preference.Preference
import android.preference.SwitchPreference
import com.rutershok.phrases.model.Storage
import java.util.*

object Setting {
    @JvmStatic
    fun setNotificationsEnabled(context: Context, preference: Preference) {
        Storage.setNotificationsEnabled(context, (preference as SwitchPreference).isChecked)
    }

    @JvmStatic
    fun setAnimations(context: Context, preference: Preference) {
        Storage.setAnimationsState(context, (preference as SwitchPreference).isChecked)
    }

    @JvmStatic
    fun setLanguage(context: Context, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = Configuration(resources.configuration)
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    @JvmStatic
    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        context.startActivity(intent)
    }
}