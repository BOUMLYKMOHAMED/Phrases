package com.rutershok.phrases.model

import android.app.Activity
import com.getkeepsafe.taptargetview.TapTarget
import com.rutershok.phrases.R
import java.util.*

enum class Target(private val id: Int, private val titleRes: Int) {
    SEARCH(R.id.button_search, R.string.search_a_phrase), CREATE_PHRASE(
        R.id.button_create_phrase,
        R.string.create_your_phrase
    ),
    FAVORITES(
        R.id.button_favorites, R.string.favorites
    ),
    PREMIUM_VERSION(R.id.button_premium_version, R.string.purchase_premium_version), SETTINGS(
        R.id.button_settings, R.string.settings
    );

    companion object {
        @JvmStatic
        fun getAll(activity: Activity): List<TapTarget> {
            val targetList: MutableList<TapTarget> = ArrayList()
            for (target in values()) {
                targetList.add(
                    TapTarget.forView(
                        activity.findViewById(target.id),
                        activity.getString(target.titleRes)
                    )
                        .targetCircleColor(R.color.white)
                        .titleTextColor(R.color.white)
                        .cancelable(false)
                )
            }
            return targetList
        }
    }
}