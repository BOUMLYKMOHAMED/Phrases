package com.rutershok.phrases.model;

import android.content.Context;
import android.graphics.Typeface;

import java.util.ArrayList;

public class Font {
    private static final String[] FONTS_NAME = {
            "CherryCreamSoda-Regular.ttf",
            "ComingSoon-Regular.ttf",
            "CraftyGirls-Regular.ttf",
            "Crushed-Regular.ttf",
            "FontdinerSwanky-Regular.ttf",
            "HomemadeApple-Regular.ttf",
            "JustAnotherHand-Regular.ttf",
            "LuckiestGuy-Regular.ttf",
            "MaidenOrange-Regular.ttf",
            "Montez-Regular.ttf",
            "MountainsofChristmas-Regular.ttf",
            "OpenSansHebrewCondensed-Regular.ttf",
            "PermanentMarker-Regular.ttf",
            "Rancho-Regular.ttf",
            "Redressed-Regular.ttf",
            "Roboto-Regular.ttf",
            "RobotoCondensed-Regular.ttf",
            "RobotoMono-Thin.ttf",
            "RobotoSlab-Regular.ttf",
            "Rochester-Regular.ttf",
            "RockSalt-Regular.ttf",
            "Satisfy-Regular.ttf",
            "Schoolbell-Regular.ttf",
            "Slackey-Regular.ttf",
            "Smokum-Regular.ttf",
            "SpecialElite-Regular.ttf",
            "Sunshiney-Regular.ttf",
            "Syncopate-Bold.ttf",
            "Ultra-Regular.ttf",
            "Yellowtail-Regular.ttf",
    };

    private String name;
    private Typeface typeface;

    private Font(String name, Typeface typeface) {
        setName(name);
        setTypeface(typeface);
    }

    public static Typeface getAmatic(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/Amatic.ttf");
    }

    private static Typeface getTypeface(Context context, String fontName) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/" + fontName);
    }

    private static Typeface getSerif() {
        return Typeface.create(Typeface.SERIF, Typeface.NORMAL);
    }

    public static ArrayList<Font> getAll(Context context) {
        ArrayList<Font> listFonts = new ArrayList<>();
        listFonts.add(new Font("Serif", getSerif())); //Default
        for (String font : FONTS_NAME) {
            try {
                String fontName = font.substring(0, font.indexOf("-"));
                listFonts.add(new Font(fontName, getTypeface(context, font)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return listFonts;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    private void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }
}
