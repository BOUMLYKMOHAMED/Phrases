package com.rutershok.phrases

import android.os.Bundle
import android.view.MenuItem
import com.rutershok.phrases.model.Storage.getFavoritePhrases
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.rutershok.phrases.adapters.PhrasesAdapter

class FavoritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setActionBar()
        setPhrasesRecycler()
    }

    private fun setPhrasesRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_favorites)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = PhrasesAdapter(getFavoritePhrases(this))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setActionBar() {
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(R.string.favorites)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
        }
    }
}