package com.rutershok.phrases

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textfield.TextInputEditText
import com.rutershok.phrases.adapters.PhrasesAdapter
import com.rutershok.phrases.model.Storage.getLanguage
import com.rutershok.phrases.utils.Ad
import com.rutershok.phrases.utils.Key
import com.rutershok.phrases.utils.Snackbar.networkUnavailable
import com.rutershok.phrases.utils.Snackbar.showText
import org.json.JSONObject
import java.net.URLEncoder
import java.util.*

class SearchActivity : AppCompatActivity() {
    private val phrasesList = ArrayList<String>()
    private var phrasesAdapter: PhrasesAdapter? = null
    private var search: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setActionBar()
        setEditTextSearch()
        setRecyclerView()
        Ad.showInterstitialWithFrequency(this)
        Ad.showBanner(this)
    }

    private fun setEditTextSearch() {
        val editTextSearch = findViewById<TextInputEditText>(R.id.edit_text_search)
        //Hide keyboard
        editTextSearch.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                val inputMethodManager =
                    view.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                if (editTextSearch.text != null) {
                    if (editTextSearch.text.toString().isNotEmpty()) {
                        phrasesList.clear()
                        search = editTextSearch.text.toString().toLowerCase(Locale.ROOT)
                        searchPhrases()
                        Ad.showInterstitialWithFrequency(this@SearchActivity)
                    }
                    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
                    return@setOnKeyListener true
                }
            }
            false
        }
    }

    private fun setActionBar() {
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.search_a_phrase)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Finish activity on button click
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        phrasesAdapter = PhrasesAdapter(phrasesList)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_search_phrases)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = phrasesAdapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1)) {
                    searchPhrases()
                }
                //-1 for top scrolling
                Log.e("Position", layoutManager.findFirstVisibleItemPosition().toString() + "")
            }
        })
    }

    private fun searchPhrases() {
        val progressBar = findViewById<ProgressBar>(R.id.pb_loading_phrases)
        progressBar.visibility = View.VISIBLE
        val textNoResultsFound = findViewById<TextView>(R.id.text_no_results_found)
        textNoResultsFound.visibility = View.GONE
        val url = (Key.API_URL + "search_phrases.php"
                    + "?language=" + getLanguage(this)
                    + "&search=" + URLEncoder.encode(search, "UTF-8")
                    + "&from=" + phrasesList.size
                    + "&limit=" + Key.LIMIT)
        Volley.newRequestQueue(this).add(StringRequest(
            Request.Method.GET, url,
            { response: String ->
                    val jsonArray = JSONObject(response).getJSONArray(Key.PHRASES)
                    for (i in 0 until jsonArray.length()) {
                        val `object` = jsonArray.getJSONObject(i)
                        if (!phrasesList.contains(`object`.getString(Key.PHRASE))) {
                            phrasesList.add(`object`.getString(Key.PHRASE))
                            phrasesAdapter!!.notifyDataSetChanged()
                        }
                    }
                    if (jsonArray.length() == 0) {
                        progressBar.visibility = View.GONE
                        showText(this@SearchActivity, R.string.phrases_ended)
                    }
                    progressBar.visibility = View.GONE
                    if (phrasesList.isEmpty()) {
                        textNoResultsFound.visibility = View.VISIBLE
                    }
            }) {
            progressBar.visibility = View.GONE
            Handler().postDelayed({
                networkUnavailable(
                    this@SearchActivity,
                    View.OnClickListener { searchPhrases() })
            }, 400)
        })
    }
}