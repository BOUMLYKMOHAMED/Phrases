package com.rutershok.phrases

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.rutershok.phrases.adapters.PhrasesAdapter
import com.rutershok.phrases.model.Storage.getLanguage
import com.rutershok.phrases.utils.Ad
import com.rutershok.phrases.utils.Key
import com.rutershok.phrases.utils.Snackbar.networkUnavailable
import com.rutershok.phrases.utils.Snackbar.showText
import org.json.JSONException
import org.json.JSONObject
import java.net.URLEncoder
import java.util.*

class PhrasesActivity : AppCompatActivity() {
    private val mPhrasesList = ArrayList<String>()
    private val mPhrasesAdapter = PhrasesAdapter(mPhrasesList)
    private var categoryName: String? = null
    private var categoryKey: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phrases)
        categoryName = intent.getStringExtra(Key.CATEGORY_NAME)
        categoryKey = intent.getStringExtra(Key.CATEGORY_KEY)
        setActionBar()
        setPhrasesRecycler()
        Ad.showBanner(this)
    }

    private fun setActionBar() {
        if (supportActionBar != null) {
            supportActionBar!!.title = categoryName
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }

    private fun setPhrasesRecycler() {
        loadPhrases()
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_phrases)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mPhrasesAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    loadPhrases()
                    // Ad.showInterstitialWithFrequency(activity);
                }
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadPhrases() {
        val progressBar = findViewById<ProgressBar>(R.id.pb_loading_phrases)
        progressBar.visibility = View.VISIBLE
        val url = (Key.API_URL + "get_phrases.php"
                + "?language=" + getLanguage(this)
                + "&category=" + URLEncoder.encode(categoryKey, "UTF-8")
                + "&from=" + mPhrasesList.size
                + "&limit=" + Key.LIMIT)

        Volley.newRequestQueue(this).add(StringRequest(
            Request.Method.GET, url,
            { response: String ->
                try {
                    val jsonArray = JSONObject(response).getJSONArray(Key.PHRASES)
                    for (i in 0 until jsonArray.length()) {
                        mPhrasesList.add(jsonArray.getJSONObject(i).getString(Key.PHRASE))
                        mPhrasesAdapter.notifyDataSetChanged()
                    }
                    if (jsonArray.length() == 0) {
                        showText(this@PhrasesActivity, R.string.phrases_ended)
                        progressBar.visibility = View.GONE
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                } finally {
                    progressBar.visibility = View.GONE
                }
            }) {
            progressBar.visibility = View.GONE
            networkUnavailable(
                this@PhrasesActivity,
                View.OnClickListener { loadPhrases() })
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}