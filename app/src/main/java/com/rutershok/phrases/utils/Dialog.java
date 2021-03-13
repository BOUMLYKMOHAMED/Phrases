package com.rutershok.phrases.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;

import com.flask.colorpicker.ColorPickerView;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.google.android.material.textfield.TextInputEditText;
import com.kobakei.ratethisapp.RateThisApp;
import com.rutershok.phrases.adapters.FontsAdapter;
import com.rutershok.phrases.model.Font;
import com.rutershok.phrases.model.Storage;
import com.rutershok.phrases.model.Target;
import com.rutershok.phrases.R;

import java.util.Locale;

public class Dialog {

    public static void premiumVersion(final Activity activity) {
        if (!Storage.getPremium(activity)) {
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.purchase_premium_version)
                    .setMessage(R.string.premium_version_body)
                    .setPositiveButton(R.string.purchase, (dialog, which) -> Billing.purchasePremium(activity))
                    .setNegativeButton(R.string.video, (dialog, which) -> Ad.unlockPremium(activity)).setNeutralButton(android.R.string.no, null)
                    .show();
        } else {
            Snackbar.showText(activity, R.string.you_already_have_the_premium_version);
        }
    }

    public static void changeTextColor(Activity activity, final AppCompatEditText textInputEditText) {
        final int currentPhraseColor = textInputEditText.getCurrentTextColor();
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.dialog_change_text_color, null, false);
        ColorPickerView colorPickerView = view.findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorChangedListener(textInputEditText::setTextColor);

        new AlertDialog.Builder(activity)
                .setView(view)
                .setIcon(R.drawable.ic_change_text_color)
                .setTitle(R.string.change_text_color)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    //Restore default values
                    textInputEditText.setTextColor(currentPhraseColor);
                }).create().show();
    }

    public static void modifyPhrase(final Activity activity, final AppCompatEditText textInputEditText) {
        final float currentPhraseSize = textInputEditText.getTextSize() / activity.getResources().getDisplayMetrics().scaledDensity;
        final int currentPhraseColor = textInputEditText.getCurrentTextColor();

        //Custom view
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.dialog_modify_phrase, null, false);
        final TextInputEditText editTextPhrase = view.findViewById(R.id.edit_text_phrase);
        if (textInputEditText.getText() != null) {
            editTextPhrase.setText(textInputEditText.getText().toString());
            editTextPhrase.setTextColor(textInputEditText.getCurrentTextColor());
            editTextPhrase.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInputEditText.setText(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        //Text size: [value]
        final TextView textTextSize = view.findViewById(R.id.text_phrase_size);
        textTextSize.setText(String.format(Locale.US, "%s %f", activity.getString(R.string.text_size), currentPhraseSize));
        SeekBar seekBarPhraseSize = view.findViewById(R.id.seek_bar_phrase_size);
        seekBarPhraseSize.setProgress((int) ((currentPhraseSize - 10) / activity.getResources().getDisplayMetrics().scaledDensity));
        seekBarPhraseSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float newPhraseSize = 10 + progress * activity.getResources().getDisplayMetrics().scaledDensity;
                textInputEditText.setTextSize(newPhraseSize);
                textTextSize.setText(String.format(Locale.US, "%s %f", activity.getString(R.string.text_size), newPhraseSize));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //Show Dialog
        new AlertDialog.Builder(activity)
                .setView(view)
                .setIcon(R.drawable.ic_modify_phrase)
                .setTitle(R.string.modify_phrase)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    //Restore default values
                    textInputEditText.setTextColor(currentPhraseColor);
                    textInputEditText.setTextSize(currentPhraseSize);
                }).create().show();
    }

    public static void changeBackground(final Activity activity, final ImageView imageView) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.dialog_modify_background, null, false);
        final Drawable currentBackgroundDrawable = imageView.getDrawable();
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        view.findViewById(R.id.button_choose_photo).setOnClickListener(v -> {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI).setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, activity.getString(R.string.select_image));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            activity.startActivityForResult(chooserIntent, Key.PICK_IMAGE);
            dialog.create().dismiss();
        });

        view.findViewById(R.id.button_make_photo).setOnClickListener(v -> {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(cameraIntent, Key.CAMERA_REQUEST);
            dialog.create().dismiss();
        });

        ColorPickerView colorPickerView = view.findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorChangedListener(selectedColor -> imageView.setImageDrawable(new ColorDrawable(selectedColor)));

        dialog.setView(view)
                .setIcon(R.drawable.ic_change_background)
                .setTitle(R.string.select_image)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, (dialog1, which) -> imageView.setImageDrawable(currentBackgroundDrawable)).create();
        //Show Dialog
        dialog.show();
    }

    public static void modifyFont(final Activity activity, final AppCompatEditText textInputEditText) {
        if (Storage.getPremium(activity)) {
            @SuppressLint("InflateParams") View view = LayoutInflater.from(activity).inflate(R.layout.dialog_modify_font, null, false);
            final Typeface currentTypeface = textInputEditText.getTypeface();
            //Font chooser
            ListView listView = view.findViewById(R.id.listview_fonts);
            listView.setAdapter(new FontsAdapter(activity, Font.getAll(activity), textInputEditText));

            new AlertDialog.Builder(activity)
                    .setView(view)
                    .setIcon(R.drawable.ic_modify_font)
                    .setTitle(R.string.select_font)
                    .setPositiveButton(android.R.string.ok, null)
                    .setNegativeButton(android.R.string.cancel, (dialog, which) -> textInputEditText.setTypeface(currentTypeface)).show();
        } else {
            premiumVersion(activity);
        }
    }

    public static void gdprConsent(final Activity activity) {
        if (!activity.isFinishing()) {
            View view = LayoutInflater.from(activity).inflate(R.layout.dialog_eu_consent, null, false);
            new AlertDialog.Builder(activity)
                    .setTitle(R.string.data_protection_consent)
                    .setIcon(R.drawable.ic_data_protection_consent)
                    .setView(view)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> ConsentInformation.getInstance(activity).setConsentStatus(ConsentStatus.PERSONALIZED)).setNegativeButton(android.R.string.no, (dialog, which) -> ConsentInformation.getInstance(activity).setConsentStatus(ConsentStatus.NON_PERSONALIZED)).show();
        }
    }

    public static void showTutorial(Activity activity) {
        if (!Storage.getIsShowed(activity, Key.TUTORIAL)) {
            new TapTargetSequence(activity).targets(Target.getAll(activity))
                    .start();
            Storage.setIsShowed(activity, Key.TUTORIAL, true);
        }
    }

    public static void rateThisApp(Activity activity) {
        RateThisApp.onCreate(activity);
        RateThisApp.showRateDialogIfNeeded(activity);
    }
}