package com.rutershok.phrases.model

import android.app.Activity
import com.android.volley.Request
import com.rutershok.phrases.utils.Network.isAvailable
import com.rutershok.phrases.utils.Snackbar.serverOffline
import com.android.volley.toolbox.Volley
import com.android.volley.toolbox.StringRequest
import com.android.volley.VolleyError

object Database {
    fun checkServerStatus(activity: Activity) {
        if (isAvailable(activity)) {
            val url = "https://www.phrases.netsons.org/api/connection.php"
            Volley.newRequestQueue(activity).add(
                    StringRequest(
                        Request.Method.GET, url,
                        null, { serverOffline(activity) })
                )
        }
    }
}