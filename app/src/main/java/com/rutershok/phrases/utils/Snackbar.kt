package com.rutershok.phrases.utils

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.rutershok.phrases.R
import de.mateware.snacky.Snacky

object Snackbar {

    @JvmStatic
    fun networkUnavailable(activity: Activity?, clickListener: View.OnClickListener?) {
        Snacky.builder().setActivity(activity)
            .setTextSize(Key.SNACKBAR_TEXT_SIZE.toFloat())
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_LONG)
            .setText(R.string.network_unavailable)
            .setBackgroundColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
            .setActionText(R.string.reload)
            .setDuration(Snacky.LENGTH_INDEFINITE)
            .setActionClickListener(clickListener)
            .build()
            .show()
    }

    @JvmStatic
    fun showText(activity: Activity, stringResId: Int) {
        Snacky.builder()
            .setActivity(activity)
            .setTextSize(Key.SNACKBAR_TEXT_SIZE.toFloat())
            .setTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_SHORT)
            .setText(activity.getString(stringResId))
            .setBackgroundColor(ContextCompat.getColor(activity, R.color.colorPrimary))
            .build()
            .show()
    }

    @JvmStatic
    fun showText(view: View, stringResId: Int) {
        Snacky.builder()
            .setView(view)
            .setTextSize(Key.SNACKBAR_TEXT_SIZE.toFloat())
            .setTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_SHORT)
            .setText(view.context.getString(stringResId))
            .setBackgroundColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
            .build()
            .show()
    }

    @JvmStatic
    fun serverOffline(activity: Activity?) {
        Snacky.builder()
            .setActivity(activity)
            .setTextSize(Key.SNACKBAR_TEXT_SIZE.toFloat())
            .setTextColor(Color.WHITE)
            .setDuration(Snacky.LENGTH_INDEFINITE)
            .setText(R.string.server_offline)
            .setActionClickListener(null)
            .setActionTextColor(ContextCompat.getColor(activity!!, R.color.white))
            .setActionText(android.R.string.ok)
            .setBackgroundColor(ContextCompat.getColor(activity, R.color.red_500))
            .build()
            .show()
    }
}