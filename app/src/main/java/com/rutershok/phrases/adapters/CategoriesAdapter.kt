package com.rutershok.phrases.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rutershok.phrases.utils.Ad
import com.rutershok.phrases.PhrasesActivity
import androidx.cardview.widget.CardView
import com.rutershok.phrases.R
import com.rutershok.phrases.model.Category
import com.rutershok.phrases.model.Font.getAmatic
import com.rutershok.phrases.utils.Key

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {
    private val categories = Category.categories
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        Glide.with(context).load(categories[position].backgroundImage).into(holder.imageBackground)
        val categoryName = context.resources.getString(categories[position].nameResId)
        val categoryKey = context.resources.getString(categories[position].keyResId)
        holder.textCategory.text = categoryName
        holder.textCategory.typeface = getAmatic(context)
        holder.cardCategory.setOnClickListener {
            Ad.showInterstitialWithFrequency(context as Activity)
            context.startActivity(
                Intent(context, PhrasesActivity::class.java)
                    .putExtra(Key.CATEGORY_NAME, categoryName)
                    .putExtra(Key.CATEGORY_KEY, categoryKey)
            )
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardCategory: CardView = view.findViewById(R.id.card_category_item)
        val textCategory: TextView = view.findViewById(R.id.text_phrases_category_name)
        val imageBackground: ImageView = view.findViewById(R.id.image_phrases_category_background)
    }
}