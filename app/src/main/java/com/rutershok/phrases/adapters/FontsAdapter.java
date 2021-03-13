package com.rutershok.phrases.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.rutershok.phrases.model.Font;
import com.rutershok.phrases.R;

import java.util.ArrayList;

public class FontsAdapter extends ArrayAdapter<Font> {

    private final ArrayList<Font> fontList;
    private final AppCompatEditText textPhrase;

    public FontsAdapter(@NonNull Context context, @NonNull ArrayList<Font> fonts, AppCompatEditText textPhrase) {
        super(context, 0, fonts);
        this.fontList = fonts;
        this.textPhrase = textPhrase;
    }

    @Override
    public int getCount() {
        return fontList.size();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_font, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final Font font = getItem(position);
        if (font != null) {
            holder.textFont.setText(font.getName());
            holder.textFont.setTypeface(font.getTypeface());
            holder.textFont.setOnClickListener(v -> textPhrase.setTypeface(font.getTypeface()));
        }

        return view;
    }

    static class ViewHolder {
        final TextView textFont;
        ViewHolder(View view) {
            textFont = view.findViewById(R.id.text_font_preview);
        }
    }
}
