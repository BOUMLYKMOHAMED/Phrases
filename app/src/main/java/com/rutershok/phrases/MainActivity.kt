package com.rutershok.phrases

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rutershok.phrases.adapters.CategoriesAdapter
import com.rutershok.phrases.model.Database.checkServerStatus
import com.rutershok.phrases.model.Font
import com.rutershok.phrases.model.Storage.getLanguage
import com.rutershok.phrases.model.Storage.getPremium
import com.rutershok.phrases.notifications.Notification
import com.rutershok.phrases.utils.Ad
import com.rutershok.phrases.utils.Billing
import com.rutershok.phrases.utils.Dialog
import com.rutershok.phrases.utils.Key
import com.rutershok.phrases.utils.Setting.setLanguage

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setLanguage(this, getLanguage(this)!!)
        //Set custom title with different text size
        setTitle()
        //Set buttons of menu on click listener
        setMenuButtons()
        //Set categories recycler view
        setRecyclerView()
        Billing.initialize(this)
        Notification.initialize(this)
        Ad.initialize(this)
        Dialog.showTutorial(this)
        Dialog.rateThisApp(this)

        //Check server status and display message for the user
        checkServerStatus(this)
    }

    private fun setTitle() {
        //Different text size
        val titleString = getString(R.string.app_title) //Setting.getTitle(this);
        val title = SpannableString(titleString)
        title.setSpan(RelativeSizeSpan(1.5f), 0, titleString.indexOf("\n"), 0) // set Size
        val textTitle = findViewById<TextView>(R.id.text_title)
        textTitle.typeface = Font.getAmatic(this)
        textTitle.text = title
    }

    private fun setMenuButtons() {
        //Menu buttons
        val buttons = intArrayOf(
            R.id.button_search,
            R.id.button_create_phrase,
            R.id.button_favorites,
            R.id.button_premium_version,
            R.id.button_settings
        )
        for (button in buttons) {
            findViewById<View>(button).setOnClickListener(this)
        }
    }

    private fun setRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_categories)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = CategoriesAdapter()
        recyclerView.setHasFixedSize(true)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_search -> startActivity(Intent(this, SearchActivity::class.java))
            R.id.button_create_phrase -> startActivity(
                Intent(this, CreatePhraseActivity::class.java).putExtra(
                    Key.TITLE, getString(R.string.create_your_phrase)
                )
            )
            R.id.button_favorites -> if (getPremium(this)) {
                startActivity(Intent(this, FavoritesActivity::class.java))
            } else {
                Dialog.premiumVersion(this)
            }
            R.id.button_premium_version -> Dialog.premiumVersion(this)
            R.id.button_settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}