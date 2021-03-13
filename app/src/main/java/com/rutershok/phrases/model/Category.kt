package com.rutershok.phrases.model

import com.rutershok.phrases.utils.Network.isAvailable
import com.rutershok.phrases.utils.Snackbar.serverOffline
import com.getkeepsafe.taptargetview.TapTarget
import com.google.gson.Gson
import com.rutershok.phrases.model.Storage
import com.google.gson.reflect.TypeToken
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import com.android.volley.VolleyError
import com.rutershok.phrases.R
import java.util.*

enum class Category(val nameResId: Int, val backgroundImage: Int, val keyResId: Int) {
    ALL(R.string.cat_all, R.drawable.bg_all, R.string.key_all), BEAUTIFUL(
        R.string.cat_beautiful,
        R.drawable.bg_beautiful,
        R.string.key_beautiful
    ),
    GOOD_MORNING(
        R.string.cat_good_morning, R.drawable.bg_good_morning, R.string.key_good_morning
    ),
    GOODNIGHT(
        R.string.cat_goodnight, R.drawable.bg_goodnight, R.string.key_goodnight
    ),
    MOTHER(R.string.cat_mother, R.drawable.bg_mother, R.string.key_mother), FATHER(
        R.string.cat_father, R.drawable.bg_father, R.string.key_father
    ),
    BIRTH(R.string.cat_birth, R.drawable.bg_birth, R.string.key_birth), BIRTHDAY(
        R.string.cat_birthday, R.drawable.bg_birthday, R.string.key_birthday
    ),
    VALENTINES_DAY(
        R.string.cat_valentines_day,
        R.drawable.bg_valentines_day,
        R.string.key_valentines_day
    ),
    WOMEN(
        R.string.cat_women, R.drawable.bg_women, R.string.key_women
    ),
    GRANDPARENTS(
        R.string.cat_grandparents,
        R.drawable.bg_grandparents,
        R.string.key_grandparents
    ),
    MURPHYS_LAW(
        R.string.cat_murphys_law, R.drawable.bg_murphys_law, R.string.key_murphys_law
    ),
    LOVE(R.string.cat_love, R.drawable.bg_love, R.string.key_love), ENGAGEMENT(
        R.string.cat_engagement, R.drawable.bg_engagement, R.string.key_engagement
    ),
    MISS_YOU(R.string.cat_miss_you, R.drawable.bg_miss_you, R.string.key_miss_you), FAREWELL(
        R.string.cat_farewell, R.drawable.bg_farewell, R.string.key_farewell
    ),
    SAD(R.string.cat_sad, R.drawable.bg_sad, R.string.key_sad), THANKS(
        R.string.cat_thanks, R.drawable.bg_thanks, R.string.key_thanks
    ),
    TRIP(R.string.cat_trip, R.drawable.bg_trip, R.string.key_trip), WORK(
        R.string.cat_work, R.drawable.bg_work, R.string.key_work
    ),
    DEGREE(R.string.degree, R.drawable.bg_degree, R.string.key_degree), NAME_DAY(
        R.string.cat_name_day, R.drawable.bg_nameday, R.string.key_name_day
    ),
    HEALING(R.string.cat_healing, R.drawable.bg_healing, R.string.key_healing), NEW_HOUSE(
        R.string.cat_new_house, R.drawable.bg_new_house, R.string.key_new_house
    ),
    WEDDING(R.string.cat_wedding, R.drawable.bg_wedding, R.string.key_wedding), ANNIVERSARY(
        R.string.cat_anniversary, R.drawable.bg_anniversary, R.string.key_anniversary
    ),
    HAPPY_EASTER(
        R.string.cat_happy_easter, R.drawable.bg_easter, R.string.key_happy_easter
    ),
    MERRY_CHRISTMAS(
        R.string.cat_merry_christmas, R.drawable.bg_christmas, R.string.key_merry_christmas
    ),
    HALLOWEEN(
        R.string.cat_halloween, R.drawable.bg_halloween, R.string.key_halloween
    ),
    EPIPHANY(R.string.cat_epiphany, R.drawable.bg_epiphany, R.string.key_epiphany);

    companion object {
        @JvmStatic
        val categories = Arrays.asList(*values())
    }
}