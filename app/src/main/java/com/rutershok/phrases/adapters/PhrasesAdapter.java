package com.rutershok.phrases.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rutershok.phrases.CreatePhraseActivity;
import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.R;
import com.rutershok.phrases.utils.Key;
import com.rutershok.phrases.utils.Share;
import com.rutershok.phrases.utils.Snackbar;

import java.util.List;

public class PhrasesAdapter extends RecyclerView.Adapter<PhrasesAdapter.ViewHolder> {

    private final List<String> mPhrases;
    private int mLastPosition = -1;

    public PhrasesAdapter(List<String> phrases) {
        this.mPhrases = phrases;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_phrase, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        setAnimation(holder, position);
        final String phrase = mPhrases.get(position)
                .replaceAll("\n", " ")
                .replaceAll("\\n", " ")
                .replaceAll("\\t", " ")
                .replaceAll("\t", " ")
                .replaceAll(" +", " ").trim();
        holder.textPhrase.setText(phrase);

        if (Storage.getFavoritePhrases(holder.itemView.getContext()).contains(phrase)) {
            holder.imageFavorite.setImageResource(R.drawable.ic_favorite_button_on);
        }

        setListeners(holder, phrase);
    }

    private void setListeners(final ViewHolder holder, final String phrase) {
        holder.imageShare.setOnClickListener(view -> startEditor(view, phrase));
        holder.imageFavorite.setOnClickListener(view -> setFavorites(holder, phrase));
        holder.imageCopyToClipboard.setOnClickListener(view -> {
            Share.copyToClipboard(view.getContext(), phrase);
            Snackbar.showText(view, R.string.phrase_has_been_copied_to_clipboard);
        });
        holder.itemView.setOnClickListener(view -> startEditor(view, phrase));
    }

    private void startEditor(View view, String phrase) {
        view.getContext().startActivity(new Intent(view.getContext(), CreatePhraseActivity.class)
                .putExtra(Key.PHRASE, phrase).putExtra(Key.TITLE, view.getContext().getString(R.string.share)));
    }

    private void setAnimation(ViewHolder holder, int position) {
        if (Storage.getAnimationsState(holder.itemView.getContext())) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(),
                    (position > mLastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            holder.itemView.startAnimation(animation);
            mLastPosition = holder.getAdapterPosition();
        }
    }

    private void setFavorites(ViewHolder holder, String phrase) {
        Context context = holder.itemView.getContext();
        int favoriteImage = R.drawable.ic_favorite_button_off;
        if (Storage.getFavoritePhrases(context).contains(phrase)) {
            Storage.removeFavoritePhrase(context, phrase);
        } else {
            Storage.addFavoritePhrase(context, phrase);
            favoriteImage = R.drawable.ic_favorite_button_on;
        }
        holder.imageFavorite.setImageResource(favoriteImage);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return mPhrases.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView textPhrase;
        final ImageView imageFavorite;
        final ImageView imageShare;
        final ImageView imageCopyToClipboard;

        ViewHolder(View view) {
            super(view);
            textPhrase = view.findViewById(R.id.text_phrase_body);
            imageFavorite = view.findViewById(R.id.image_phrase_favorite);
            imageShare = view.findViewById(R.id.image_phrase_share);
            imageCopyToClipboard = view.findViewById(R.id.image_phrase_copy_to_clipboard);
        }
    }
}